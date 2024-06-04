package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MaladeMain extends AppCompatActivity {
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    int doesntSeen=0;
    ImageView buble;
    TextView doesntseeny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malade_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        buble = findViewById(R.id.imageView31);
        doesntseeny = findViewById(R.id.textView77);



        checkNotify();


        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.rdv) {
                startActivity(new Intent(this, Appointment.class));
                finish();
            } else if (itemId == R.id.profil) {
                startActivity(new Intent(this, Profile.class));
                finish();
            }
            return false;

        });
    }

    private void checkNotify() {

        fStore.collection("NewNotify").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String nbr =task.getResult().getString("doesntseen");
                    if (nbr != null){
                        doesntSeen = Integer.parseInt(nbr);
                    }



        fStore.collection("Notify")
                .whereEqualTo("UserUid",fAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                Notification notify = doc.getDocument().toObject(Notification.class);
                                if (notify.getEtas().equals("not yet")) {
                                    NotificationHelper notificationHelper = new NotificationHelper(MaladeMain.this);
                                    notificationHelper.notify(notify.getTitle(), notify.getBody(), R.drawable.bg);
                                    doc.getDocument().getReference().update("etas", "processed");

                                    doesntSeen++;

                                }
                            }

                        }
                        if (doesntSeen != 0){
                            buble.setVisibility(View.VISIBLE);
                            doesntseeny.setVisibility(View.VISIBLE);
                            doesntseeny.setText(Integer.toString(doesntSeen));}
                            Map<String,Object> notifyInfo = new HashMap<>();
                            notifyInfo.put("doesntseen",Integer.toString(doesntSeen));
                            fStore.collection("NewNotify").document(fAuth.getCurrentUser().getUid()).set(notifyInfo);

                    }
                });

                }
            }
        });
    }

    public void bell(View v){
        Intent intent = new Intent(this,BellNotification.class);
        startActivity(intent);
        buble.setVisibility(View.INVISIBLE);
        doesntseeny.setVisibility(View.INVISIBLE);
        doesntSeen=0;
        fStore.collection("NewNotify").document(fAuth.getCurrentUser().getUid()).update("doesntseen","0");
    }public void medicalRecordListView(View v){
        Intent intent = new Intent(this,MedicalRecordListView.class);
        intent.putExtra("natur","user");
        startActivity(intent);
    }public void drugsListView(View v){
        Intent intent = new Intent(this,TritmentPlan.class);
        intent.putExtra("natur","user");
        startActivity(intent);
    }public void ambulance(View v){
        Intent intent = new Intent(this,Ambilance.class);
        intent.putExtra("natur","user");
        startActivity(intent);
    }
    public void seeAllDoctors(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","all");
        startActivity(intent);
    } public void orthopedics(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","orth");
        startActivity(intent);
    }public void oph(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","oph");
        startActivity(intent);
    }public void onco(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","onco");
        startActivity(intent);
    }public void odon(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","odon");
        startActivity(intent);
    }public void gyn(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","gyn");
        startActivity(intent);
    }public void gen(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","gen");
        startActivity(intent);
    }public void cardio(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","cardio");
        startActivity(intent);
    }public void der(View v){
        Intent intent = new Intent(this,DoctorsListView.class);
        intent.putExtra("Speciality","der");
        startActivity(intent);
    }public void seeAllSpecialities(View v){
        Intent intent = new Intent(this,SeeAllSpecialities.class);
        intent.putExtra("Want","Doctors");
        startActivity(intent);
    }public void bloodDonation(View v){
        Intent intent = new Intent(this,BloodDonation.class);
        startActivity(intent);
    }

}