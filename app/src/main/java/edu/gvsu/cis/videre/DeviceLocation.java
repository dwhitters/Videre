package edu.gvsu.cis.videre;

import org.parceler.Parcel;

@Parcel
public class DeviceLocation {
    double latitude;
    double longitude;
    String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
