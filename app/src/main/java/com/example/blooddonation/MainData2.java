package com.example.blooddonation;

import android.graphics.Bitmap;

public class MainData2 {

    String name,place,bloodGroup,Forwohom,uid;
    Bitmap bitmap;

    public MainData2(String name, String place,  String forwohom, String bloodGroup,Bitmap bitmap,String uid) {
        this.name = name;
        this.place = place;
        Forwohom = forwohom;
        this.bloodGroup = bloodGroup;
        this.bitmap = bitmap;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getForwohom() {
        return Forwohom;
    }

    public void setForwohom(String forwohom) {
        Forwohom = forwohom;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
