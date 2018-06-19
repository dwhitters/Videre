package edu.gvsu.cis.videre;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap mMap;
    MarkerOptions mo;
    MarkerOptions mo2;
    Marker marker;
    Marker marker2;
    List<Marker> markerList = new ArrayList<>();
    LatLng myCoordinates;
    Device mDevice;
    LatLng myDevice;
    LocationManager locationManager;
    DatabaseReference deviceRef;
    List<Device> userDevices;
    List<MarkerOptions> markerOpArr = new ArrayList<>();
    Circle mCircle;
    PolylineOptions rectOptions = new PolylineOptions();
    Polyline polyline;
    double radiusInMeters;

    @Override
    public void onResume() {
        super.onResume();
                deviceRef.addValueEventListener(listener);
    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Device mDevice = (Device) dataSnapshot.getValue(Device.class);
            if(mDevice != null)
            {
                myDevice = new LatLng(mDevice.history.get(mDevice.history.size()-1).latitude,mDevice.history.get(mDevice.history.size()-1).longitude);
            } else {
                myDevice = new LatLng(marker2.getPosition().latitude ,marker2.getPosition().longitude );
            }
            marker2.setPosition(myDevice);

            if(mDevice.isActivated()){
                rectOptions.add(myDevice);
            }

            polyline = mMap.addPolyline(rectOptions);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public List<Device> MapsActivity() {
        return userDevices = DeviceActivity.userDevices;
    }


    public void init() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsActivity();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //come back later
        mDevice = Parcels.unwrap(getIntent().getBundleExtra("device").getParcelable("device"));
        deviceRef = CurrentSession.getInstance().getDatabaseRef().child("devices").child(mDevice.key);
        //Set the marker Option, lets us know that we have 2 markers. These are
        //The original locations for the markers to start, they will not be used unless validation is not
        //given or if the database is not being accessed
        mo = new MarkerOptions().position(new LatLng(0, 0)).title(getResources().getString(R.string.my_current_location));
        mo2 = new MarkerOptions().position(new LatLng(mDevice.history.get(mDevice.history.size()-1).latitude,
                mDevice.history.get(mDevice.history.size()-1).longitude))
                .title(mDevice.id+ getResources().getString(R.string.device_Select));

        //mo2 = new MarkerOptions().position(new LatLng(0, 0)).title("My Device Location");
        for(int i = 0; i<userDevices.size();i++){
            if(!userDevices.get(i).key.equals(mDevice.key)) {
                markerOpArr.add(new MarkerOptions().position(new LatLng(userDevices.get(i).
                        history.get(userDevices.get(i).history.size()-1).latitude,
                        userDevices.get(i).history.get(userDevices.get(i).history.size()-1).longitude))
                        .title(userDevices.get(i).id));
            }
        }
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);

        if(mDevice.isActivated()) {
            for (int j = 0; j<mDevice.history.size(); j++) {
                rectOptions.add(new LatLng(mDevice.history.get(j).latitude,mDevice.history.get(j).longitude));
            }
        }

        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marker = mMap.addMarker(mo);
        marker2 = mMap.addMarker(mo2);
        polyline = mMap.addPolyline(rectOptions);
        for (int i = 0; i<markerOpArr.size(); i++) {
            markerList.add(mMap.addMarker(new MarkerOptions().
            position(new LatLng(markerOpArr.get(i).getPosition().latitude,markerOpArr.get(i).
                    getPosition().longitude)).icon(BitmapDescriptorFactory.
            defaultMarker(BitmapDescriptorFactory.HUE_CYAN))));
            markerList.get(i);
        }
        if(mDevice.deviceType == DeviceType.STOLEN && mDevice.history.get(0) != null) {
            drawMarkerWithCircle(new LatLng(mDevice.history.get(0).latitude,mDevice.history.get(0).longitude));
        }

        // Set the user radio button true on init.
        radioUserBtn.setChecked(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myCoordinates = new LatLng(location.getLatitude(),location.getLongitude());
        marker.setPosition(myCoordinates);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = getResources().getString(R.string.location_permissions_request_1);
            title = getResources().getString(R.string.enable_location);
            btnText = getResources().getString(R.string.location_settings);
        } else {
            message = getResources().getString(R.string.location_permissions_request_1);
            title = getResources().getString(R.string.permission_access);
            btnText = getResources().getString(R.string.grant);
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        deviceRef.removeEventListener(listener);
    }

    @BindView(R.id.radioUser) RadioButton radioUserBtn;

    @OnClick(R.id.radioUser) void radioUser() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
    }

    @OnClick(R.id.radioDevice) void radioDevice() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myDevice));
    }

    private void drawMarkerWithCircle(LatLng position){
        radiusInMeters = 1000.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);
    }
}
