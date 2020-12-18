package com.thatchosenone.travelmakati.models;

public class AveRateModel {
    String leisureID, totalRate;

    public AveRateModel() {
    }

    public AveRateModel(String leisureID, String totalRate) {
        this.leisureID = leisureID;
        this.totalRate = totalRate;
    }

    public String getLeisureID() {
        return leisureID;
    }

    public void setLeisureID(String leisureID) {
        this.leisureID = leisureID;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }
}


