package com.example.tabibiapp;

public class DoctorRequest {
    String Name,FullName,RequesterUid,Msg;

    public DoctorRequest() {}

    public DoctorRequest(String fullName, String name, String requesterUid, String msg) {
        FullName = fullName;
        RequesterUid = requesterUid;
        Msg = msg;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getRequesterUid() {
        return RequesterUid;
    }

    public void setRequesterUid(String requesterUid) {
        RequesterUid = requesterUid;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
