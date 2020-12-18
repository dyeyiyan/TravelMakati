package com.thatchosenone.travelmakati.models;

public class LeisurePhotosModel {

    String caption, date_time, dp, leisure_photo, luid, name, user_uid;

    public LeisurePhotosModel() {
    }

    public LeisurePhotosModel(String caption, String date_time, String dp, String leisure_photo, String luid, String name, String user_uid) {
        this.caption = caption;
        this.date_time = date_time;
        this.dp = dp;
        this.leisure_photo = leisure_photo;
        this.luid = luid;
        this.name = name;
        this.user_uid = user_uid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getLeisure_photo() {
        return leisure_photo;
    }

    public void setLeisure_photo(String leisure_photo) {
        this.leisure_photo = leisure_photo;
    }

    public String getLuid() {
        return luid;
    }

    public void setLuid(String luid) {
        this.luid = luid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
