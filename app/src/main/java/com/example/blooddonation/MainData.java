package com.example.blooddonation;

import android.graphics.Bitmap;
import android.net.Uri;

public class MainData {

    String Name;
    String BloodGroup;
    Bitmap bitmap1;
    String UseridD;

    public MainData(String name, String bloodGroup, Bitmap bitmap,String Userid) {
        Name = name;
        BloodGroup = bloodGroup;
        bitmap1 = bitmap;
        UseridD = Userid;
    }

    public String getUserid() {
        return UseridD;
    }

    public void setUserid(String userid) {
        UseridD = userid;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }
}
