package com.thatchosenone.travelmakati.models;

public class Reviews {
    private String image, firstname, lastname, date, ratings, comment;

    public Reviews() {

    }

    public Reviews(String image, String firstname, String lastname, String date, String ratings, String comment) {
        this.image = image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.ratings = ratings;
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
            this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
