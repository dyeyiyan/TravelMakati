package com.thatchosenone.travelmakati.models;
public class ReviewModel {

    String comment, date_time, luid, rate_value, user_uid, isDeleted;


    public ReviewModel() {
    }

    public ReviewModel(String comment, String date_time, String luid, String rate_value, String user_uid, String isDeleted) {
        this.comment = comment;
        this.date_time = date_time;
        this.luid = luid;
        this.rate_value = rate_value;
        this.user_uid = user_uid;
        this.isDeleted = isDeleted;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getLuid() {
        return luid;
    }

    public void setLuid(String luid) {
        this.luid = luid;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}






