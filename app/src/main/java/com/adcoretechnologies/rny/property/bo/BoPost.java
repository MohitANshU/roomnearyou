package com.adcoretechnologies.rny.property.bo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan on 06/09/16.
 */

@IgnoreExtraProperties
public class BoPost {
    public String propertyId;
    public String ownerName;
    public String ownerEmail;
    public String ownerContactNumber;
    public String locality;
    public String heroImageUrl;
    public double latitude;
    public double longitude;
    public String city;
    public String postedOn;
    public long postedOnLong;
    public List<String> images;
    public int starCount = 0;
    public int hitCount = 0;
    public Map<String, String> stars = new HashMap<>();
    public Map<String, Boolean> hits = new HashMap<>();


    public BoPost(String ownerName, String ownerEmail, String ownerContactNumber, String locality, double latitude, double longitude, String city, String postedOn, long postedOnLong, List<String> images, int hitCount) {
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerContactNumber = ownerContactNumber;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.postedOn = postedOn;
        this.postedOnLong = postedOnLong;
        this.images = images;
        this.hitCount = hitCount;
        if (images != null && images.size() > 0) {
            this.heroImageUrl = images.get(0);
        }
    }

    public BoPost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("itemName", itemName);
//        result.put("email", email);
//        result.put("description", description);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//        result.put("country", country);
//        result.put("address", address);
//        result.put("lostOn", lostOn);
//        result.put("postedOn", postedOn);
//        result.put("postedOnLong", postedOnLong);
//        result.put("postImages", postImages);
//        return result;
//    }
}
