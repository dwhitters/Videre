package edu.gvsu.cis.videre;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * Holds all the information for a device.
 */
@Parcel
public class Device {

    public String id = "";
    public boolean inUse = false;
    public DeviceType deviceType = null;
    String key;
    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return deviceType.name();
    }

    // Overriding equals() to compare two Device objects
    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Device or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Device)) {
            return false;
        }

        // typecast o to Device so that we can compare data members
        Device d = (Device) o;

        // Compare the data members and return accordingly
        return key.equals(d.key);
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
