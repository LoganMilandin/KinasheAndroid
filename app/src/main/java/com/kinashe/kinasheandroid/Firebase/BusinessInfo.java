package com.kinashe.kinasheandroid.Firebase;

import java.util.List;

/**
 * represents a single company's information from the database. Database
 * getters return objects of this class
 */
public class BusinessInfo {
    private String businessType;
    private String companyName;
    private String description;
    private String descriptionTrans;

    private String phone;
    private String lat;
    private String lon;
    private int monthlyPayment;


    private List<List<String>> hours;
    //null if there are no photos
    private List<String> photos;
    //null if there are no coupons
    private List<Coupon> coupons;

    //default empty constructor required by Firebase
    private BusinessInfo() {
    }

    //getters for string fields (some may not be used)
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

    public String getPhone() {
        return phone;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    //getter for payment amount, used for sorting
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

    public String toString() {
        return "\ntype: " + businessType + "\nname: " + companyName + "\ndescription: "
                + description + "\ndescriptionTrans: " + descriptionTrans
                + "\nphone: " + phone + "\nlocation:" + lat + ", " + lon
                + "\nmonthlyPayment" + monthlyPayment + "\nhours: " + hours
                + "\nphotos: " + photos + "\ncoupons: " + coupons;
    }
}