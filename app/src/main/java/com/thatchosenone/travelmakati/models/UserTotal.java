package com.thatchosenone.travelmakati.models;

public class UserTotal {
    String leisureID, totalViews, price, uID;

    public UserTotal() {
    }

    public UserTotal(String leisureID, String totalViews, String price, String uID) {
        this.leisureID = leisureID;
        this.totalViews = totalViews;
        this.price = price;
        this.uID = uID;
    }

    public String getLeisureID() {
        return leisureID;
    }

    public void setLeisureID(String leisureID) {
        this.leisureID = leisureID;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
