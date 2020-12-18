package com.thatchosenone.travelmakati.models;

public class AccountData {
    private String email, firstname, lastname, city, password, group;

    public AccountData() {

    }

    public AccountData(String email, String firstname, String lastname, String city, String password, String group) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.city = city;
        this.password = password;
        this.group = group;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setText(String s) {
    }
}
