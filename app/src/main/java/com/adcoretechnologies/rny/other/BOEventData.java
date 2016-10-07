package com.adcoretechnologies.rny.other;

/**
 * Created by Irfan on 07/10/15.
 */
public class BOEventData {
    public static final int EVENT_POST_IMAGE_UPLOAD = 1;
    public static final int EVENT_POST_IMAGE_UPLOAD_REMOVE = 2;
    public static final int EVENT_POST_IMAGE_UPLOAD_PROGRESS = 3;
    public static final int EVENT_POST_DETAIL = 4;
    public static final int EVENT_POST_EDIT = 5;
    public static final int EVENT_PROPERTY_SAVED = 6;
    public static final int EVENT_INFO_CLICK = 7;
    public static final int EVENT_INFO_CLICK_CALL = 8;
    public static final int EVENT_INFO_CLICK_DIRECTION = 9;
    public static final int EVENT_INFO_CLICK_WISHLIST = 10;

    public final int eventType;

    public int getId() {
        return Id;
    }

    public String getData() {
        return data;
    }

    public Object getObject() {
        return object;
    }

    public int Id;
    public String data;
    public Object object;

    public BOEventData(int eventType, int Id) {
        this.eventType = eventType;
        this.Id = Id;
    }

    public BOEventData(int eventType, int Id, String data) {
        this.eventType = eventType;
        this.data = data;
        this.Id = Id;
    }

    public BOEventData(int eventType, int Id, String data, Object object) {
        this.eventType = eventType;
        this.Id = Id;
        this.data = data;
        this.object = object;
    }

}
