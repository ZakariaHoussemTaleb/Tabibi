package com.example.tabibiapp;

import com.google.firebase.Timestamp;

public class ListDesRendezVous {
    String DateStart,DateFinish;
    String DoctorUid,UserUid,etat,Description;
    Timestamp DateDepo;
    public ListDesRendezVous(){}
    public ListDesRendezVous(String dateStart, String dateFinish, String doctorUid, String userUid, String etat,String description,Timestamp dateDepo) {
        DateStart = dateStart;
        DateFinish = dateFinish;
        DoctorUid = doctorUid;
        UserUid = userUid;
        Description=description;
        this.etat = etat;
        DateDepo = dateDepo;
    }

    public Timestamp getDateDepo() {
        return DateDepo;
    }

    public void setDateDepo(Timestamp dateDepo) {
        DateDepo = dateDepo;
    }

    public String getDateStart() {
        return DateStart;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDateStart(String dateStart) {
        DateStart = dateStart;
    }

    public String getDateFinish() {
        return DateFinish;
    }

    public void setDateFinish(String dateFinish) {
        DateFinish = dateFinish;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
