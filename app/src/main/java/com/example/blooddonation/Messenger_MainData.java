package com.example.blooddonation;

import android.graphics.Bitmap;

public class Messenger_MainData  {
    String muserid;
    boolean bool;
    String email;
    String gender;
    String group;
    String name;
    String picturelink;

    public Messenger_MainData(String muserid, boolean bool, String email, String gender, String group, String name, String picturelink) {
        this.muserid = muserid;
        this.bool = bool;
        this.email = email;
        this.gender = gender;
        this.group = group;
        this.name = name;
        this.picturelink = picturelink;
    }

    public Messenger_MainData() {
    }

    public String getMuserid() {
        return muserid;
    }

    public void setMuserid(String muserid) {
        this.muserid = muserid;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturelink() {
        return picturelink;
    }

    public void setPicturelink(String picturelink) {
        this.picturelink = picturelink;
    }
}


