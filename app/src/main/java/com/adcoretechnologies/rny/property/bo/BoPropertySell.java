package com.adcoretechnologies.rny.property.bo;

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
public class BoPropertySell extends BoProperty {
    public String houseCondition;
    public String houseType;
    public String propertyType;
    public String description;
    public String area;
    public String floorNumber;
    public String sellingPrice;
    public boolean isNegotiable;

    public BoPropertySell(String ownerName, String ownerEmail, String ownerContactNumber, String locality, double latitude, double longitude, String city, Date postedOn, List<String> images, int hitCount, String houseCondition, String houseType, String propertyType, String description, String area, String floorNumber, String sellingPrice, boolean isNegotiable) {
        super(ownerName, ownerEmail, ownerContactNumber, locality, latitude, longitude, city, postedOn.toString(), postedOn.getTime(), images, hitCount);
        this.houseCondition = houseCondition;
        this.houseType = houseType;
        this.propertyType = propertyType;
        this.description = description;
        this.area = area;
        this.floorNumber = floorNumber;
        this.sellingPrice = sellingPrice;
        this.isNegotiable = isNegotiable;
    }

    public BoPropertySell() {
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
        result.put("houseCondition", houseCondition);
        result.put("houseType", houseType);
        result.put("propertyType", propertyType);
        result.put("description", description);
        result.put("area", area);
        result.put("floorNumber", floorNumber);
        result.put("sellingPrice", sellingPrice);
        result.put("isNegotiable", isNegotiable);
        result.put("postType", "sell");
        result.put("heroImageUrl", heroImageUrl);
        result.put("images", images);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("hitCount", hitCount);
        result.put("hits", hits);
        return result;
    }
}
