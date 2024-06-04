package com.example.tabibiapp;

public class Coment {
    String userName,UserUid,DoctorUid,comment,rating;
    public Coment(){}
    public Coment(String userName, String userUid, String doctorUid, String comment, String rating) {
        this.userName = userName;
        UserUid = userUid;
        DoctorUid = doctorUid;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

    public String getDoctorUid() {
        return DoctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        DoctorUid = doctorUid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
