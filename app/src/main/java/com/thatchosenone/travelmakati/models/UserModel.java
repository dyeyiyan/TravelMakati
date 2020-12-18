package com.thatchosenone.travelmakati.models;

public class UserModel {

    String name, email, cover_photo, display_photo, city, uid, onlineStatus, typingTo;


    public UserModel() {
    }

    public UserModel(String name, String email, String cover_photo, String display_photo, String city, String uid, String onlineStatus, String typingTo) {
        this.name = name;
        this.email = email;
        this.cover_photo = cover_photo;
        this.display_photo = display_photo;
        this.city = city;
        this.uid = uid;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getDisplay_photo() {
        return display_photo;
    }

    public void setDisplay_photo(String display_photo) {
        this.display_photo = display_photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }
}
