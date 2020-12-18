package com.thatchosenone.travelmakati.models;

public class ChatListModel {

    String id; // we will need this id to get chatlist , sender/receiver uid

    public ChatListModel() {
    }

    public ChatListModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
