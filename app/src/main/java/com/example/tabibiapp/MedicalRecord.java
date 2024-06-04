package com.example.tabibiapp;

public class MedicalRecord {
    String Diagnosis,DoctorUid,TritmentPlan,UserUid,DoctorFullName,DoctorSpeciality;
    public MedicalRecord(){}
    public MedicalRecord(String diagnosis, String doctorUid, String tritmentPlan, String userUid, String doctorFullName, String doctorSpeciality) {
        Diagnosis = diagnosis;
        DoctorUid = doctorUid;
        TritmentPlan = tritmentPlan;
        UserUid = userUid;
        DoctorSpeciality = doctorSpeciality;
        DoctorFullName = doctorFullName;
    }

    public String getDoctorFullName() {
        return DoctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        DoctorFullName = doctorFullName;
    }

    public String getDoctorSpeciality() {
        return DoctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        DoctorSpeciality = doctorSpeciality;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getDoctorUid() {
        return DoctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        DoctorUid = doctorUid;
    }

    public String getTritmentPlan() {
        return TritmentPlan;
    }

    public void setTritmentPlan(String tritmentPlan) {
        TritmentPlan = tritmentPlan;
    }
}
