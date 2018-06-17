package edu.gvsu.cis.videre;

import android.app.Fragment;
import android.bluetooth.BluetoothClass;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import org.parceler.Parcels;

import edu.gvsu.cis.videre.dummy.DeviceContent;

public class DeviceActivity extends AppCompatActivity
        implements DeviceFragment.OnListFragmentInteractionListener {

    static final int NEW_DEVICE_REQUEST = 0; // The request code.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(DeviceActivity.this, SetupActivity.class);
            startActivityForResult(intent, NEW_DEVICE_REQUEST);
        });
    }

    /**
     * Adds a new device to the list of devices if the device name is not already in use.
     * @param newDevice
     *      The device to add.
     */
    private void addDeviceToList(Device newDevice) {
        if(DeviceContent.addItem(newDevice)) {
            // Update the dataset if the item was successfully added.
            ((DeviceFragment)getFragmentManager().findFragmentById(R.id.recyclerViewFragment)).updateDataSet();
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Title");
//
//        // Set up the input
//        final EditText input = new EditText(this);
//        // Specify the type of input expected.
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Device newDevice = DeviceContent.createDevice(input.getText().toString());
//                addDeviceToList(newDevice);
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();

        Intent intent = new Intent(DeviceActivity.this, MapsActivity.class);
        Parcelable parcel  =  Parcels.wrap(item);
        intent.putExtra("DEVICE",parcel);
        setResult(RESULT_OK,intent);
        finish();
    }
}
