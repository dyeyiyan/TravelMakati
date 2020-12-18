package com.thatchosenone.travelmakati.models;

public class RatePostModel {
    //use same  name as  we given while uploading post
    String uid, uName, uEmail, pLikes, uDp, pID, pTitle, pDescription, pImage, pTime, pLeisureName;
    Double pRatings;


    public RatePostModel() {
    }

    public RatePostModel(String uid, String uName, String uEmail, String pLikes, String uDp, String pID, String pTitle, String pDescription, String pImage, String pTime, String pLeisureName, Double pRatings) {
        this.uid = uid;
        this.uName = uName;
        this.uEmail = uEmail;
        this.pLikes = pLikes;
        this.uDp = uDp;
        this.pID = pID;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pImage = pImage;
        this.pTime = pTime;
        this.pLeisureName = pLeisureName;
        this.pRatings = pRatings;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpLeisureName() {
        return pLeisureName;
    }

    public void setpLeisureName(String pLeisureName) {
        this.pLeisureName = pLeisureName;
    }

    public Double getpRatings() {
        return pRatings;
    }

    public void setpRatings(Double pRatings) {
        this.pRatings = pRatings;
    }
}
