package edu.gvsu.cis.videre;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothActivity extends AppCompatActivity
        implements BluetoothFragment.OnListFragmentInteractionListener {

    final static int REQUEST_ENABLE_BT = 1;     // Used to request bluetooth permissions from user.
    final static int REQUEST_FINE_LOCATION = 2; // Used to request location permissions from user.
    boolean mScanning = false; // Flag that indicates whether the device is scanning.
    HashMap<String, BluetoothDevice> mScanResults; // Map of found devices.
    ScanCallback mScanCallback;
    private BluetoothManager mBluetoothManager;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothLeService mBluetoothLeService;
    Handler mHandler; // To have a fixed scan period.
    final int SCAN_PERIOD = 10000; // scan period in ms.
    private BluetoothItem mBluetoothItem;

    public static List<BluetoothItem> bluetoothDevices;

    String TAG = "Bluetooth";
    BluetoothAdapter mBluetoothAdapter;

    @BindView(R.id.txtBtStatus) TextView txtBtStatus;

    @OnClick(R.id.btnStartScan) void btnStartScan() {
        startScan();
    }
    @OnClick(R.id.btnStopScan) void btnStopScan() {
        stopScan();
    }

    private boolean hasPermissions() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            requestBluetoothEnable();
            return false;
        } else if (!hasLocationPermissions()) {
            requestLocationPermission();
            return false;
        }
        return true;
    }

    private void startScan() {
        if (!hasPermissions() || mScanning) {
            txtBtStatus.setText("Not allowed!");

            return;
        }
        // No filters. Scan mode is low power as this is BLE baby.
        List<ScanFilter> filters = new ArrayList<>();
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();

        mScanResults = new HashMap<>();
        bluetoothDevices.clear();
        updateFragmentAdapter();
        mScanCallback = new BtleScanCallback();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);

        txtBtStatus.setText("Started Scan!");

        mScanning = true;
        mHandler = new Handler();
        mHandler.postDelayed(this::stopScan, SCAN_PERIOD);
    }

    private void stopScan() {
        if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
            txtBtStatus.setText("Stopped Scan!");
            scanComplete();
        }

        mScanCallback = null;
        mScanning = false;
        mHandler = null;
    }

    private void scanComplete() {

        if (mScanResults.isEmpty()) {
            txtBtStatus.setText("No Results");

            return;
        }
        for (String deviceAddress : mScanResults.keySet()) {
            txtBtStatus.setText("Found Device");

            Log.d(TAG, "Found device: " + deviceAddress);
        }
    }

    /**
     * Get permissions for Bluetooth from the user.
     */
    private void requestBluetoothEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        Log.d(TAG, "Requested user enables Bluetooth. Try starting the scan again.");
    }

    /**
     * Checks if the app has location permissions.
     *
     * @return
     *      True if the app has location permission. False otherwise.
     */
    private boolean hasLocationPermissions() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests location permissions from the user.
     */
    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothDevices = new ArrayList<BluetoothItem>();

        setContentView(R.layout.activity_bluetooth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Make sure BLE is possible on the current device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            txtBtStatus.setText("BLE not supported...");
          //  finish();
        }

        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        // Get bluetooth adapter that allows basic BLE operations such as scans.
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mScanResults = new HashMap<>();
        mBluetoothLeService = BluetoothLeService.getInstance();
    }

    @Override
    public void onListFragmentInteraction(BluetoothItem item) {
        Intent resultIntent = new Intent();
        if(!mBluetoothLeService.connect(item.address, txtBtStatus, mBluetoothManager, mBluetoothAdapter)) {
            Snackbar.make(findViewById(R.id.bluetoothLayout), R.string.bt_connection_failed, Snackbar.LENGTH_LONG)
                    .show();
        }
        resultIntent.putExtra("BT_NAME", item.name);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private class BtleScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            txtBtStatus.setText("Got Scan Result!");
            addScanResult(result);
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {

            txtBtStatus.setText("Got batch scan result!");

            for (ScanResult result : results) {
                addScanResult(result);
            }
        }
        @Override
        public void onScanFailed(int errorCode) {

            txtBtStatus.setText("Scan failed!");

            Log.e(TAG, "BLE Scan Failed with code " + errorCode);
        }
        private void addScanResult(ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String deviceAddress = device.getAddress();
            mBluetoothItem = new BluetoothItem();
            mBluetoothItem.name = device.getName();
            mBluetoothItem.address = deviceAddress;

            // Add the scan result if it is not already present.
            if(!bluetoothDevices.contains(mBluetoothItem)) {
                mScanResults.put(deviceAddress, device);
                bluetoothDevices.add(mBluetoothItem);
                updateFragmentAdapter();
            }
        }
    }

    private void updateFragmentAdapter() {
        ((BluetoothFragment)getSupportFragmentManager()
                .findFragmentById(R.id.bluetoothFragment))
                .btFragmentAdapter.notifyDataSetChanged();
    }
}
