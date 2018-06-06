package edu.gvsu.cis.videre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.parceler.Parcels;
import org.parceler.transfuse.annotations.Bind;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupActivity extends AppCompatActivity {

    private Device newDevice;
    private DeviceType newDeviceType;
    private String newDeviceName;

    @BindView(R.id.rBtnGroup) RadioGroup rBtnGroup;
    @BindView(R.id.deviceNameTxt) EditText deviceNameTxt;

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
            newDevice = new Device(newDeviceName, true, newDeviceType);

            // Parcel the new object up and pass it back to the device activity.
            Intent resultIntent = new Intent();
            Bundle retBundle = new Bundle();
            retBundle.putParcelable("device", Parcels.wrap(newDevice));
            resultIntent.putExtra("device", retBundle);
            setResult(Activity.RESULT_OK, resultIntent);
            finish(); // Close the current activity.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initially set the device type to "moved".
        rBtnGroup.check(R.id.rBtnMoved);
    }
}
