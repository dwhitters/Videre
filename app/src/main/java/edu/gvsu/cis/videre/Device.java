package edu.gvsu.cis.videre;

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

    @ParcelConstructor
    public Device(String id, boolean inUse, DeviceType deviceType) {
        this.id = id;
        this.inUse = inUse;
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return deviceType.name();
    }
}
