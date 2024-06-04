package com.example.tabibiapp;

public class BloodRequest {
    String blood,DoctorUid,Wilaya,Adress,Hospital,Need,phone;
    public BloodRequest() {}

    public BloodRequest(String blood, String doctorUid, String wilaya, String adress, String hospital, String need, String phone) {
        this.blood = blood;
        DoctorUid = doctorUid;
        Wilaya = wilaya;
        Adress = adress;
        Hospital = hospital;
        Need = need;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getDoctorUid() {
        return DoctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        DoctorUid = doctorUid;
    }

    public String getWilaya() {
        return Wilaya;
    }

    public void setWilaya(String wilaya) {
        Wilaya = wilaya;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public String getNeed() {
        return Need;
    }

    public void setNeed(String need) {
        Need = need;
    }
}
