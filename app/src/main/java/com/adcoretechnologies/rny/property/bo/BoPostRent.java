package com.adcoretechnologies.rny.property.bo;

import com.adcoretechnologies.rny.util.Common;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan on 06/09/16.
 */

@IgnoreExtraProperties
public class BoPostRent extends BoPost {
    public String roomCondition;
    public String roomType;
    public String suitableFor;
    public String forWhom;
    public String availability;
    public String vacantDate;
    public String vacantDateLong;
    public String floorNumber;
    public String rent;

    public BoPostRent(String ownerName, String ownerEmail, String ownerContactNumber, String locality, double latitude, double longitude, String city, Date postedOn, List<String> images, int hitCount, String roomCondition, String roomType, String suitableFor, String forWhom, String availability, String vacantDate, String floor, String rent) {
        super(ownerName, ownerEmail, ownerContactNumber, locality, latitude, longitude, city, postedOn.toString(), postedOn.getTime(), images, hitCount);
        this.roomCondition = roomCondition;
        this.roomType = roomType;
        this.suitableFor = suitableFor;
        this.forWhom = forWhom;
        this.availability = availability;
        this.vacantDate = vacantDate;
        this.floorNumber = floor;
        this.rent = rent;
    }

    public BoPostRent() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ownerName", ownerName);
        result.put("ownerEmail", ownerEmail);
        result.put("ownerContactNumber", ownerContactNumber);
        result.put("locality", locality);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("city", city);
        result.put("postedOnLong", postedOnLong);
        result.put("postedOn", postedOn);
        result.put("roomCondition", roomCondition);
        result.put("roomType", roomType);
        result.put("suitableFor", suitableFor);
        result.put("availability", availability);
        result.put("vacantDate", vacantDate);
        result.put("vacantDateLong", vacantDateLong);
        result.put("forWhom", forWhom);
        result.put("floorNumber", floorNumber);
        result.put("rent", rent);
        result.put("postType", "rent");
        result.put("heroImageUrl", heroImageUrl);
        result.put("images", images);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("hitCount", hitCount);
        result.put("hits", hits);
        result.put("postedById", Common.getUid());
        return result;
    }
}
