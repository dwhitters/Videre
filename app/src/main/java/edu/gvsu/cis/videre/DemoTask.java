package edu.gvsu.cis.videre;

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

        while(true) {
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
     * Clears all items in the history except for the last one. This is the starting location for the
     * device now.
     *
     * @param device
     *      The device who's history is being modified.
     */
    public void addToHistoryList(Device device) {
        DeviceLocation location = new DeviceLocation();
        location.latitude = device.history.get(device.history.size()-1).latitude;
        location.longitude = device.history.get(device.history.size()-1).longitude;
        location.timeStamp = DateTime.now().toString().split("\\.")[0];
        if(mIncreaseLongitude) {
            double newLong = ((location.longitude + 181) % 360) - 180;
            location.longitude = newLong;
            mIncreaseLongitude = false;
        } else {
            double newLat = ((location.latitude + 181) % 360) - 180;
            location.latitude = newLat;
            mIncreaseLongitude = true;
        }
        device.history.add(location);
        CurrentSession.getInstance().getDatabaseRef().child("devices").child(device.key).child("history").setValue(device.history);
    }
}
