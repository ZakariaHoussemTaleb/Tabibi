package com.example.tabibiapp;

public class Doctor{
    String Name, FamillyName, Speciality, Phone, TimeStartWorking, TimeFinishWorking, Wilaya, Adress,Uid,MaxAtDay;
    double rating;
    boolean Freefriday,Freethursday,Freewednesday,Freemonday,Freesunday,Freesaturday,Freetuesday;
    public Doctor(){}

    public Doctor(String name, String famillyName, String speciality, String phone, String timeStartWorking, String timeFinishWorking, String wilaya, String adress, String uid, double rating, String maxAtDay, boolean freefriday, boolean freethursday, boolean freewednesday, boolean freemonday, boolean freesunday, boolean freesaturday, boolean freetuesday) {
        Name = name;
        FamillyName = famillyName;
        Speciality = speciality;
        Phone = phone;
        TimeStartWorking = timeStartWorking;
        TimeFinishWorking = timeFinishWorking;
        Wilaya = wilaya;
        Adress = adress;
        Uid = uid;
        this.rating = rating;
        MaxAtDay = maxAtDay;
        Freefriday = freefriday;
        Freethursday = freethursday;
        Freewednesday = freewednesday;
        Freemonday = freemonday;
        Freesunday = freesunday;
        Freesaturday = freesaturday;
        Freetuesday = freetuesday;
    }

    public String getMaxAtDay() {
        return MaxAtDay;
    }

    public void setMaxAtDay(String maxAtDay) {
        MaxAtDay = maxAtDay;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public boolean isFreefriday() {
        return Freefriday;
    }

    public void setFreefriday(boolean freefriday) {
        Freefriday = freefriday;
    }

    public boolean isFreethursday() {
        return Freethursday;
    }

    public void setFreethursday(boolean freethursday) {
        Freethursday = freethursday;
    }

    public boolean isFreewednesday() {
        return Freewednesday;
    }

    public void setFreewednesday(boolean freewednesday) {
        Freewednesday = freewednesday;
    }

    public boolean isFreemonday() {
        return Freemonday;
    }

    public void setFreemonday(boolean freemonday) {
        Freemonday = freemonday;
    }

    public boolean isFreesunday() {
        return Freesunday;
    }

    public void setFreesunday(boolean freesunday) {
        Freesunday = freesunday;
    }

    public boolean isFreesaturday() {
        return Freesaturday;
    }

    public void setFreesaturday(boolean freesaturday) {
        Freesaturday = freesaturday;
    }

    public boolean isFreetuesday() {
        return Freetuesday;
    }

    public void setFreetuesday(boolean freetuesday) {
        Freetuesday = freetuesday;
    }

    public String getName() {
        return Name;
    }

    public String getUid() {
        return Uid;
    }

    public String getFamillyName() {
        return FamillyName;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public String getPhone() {
        return Phone;
    }


    public String getTimeStartWorking() {
        return TimeStartWorking;
    }

    public String getTimeFinishWorking() {
        return TimeFinishWorking;
    }


    public String getWilaya() {
        return Wilaya;
    }

    public String getAdress() {
        return Adress;
    }

    public double getRating() {
        return 0;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setFamillyName(String famillyName) {
        FamillyName = famillyName;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setTimeStartWorking(String timeStartWorking) {
        TimeStartWorking = timeStartWorking;
    }

    public void setTimeFinishWorking(String timeFinishWorking) {
        TimeFinishWorking = timeFinishWorking;
    }


    public void setWilaya(String wilaya) {
        Wilaya = wilaya;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}