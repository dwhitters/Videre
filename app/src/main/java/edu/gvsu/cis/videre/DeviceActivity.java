package edu.gvsu.cis.videre;
import android.app.Fragment;
import android.bluetooth.BluetoothClass;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceActivity extends AppCompatActivity
        implements DeviceFragment.OnListFragmentInteractionListener {

    DatabaseReference deviceRef;
    // Shows whether the device list for the user is created yet.
    private boolean deviceListExists = true;

    static final int NEW_DEVICE_REQUEST = 0; // The request code.
    public static List<Device> userDevices; // List of all the user's devices.
	
    private CurrentSession mCurrentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDevices = new ArrayList<Device>();
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(DeviceActivity.this, SetupActivity.class);
            startActivityForResult(intent, NEW_DEVICE_REQUEST);
        });

        mCurrentSession = CurrentSession.getInstance();

        DatabaseReference rootRef = mCurrentSession.getDatabaseRef().child("devices");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                deviceListExists = false; // Set when the devices node is non-existent.
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if(deviceListExists) {
            deviceRef.removeEventListener(chEvListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userDevices.clear();
        if(deviceListExists) {
            deviceRef = mCurrentSession.getDatabaseRef().child("devices");
            deviceRef.addChildEventListener(chEvListener);
        }
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Device entry = dataSnapshot.getValue(Device.class);
            entry.key = dataSnapshot.getKey();
            userDevices.add(entry);

            // Update the dataset if the item was successfully added.
            ((DeviceFragment)getFragmentManager().findFragmentById(R.id.recyclerViewFragment)).updateDataSet();
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Device entry = (Device)
                    dataSnapshot.getValue(Device.class);
            List<Device> newUserDeviceList = new ArrayList<Device>();
            for (Device t : userDevices) {
                if (!t.key.equals(dataSnapshot.getKey())) {
                    newUserDeviceList.add(t);
                }
            }
            userDevices = newUserDeviceList;
        }
        @Override

        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    /**
     * Creates a device list for the user on the Firebase database.
     *
     * @param newDevice
     *      The new device to be added to the new list.
     */
    private void createNewDeviceList(Device newDevice) {
        HashMap<String, String> testMap = new HashMap<>();
        testMap.put("test", "test"); // Workaround to create a list under a specific node on the database.
        mCurrentSession.getDatabaseRef().child("devices").setValue(testMap);
        mCurrentSession.getDatabaseRef().child("devices").push().setValue(newDevice);
        mCurrentSession.getDatabaseRef().child("devices").child("test").removeValue();
    }

    /**
     * Adds a new device to the list of devices if the device name is not already in use.
     * @param newDevice
     *      The device to add.
     */
    private void addDeviceToList(Device newDevice) {
        boolean itemNameNotInUse = true; // Flag that is set to false when the item name is in use.
        for(Device device : userDevices) {
            if(device.id.equalsIgnoreCase(newDevice.id)) {
                itemNameNotInUse = false;
                break;
            }
        }
        if(itemNameNotInUse) {
            if (deviceListExists) {
                deviceRef.push().setValue(newDevice);
            } else {
                createNewDeviceList(newDevice);
                deviceListExists = true;
                onResume(); // Set the listener and device ref now.
            }
        } else {
            Snackbar.make(findViewById(R.id.deviceCoordinatorLayout), R.string.device_name_in_use, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_DEVICE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Unwrap the new device and add it to the device list.
                Device newDevice = Parcels.unwrap(data.getBundleExtra("device").getParcelable("device"));
                addDeviceToList(newDevice);
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Device item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go to Map?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected.
        input.setInputType(InputType.TYPE_NULL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Device newDevice = DeviceContent.createDevice(input.getText().toString());
//                addDeviceToList(newDevice);
                Intent intent = new Intent(DeviceActivity.this, MapsActivity.class);
                intent.putExtra("Latitude",item.latitude);
                intent.putExtra("Longitude",item.longitude);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
//        Intent intent = new Intent(DeviceActivity.this, MapsActivity.class);
//        startActivity(intent);
//        Parcelable parcel  =  Parcels.wrap(item);
//        intent.putExtra("DEVICE",parcel);
//        setResult(RESULT_OK,intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);

        if(!handled) {
            int id = item.getItemId();
            if(id == R.id.action_signout) {
                Intent intent  = new Intent(DeviceActivity.this, SigninActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                handled = true;
            }
        }

        return handled;
    }
}
