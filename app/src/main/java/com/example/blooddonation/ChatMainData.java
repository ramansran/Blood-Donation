package com.example.blooddonation;

import android.graphics.Bitmap;

import java.util.Date;

public class ChatMainData {

    private Date date;
    String sender;
    String reciever;
    String message;


    public ChatMainData() {
    }

    public ChatMainData(String sender, String reciever, String message, Bitmap bitmap) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;

    }

    public String getSender()  {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
