package com.thatchosenone.travelmakati.models;

public class LeisureModel {
    String name, address, leisureID, leisurePhoto, category, description, totalRate, totalViews, publish, price, in;

    public LeisureModel() {
    }

    public LeisureModel(String name, String address, String leisureID, String leisurePhoto, String category, String description, String totalRate, String totalViews, String publish, String price, String in) {
        this.name = name;
        this.address = address;
        this.leisureID = leisureID;
        this.leisurePhoto = leisurePhoto;
        this.category = category;
        this.description = description;
        this.totalRate = totalRate;
        this.totalViews = totalViews;
        this.publish = publish;
        this.price = price;
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLeisureID() {
        return leisureID;
    }

    public void setLeisureID(String leisureID) {
        this.leisureID = leisureID;
    }

    public String getLeisurePhoto() {
        return leisurePhoto;
    }

    public void setLeisurePhoto(String leisurePhoto) {
        this.leisurePhoto = leisurePhoto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }
}
