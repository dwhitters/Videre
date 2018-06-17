package edu.gvsu.cis.videre;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Holds all the information for a device.
 */
@Parcel
public class Device {

    public String id = "";
    public boolean inUse = false;
    public DeviceType deviceType = null;
    String key;
    public LatLng location = null;

    @Override
    public String toString() {
        return deviceType.name();
    }

    public String getId() {
        return id;
    }

    public boolean isInUse() {
        return inUse;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getKey() {
        return key;
    }
}
