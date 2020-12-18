package com.thatchosenone.travelmakati.models;

public class AnnouncementModel {

    String announce, aphoto, lphoto, luid, name, ptime, type, user_uid;

    public AnnouncementModel() {
    }

    public AnnouncementModel(String announce, String aphoto, String lphoto, String luid, String name, String ptime, String type, String user_uid) {
        this.announce = announce;
        this.aphoto = aphoto;
        this.lphoto = lphoto;
        this.luid = luid;
        this.name = name;
        this.ptime = ptime;
        this.type = type;
        this.user_uid = user_uid;
    }


    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getAphoto() {
        return aphoto;
    }

    public void setAphoto(String aphoto) {
        this.aphoto = aphoto;
    }

    public String getLphoto() {
        return lphoto;
    }

    public void setLphoto(String lphoto) {
        this.lphoto = lphoto;
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

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
