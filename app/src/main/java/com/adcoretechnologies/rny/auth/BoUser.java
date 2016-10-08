package com.adcoretechnologies.rny.auth;

import com.adcoretechnologies.rny.util.Common;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

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

    public BoUser() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public long getDateCreatedLong() {
        return dateCreatedLong;
    }

    public void setDateCreatedLong(long dateCreatedLong) {
        this.dateCreatedLong = dateCreatedLong;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
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
    public String profilePicUrl;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("locality", locality);
        result.put("gender", gender);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("imei", imei);
        result.put("contactNumber", contactNumber);
        result.put("profilePicUrl", profilePicUrl);
        result.put("updatedOn", Common.getTimestampString());
        result.put("updatedOnLong", Common.getTimestampLong());
        return result;
    }
}
