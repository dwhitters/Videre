package edu.gvsu.cis.videre;

import android.location.Location;
import android.os.AsyncTask;

import org.joda.time.DateTime;

import java.util.List;

import static java.lang.Thread.sleep;

public class DemoTask extends AsyncTask<String, Void, Void>{

    public static List<Device> userDevices; // List of all the user's devices.

    private boolean mIncreaseLongitude = true;


    // Constructor
    public DemoTask() {userDevices = DeviceActivity.userDevices;}

    @Override
    protected Void doInBackground(String... strings) {

        int count = 0;

        while(true) {
            count++;
            try {
                sleep(10000); // Wait 5 seconds.
            } catch (InterruptedException e) {
                // Do nothing.
            }

            // Don't update move device history.
            for(Device d : userDevices) {
                if(d.deviceType != DeviceType.MOVE) {
                    addToHistoryList(d);
                }
            }
        }
    }

    /**
     * Adds to the history of the device.
     *
     * @param device
     *      The device who's history is being modified.
     */
    public void addToHistoryList(Device device) {
        DeviceLocation location = new DeviceLocation();
        location.latitude = device.history.get(device.history.size()-1).latitude;
        location.longitude = device.history.get(device.history.size()-1).longitude;
        location.timeStamp = DateTime.now().toString().split("\\.")[0];

        if(!device.isActivated()) {
            if(device.deviceType == DeviceType.STOLEN) {
                DeviceLocation orgLocation = device.history.get(0);
                float ar[] = {0};
                Location.distanceBetween(orgLocation.latitude, orgLocation.longitude, location.latitude, location.longitude, ar);
                if(ar[0] > 1000) {
                    device.activated = true;
                    CurrentSession.getInstance().getDatabaseRef().child("devices").child(device.key).child("activated").setValue(device.activated);
                }
            }
        }
        if(mIncreaseLongitude) {
            double newLong = ((location.longitude + 180.01) % 360) - 180;
            location.longitude = newLong;
            mIncreaseLongitude = false;
        } else {
            double newLat = ((location.latitude + 180.01) % 360) - 180;
            location.latitude = newLat;
            mIncreaseLongitude = true;
        }
        device.history.add(location);
        CurrentSession.getInstance().getDatabaseRef().child("devices").child(device.key).child("history").setValue(device.history);
    }
}
