package com.thatchosenone.travelmakati.models;

public class CategoriesModel {

    private String categName;
    private  String categPhoto;

    public CategoriesModel() {
    }

    public CategoriesModel(String categName, String categPhoto) {
        this.categName = categName;
        this.categPhoto = categPhoto;
    }

    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    }

    public String getCategPhoto() {
        return categPhoto;
    }

    public void setCategPhoto(String categPhoto) {
        this.categPhoto = categPhoto;
    }
}
