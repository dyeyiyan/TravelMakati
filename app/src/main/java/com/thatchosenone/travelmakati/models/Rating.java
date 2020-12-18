package com.thatchosenone.travelmakati.models;

public class Rating {
    private Double RateValue;
    private String Comment;
    private String DatePosted;

    public Rating(){
        
    }

    public Rating(Double rateValue, String comment, String datePosted) {
        this.RateValue = rateValue;
        this.Comment = comment;
        DatePosted = datePosted;

    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public Double getRateValue() {
        return RateValue;
    }

    public void setRateValue(Double rateValue) {
        RateValue = rateValue;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
