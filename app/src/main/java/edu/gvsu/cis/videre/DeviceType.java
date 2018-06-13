package edu.gvsu.cis.videre;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel
public enum DeviceType {
    MOVE(0),
    STOLEN(1),
    WATCH(2);

    @ParcelProperty("val")
    int val;
    @ParcelConstructor
    DeviceType(int val) {
        this.val = val;
    }
    int getVal() {
        return val;
    }
}