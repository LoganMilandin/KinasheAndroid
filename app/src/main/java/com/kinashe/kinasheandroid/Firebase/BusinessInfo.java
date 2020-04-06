package com.kinashe.kinasheandroid.Firebase;

import java.io.Serializable;
import java.util.List;

/**
 * represents a single company's information from the database. Database
 * getters return instances of this class
 */
public class BusinessInfo implements Serializable {

    private String businessType;
    private String companyName;
    private String description;
    private String descriptionTrans;
    private String website;
    private String phone;
    private String lat;
    private String lon;
    private int monthlyPayment;
    private boolean verified;

    //attribute not from database, used to order businesses instead once
    //user location is calculated
    private double distance;


    private List<List<String>> hours;
    //null if there are no photos
    private List<String> photos;
    //null if there are no coupons
    private List<Coupon> coupons;

    //default empty constructor required by Firebase
    private BusinessInfo() {}

    //getters for fields, required by firebase
    public String getBusinessType() {
        return businessType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionTrans() {
        return descriptionTrans;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public int getMonthlyPayment() {
        return monthlyPayment;
    }

    public List<List<String>> getHours() {
        return hours;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public boolean isVerified() {
        return verified;
    }

    //getters and setters for custom distance field
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    //this is only to help with debugging
    public String toString() {
        return "\ntype: " + businessType + "\nname: " + companyName + "\ndescription: "
                + description + "\ndescriptionTrans: " + descriptionTrans
                + "\nphone: " + phone + "\nlocation:" + lat + ", " + lon
                + "\nmonthlyPayment" + monthlyPayment + "\nhours: " + hours
                + "\nphotos: " + photos + "\ncoupons: " + coupons;
    }

}
