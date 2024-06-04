package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SeeAllSpecialities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_specialities);

    }
    public void orthopedicss(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","orth");
        startActivity(intent);
    }public void ophs(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","oph");
        startActivity(intent);
    }public void oncos(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","onco");
        startActivity(intent);
    }
    public void odons(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","odon");
        startActivity(intent);
    }public void gyns(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","gyn");
        startActivity(intent);
    }
    public void gens(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","gen");
        startActivity(intent);
    }public void cardios(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","cardio");
        startActivity(intent);
    }
    public void ders(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","der");
        startActivity(intent);
}}