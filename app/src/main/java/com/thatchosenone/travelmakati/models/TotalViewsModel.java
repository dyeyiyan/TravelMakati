package com.thatchosenone.travelmakati.models;

public class TotalViewsModel {
    String leisureID, totalViews, price;

    public TotalViewsModel() {
    }

    public TotalViewsModel(String leisureID, String totalViews, String price) {
        this.leisureID = leisureID;
        this.totalViews = totalViews;
        this.price = price;
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

}
