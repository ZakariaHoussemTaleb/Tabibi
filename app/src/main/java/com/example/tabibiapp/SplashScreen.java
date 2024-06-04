package com.example.tabibiapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth lAuth;
    private FirebaseFirestore fstore;
    BrodcastReciever brodcastReciever = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        brodcastReciever = new BrodcastReciever();
        Log.w(TAG, "admin splash");
        InternetStatus();

        lAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        /*if (lAuth.getCurrentUser() != null){
            updateUI(lAuth.getCurrentUser());
        }else {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }*/

    }
    public static String checkInternet(Context context){
        String status=null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkInfo = connectivityManager.getNetworkCapabilities(activeNetwork);



        if (networkInfo!=null){
            status="connected";
            return status;
        } else{
            status="disconnected";
            return status;
        }
    }
    public void updateUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        if (user!=null){
            String uid = user.getUid();
            DocumentReference df = fstore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isDoctor")!=null){
                        Intent intent = new Intent(SplashScreen.this,DoctorMain.class);
                        startActivity(intent);
                        finish();
                    } else if (documentSnapshot.getString("isAdmin")!=null) {
                        Intent intent = new Intent(SplashScreen.this,Admin.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SplashScreen.this,MaladeMain.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }else {
            Intent intent = new Intent(SplashScreen.this,Login.class);
            startActivity(intent);
            finish();
        }
    }
    public void InternetStatus(){
        registerReceiver(brodcastReciever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(brodcastReciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (brodcastReciever != null) {
            unregisterReceiver(brodcastReciever);
        }
    }
}