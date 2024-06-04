package com.example.tabibiapp;

import android.net.Uri;

public class Notification {
    String title,body,DoctorUid,UserUid,time,etas;

    public Notification() {}

    public Notification(String title, String body, String doctorUid, String userUid, String time, String etas) {
        this.title = title;
        this.body = body;
        DoctorUid = doctorUid;
        UserUid = userUid;
        this.time = time;
        this.etas = etas;
    }

    public String getEtas() {
        return etas;
    }

    public void setEtas(String etas) {
        this.etas = etas;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDoctorUid() {
        return DoctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        DoctorUid = doctorUid;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }
}
