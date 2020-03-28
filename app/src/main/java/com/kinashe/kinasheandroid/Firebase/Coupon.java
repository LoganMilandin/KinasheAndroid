package com.kinashe.kinasheandroid.Firebase;

/**
 * represents a single coupon for a given business. A BusinessInfo object contains
 * a list of coupons
 */
public class Coupon {
    private String availability;
    private String availabilityTrans;
    private String deal;
    private String dealTrans;
    private String title;
    private String titleTrans;
    private String expiration;
    //gives Unix time in milliseconds. Need to check against current
    //date to decide whether to render
    private long expTimestamp;

    public Coupon() {
    }

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


    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setAvailabilityTrans(String availabilityTrans) {
        this.availabilityTrans = availabilityTrans;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public void setDealTrans(String dealTrans) {
        this.dealTrans = dealTrans;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleTrans(String titleTrans) {
        this.titleTrans = titleTrans;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setExpTimestamp(long expTimestamp) {
        this.expTimestamp = expTimestamp;
    }

    public long getExpTimestamp() {
        return expTimestamp;
    }

    public String toString() {
        return "\n\ttitle: " + title + "\n\ttitleTrans" + titleTrans + "\n\tavailability: "
                + availability + "\n\tavailabilityTrans: " + availabilityTrans
                + "\n\tdeal: " + deal + "dealTrans: " + dealTrans + "\n\texpTimestamp: "
                + expTimestamp + "\n\texpiration: " + expiration + "\n";
    }
}
