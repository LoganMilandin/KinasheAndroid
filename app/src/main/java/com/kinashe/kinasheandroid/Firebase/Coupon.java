package com.kinashe.kinasheandroid.Firebase;

import java.io.Serializable;

/**
 * represents a single coupon for a given business. A BusinessInfo object contains
 * a list of coupons
 */
public class Coupon implements Serializable {
    private String availability;
    private String availabilityTrans;
    private String deal;
    private String dealTrans;
    private String title;
    private String titleTrans;
    private String expiration;
    private boolean active;
    //gives Unix time in milliseconds. Need to check against current
    //date to decide whether to render
    private long expTimestamp;

    private Coupon() {}

    public String getAvailability() {
        return availability;
    }

    public String getAvailabilityTrans() {
        return availabilityTrans;
    }

    public String getDeal() {
        return deal;
    }

    public String getDealTrans() {
        return dealTrans;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleTrans() {
        return titleTrans;
    }

    public String getExpiration() {
        return expiration;
    }

    public long getExpTimestamp() {
        return expTimestamp;
    }

    public boolean isActive() {
        return active;
    }

    //only for debugging
    public String toString() {
        return "\n\ttitle: " + title + "\n\ttitleTrans" + titleTrans + "\n\tavailability: "
                + availability + "\n\tavailabilityTrans: " + availabilityTrans
                + "\n\tdeal: " + deal + "dealTrans: " + dealTrans + "\n\texpTimestamp: "
                + expTimestamp + "\n\texpiration: " + expiration + "\n";
    }
}
