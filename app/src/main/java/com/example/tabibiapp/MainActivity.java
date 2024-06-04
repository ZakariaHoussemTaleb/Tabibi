package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void signOut(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }
    public void getStarted(View v){
        Intent intent = new Intent(this,MaladeMain.class);
        startActivity(intent);
        finish();
    }
    public void next(View v){
        setContentView(R.layout.first_view);
    }
    public void next2(View v) {
        setContentView(R.layout.first_view3);
    }


}