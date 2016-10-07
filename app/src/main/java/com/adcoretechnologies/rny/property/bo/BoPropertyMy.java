package com.adcoretechnologies.rny.property.bo;

import java.util.List;

/**
 * Created by Irfan on 01/10/16.
 */

public class BoPropertyMy{

    public boolean isRented() {
        if (postType.toLowerCase().equals("rent"))
            return true;
        else return false;
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
    public long postedOnLong;
    public List<String> images;
    public String heroImageUrl;

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int starCount = 0;
    public int hitCount = 0;

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
