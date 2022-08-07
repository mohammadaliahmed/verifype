package com.appsinventiv.verifype.Models;

import java.io.Serializable;

public class User implements Serializable {
    String name, phone, password;
    String fcmKey;
    String fullFone;
    String city,country,email;

    public User() {
    }

    public User(String name, String phone, String password, String fullFone) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.fullFone = fullFone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullFone() {
        return fullFone;
    }

    public void setFullFone(String fullFone) {
        this.fullFone = fullFone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }
}
