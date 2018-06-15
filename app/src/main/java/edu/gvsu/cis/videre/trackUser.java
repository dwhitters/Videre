package edu.gvsu.cis.videre;

import java.util.ArrayList;
import org.parceler.Parcel;

public class trackUser {
    String _key;
    String emailStr;
    String passStr;
    ArrayList<Device> userDevices = new ArrayList<Device>();

    public String getEmailStr() { return emailStr;}
    public String getPassStr() { return passStr;}
    public String getKey() { return _key;}

    public ArrayList<Device> getUserDevices() {
        return userDevices;
    }

    public void setEmailStr(String emailStr){ this.emailStr = emailStr;}
    public void setPassStr(String passStr){this.passStr = passStr;}

    public void setUserDevices(ArrayList<Device> userDevices) {
        this.userDevices = userDevices;
    }
    public void setKey(String _key ) {this._key = _key;}
}
