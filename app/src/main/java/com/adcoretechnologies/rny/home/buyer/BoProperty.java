package com.adcoretechnologies.rny.home.buyer;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Irfan on 01/10/16.
 */

public class BoProperty implements Serializable {

    private String defaultText = "Not Available";

    public boolean isRented() {
        if (postType.toLowerCase().equals("rent"))
            return true;
        else return false;
    }

    public String getRoomCondition() {
        return TextUtils.isEmpty(roomCondition) ? defaultText : roomCondition;
    }

    public String getRoomType() {
        return TextUtils.isEmpty(roomType) ? defaultText : roomType;
    }

    public String getSuitableFor() {
        return TextUtils.isEmpty(suitableFor) ? defaultText : suitableFor;
    }

    public String getForWhom() {
        return TextUtils.isEmpty(forWhom) ? defaultText : forWhom;
    }

    public String getAvailability() {
        try {
            if (availability.equals("About to vacant")) {
                if (TextUtils.isEmpty(vacantDate))
                    return defaultText;
                else
                    return vacantDate;
            } else
                return TextUtils.isEmpty(availability) ? defaultText : availability;
        } catch (Exception ex) {
            return defaultText;
        }
    }

    public String getVacantDate() {
        return vacantDate;
    }

    public long getVacantDateLong() {
        return vacantDateLong;
    }

    public String getFloorNumber() {
        return TextUtils.isEmpty(floorNumber) ? defaultText : floorNumber;
    }

    public String getRent() {
        return TextUtils.isEmpty(rent) ? defaultText : rent + " INR/month";
    }

    public String getHouseCondition() {
        return TextUtils.isEmpty(houseCondition) ? defaultText : houseCondition;
    }

    public String getHouseType() {
        return TextUtils.isEmpty(houseType) ? defaultText : houseType;
    }

    public String getPropertyType() {
        return TextUtils.isEmpty(propertyType) ? defaultText : propertyType;
    }

    public String getDescription() {
        return TextUtils.isEmpty(description) ? defaultText : description;
    }

    public String getArea() {
        return TextUtils.isEmpty(area) ? defaultText : area;
    }

    public String getSellingPrice() {
        return TextUtils.isEmpty(sellingPrice) ? defaultText : sellingPrice + " INR";

    }


    public boolean isNegotiable() {
        return isNegotiable;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getOwnerName() {
        return TextUtils.isEmpty(ownerName) ? defaultText : ownerName;
    }

    public String getOwnerEmail() {
        return TextUtils.isEmpty(ownerEmail) ? defaultText : ownerEmail;
    }

    public String getOwnerContactNumber() {
        return TextUtils.isEmpty(ownerContactNumber) ? defaultText : ownerContactNumber;
    }

    public String getLocality() {
        return TextUtils.isEmpty(locality) ? defaultText : locality;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return TextUtils.isEmpty(city) ? defaultText : city;
    }

    public String getPostedOn() {
        return TextUtils.isEmpty(postedOn) ? defaultText : postedOn;
    }

    public long getPostedOnLong() {
        return postedOnLong;
    }

    public List<String> getImages() {
        return images;
    }

    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    public String roomCondition;
    public String roomType;
    public String suitableFor;
    public String forWhom;
    public String availability;
    public String vacantDate;
    public long vacantDateLong;
    public String floorNumber;
    public String rent;
    public String houseCondition;
    public String houseType;
    public String propertyType;
    public String description;
    public String area;
    public String sellingPrice;
    public String postType;
    public boolean isNegotiable;
    public String propertyId;
    public String ownerName;
    public String ownerEmail;
    public String ownerContactNumber;
    public String locality;
    public double latitude;
    public double longitude;
    public String city;
    public String postedOn;

    public String getPostedById() {
        return postedById;
    }

    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    public String postedById;
    public long postedOnLong;
    public List<String> images;
    public String heroImageUrl;

    public void setRoomCondition(String roomCondition) {
        this.roomCondition = roomCondition;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setSuitableFor(String suitableFor) {
        this.suitableFor = suitableFor;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setVacantDate(String vacantDate) {
        this.vacantDate = vacantDate;
    }

    public void setVacantDateLong(long vacantDateLong) {
        this.vacantDateLong = vacantDateLong;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public void setHouseCondition(String houseCondition) {
        this.houseCondition = houseCondition;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setNegotiable(boolean negotiable) {
        isNegotiable = negotiable;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public void setOwnerContactNumber(String ownerContactNumber) {
        this.ownerContactNumber = ownerContactNumber;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public void setPostedOnLong(long postedOnLong) {
        this.postedOnLong = postedOnLong;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setHeroImageUrl(String heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }

}
