package edu.gvsu.cis.videre;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Holds all the information for a device.
 */
@Parcel
public class Device {
    public final String id;
    public final boolean inUse;
    public final DeviceType deviceType;
    public final LatLng location;

    @ParcelConstructor
    public Device(String id, boolean inUse, DeviceType deviceType, LatLng location) {
        this.id = id;
        this.inUse = inUse;
        this.deviceType = deviceType;
        this.location = location;
    }

    @Override
    public String toString() {
        return deviceType.name();
    }
}
