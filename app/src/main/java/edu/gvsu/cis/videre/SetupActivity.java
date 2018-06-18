package edu.gvsu.cis.videre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.parceler.Parcels;
import org.parceler.transfuse.annotations.Bind;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupActivity extends AppCompatActivity {

    // Used to request a bluetooth device from the bluetooth activity.
    public static final int BLUETOOTH_DEVICE_ADDRESS_REQUEST = 1;
    private Device newDevice;
    private DeviceType newDeviceType;
    private String newDeviceName;
    private BluetoothLeService mBleService;

    @BindView(R.id.rBtnGroup) RadioGroup rBtnGroup;
    @BindView(R.id.deviceNameTxt) EditText deviceNameTxt;
    @BindView(R.id.txtBluetoothAddress) TextView txtBluetoothAddress;

    @OnClick(R.id.btnBluetoothDevice) void btnBluetoothDevice() {
        Intent intent = new Intent(SetupActivity.this, BluetoothActivity.class);
        startActivityForResult(intent, BLUETOOTH_DEVICE_ADDRESS_REQUEST );
    }

    /**
     * Gets the device data from the activity views.
     */
    private void getDeviceData() {
        // Get the device type from the radio buttons.
        int selectedId = rBtnGroup.getCheckedRadioButtonId();
        RadioButton rBtn = (RadioButton) findViewById(selectedId);
        newDeviceType = DeviceType.valueOf(rBtn.getText().toString().toUpperCase());
        newDeviceName = deviceNameTxt.getText().toString();
    }

    /**
     * Click listener for the fab on this activity.
     */
    @OnClick(R.id.fab) void fab() {
        getDeviceData();
        if(newDeviceName.equals("")) {
            Snackbar.make(findViewById(R.id.setupLayout), R.string.device_name_must_be_given, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            // Create the new device.
            newDevice = new Device();
            newDevice.id = newDeviceName;
            newDevice.inUse = true;
            newDevice.deviceType = newDeviceType;
            newDevice.latitude = 0.0;
            newDevice.longitude = 0.0;

//           if(mBleService.isBluetoothConnected()) {
 //               if(mBleService.btSendData(String.valueOf(newDeviceType.getVal()))) {
                    mBleService.disconnect(); // Connection no longer needed.
                    // Parcel the new object up and pass it back to the device activity.
                    Intent resultIntent = new Intent();
                    Bundle retBundle = new Bundle();
                    retBundle.putParcelable("device", Parcels.wrap(newDevice));
                    resultIntent.putExtra("device", retBundle);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish(); // Close the current activity.
//                } else {
//                    Snackbar.make(findViewById(R.id.setupLayout), "BLE Data Transfer Failed!", Snackbar.LENGTH_LONG)
//                            .show();
//                }
//            } else {
//                Snackbar.make(findViewById(R.id.setupLayout), "Bluetooth Not Connected!", Snackbar.LENGTH_LONG)
//                        .show();
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBleService = BluetoothLeService.getInstance();

        // Initially set the device type to "moved".
        rBtnGroup.check(R.id.rBtnMoved);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BLUETOOTH_DEVICE_ADDRESS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Unwrap the new device and add it to the device list.
                String btName = data.getStringExtra("BT_NAME");
                txtBluetoothAddress.setText(btName); // We will be able to connect to it at this point.
            }
        }
    }
}
