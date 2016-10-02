package com.adcoretechnologies.rny.auth;

import com.adcoretechnologies.rny.util.Common;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Irfan on 30/09/16.
 */

@IgnoreExtraProperties
public class BoUser {
    public BoUser(String name, String gender, String email, String contactNumber, String locality, double latitude, double longitude, String imei) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imei = imei;
        this.dateCreated = Common.getTimestampString();
        this.dateCreatedLong = Common.getTimestampLong();
    }

    public String userId;
    public String name;
    public String email;
    public String gender;
    public String locality;
    public double latitude;
    public double longitude;
    public String imei;
    public String contactNumber;
    public long dateCreatedLong;
    public String dateCreated;
}
