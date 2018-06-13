package edu.gvsu.cis.videre;

import org.parceler.Parcel;

@Parcel
public class BluetoothItem {
    String name;
    String address;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        /* Check if o is an instance of BluetoothItem. */
        if (!(o instanceof BluetoothItem)) {
            return false;
        }

        BluetoothItem btItem = (BluetoothItem) o;

        return btItem.address.equals(this.address);
    }
}
