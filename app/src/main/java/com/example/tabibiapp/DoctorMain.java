package com.example.tabibiapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DoctorMain extends AppCompatActivity {

    private StorageReference storageReference;
    private ActiveRDVDoctorAdapter adapter;
    private ArrayList<ListDesRendezVous> Prdvs;
    private ArrayList<ListDesRendezVous> Ardvs;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private RdvAdapter rdvAdapter;
    boolean isUpComing = true , isPending = true;
    Button Upcoming,Pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);



        storageReference = FirebaseStorage.getInstance().getReference();

        ImageView imageProfile = findViewById(R.id.imageView81);
        fAuth = FirebaseAuth.getInstance();
        StorageReference profileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfile);
            }
        });

        db = FirebaseFirestore.getInstance();

        String doctorUid = fAuth.getCurrentUser().getUid();

        verifyDoctorsInformation(doctorUid);


        Ardvs = new ArrayList<ListDesRendezVous>();
        Prdvs = new ArrayList<ListDesRendezVous>();

        Upcoming = findViewById(R.id.button9);
        Pending = findViewById(R.id.button8);




        Upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPending){
                    Upcoming.setBackgroundColor(getResources().getColor(R.color.color2));
                    Pending.setBackgroundColor(getResources().getColor(R.color.white));
                    isPending = !isPending;
                }
                activeRDV(doctorUid);
                adapter = new ActiveRDVDoctorAdapter(DoctorMain.this,Ardvs);
                ListView listView = findViewById(R.id.listView3);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                isUpComing = true;


            }
        });
        Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpComing){
                    Pending.setBackgroundColor(getResources().getColor(R.color.color2));
                    Upcoming.setBackgroundColor(getResources().getColor(R.color.white));
                    isUpComing = !isUpComing;
                }
                pendingRDV(doctorUid);
                rdvAdapter = new RdvAdapter(DoctorMain.this, Prdvs);
                ListView listView = findViewById(R.id.listView3);
                listView.setAdapter(rdvAdapter);
                rdvAdapter.notifyDataSetChanged();
                isPending=true;

            }
        });
        Upcoming.performClick();

    }

    private void verifyDoctorsInformation(String doctorUid) {
        Dialog dialog = new Dialog(DoctorMain.this);
        dialog.setContentView(R.layout.custom_dialigue_box_no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(DoctorMain.this, R.drawable.custom_dialogue));
        dialog.setCancelable(false);

        Button dialogueDepose = dialog.findViewById(R.id.exitinternet);
        TextView title = dialog.findViewById(R.id.textView53);
        title.setText("Warning");
        dialogueDepose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(DoctorMain.this, DoctorInfo.class);
                startActivity(intent);
            }
        });

        TextView warning = dialog.findViewById(R.id.textView54);
        db.collection("Doctors").document(doctorUid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        Doctor doctor = doc.toObject(Doctor.class);

                        if (doctor!=null){
                        if ((doctor.getName() == null || doctor.getName().isEmpty()) &&
                                (doctor.getFamillyName() == null || doctor.getFamillyName().isEmpty()) &&
                                (doctor.getSpeciality() == null || doctor.getSpeciality().isEmpty()) &&
                                (doctor.getPhone() == null || doctor.getPhone().isEmpty()) &&
                                (doctor.getTimeFinishWorking() == null || doctor.getTimeFinishWorking().isEmpty()) &&
                                (doctor.getTimeStartWorking() == null || doctor.getTimeStartWorking().isEmpty()) &&
                                (doctor.getWilaya() == null || doctor.getWilaya().isEmpty()) &&
                                (doctor.getAdress() == null || doctor.getAdress().isEmpty()) &&
                                (doctor.getMaxAtDay() == null || doctor.getMaxAtDay().isEmpty())){
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please Fill your Full Name !", Toast.LENGTH_LONG).show();
                            warning.setText("Hello Dr ,Please Fill your informations !");
                            dialog.show();
                        }else if (doctor.getName() == null || doctor.getName().isEmpty() ||
                                doctor.getFamillyName() == null || doctor.getFamillyName().isEmpty()){

                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please Fill your Full Name !", Toast.LENGTH_LONG).show();
                            warning.setText("Hello Dr ,Please Fill your Full Name !");
                            dialog.show();
                        } else if (doctor.getPhone() == null || doctor.getPhone().isEmpty()) {

                            warning.setText("Hello Dr ,Please leave your phone number !");
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please leave your phone number !", Toast.LENGTH_LONG).show();
                            dialog.show();
                        } else if (doctor.getTimeStartWorking() == null || doctor.getTimeStartWorking().isEmpty() ||
                                doctor.getTimeFinishWorking() == null || doctor.getTimeFinishWorking().isEmpty()) {

                            warning.setText("Hello Dr ,Please FIll you time of work ,its required !");
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please FIll you time of work ,its required !", Toast.LENGTH_LONG).show();
                            dialog.show();
                        } else if (!doctor.isFreefriday() && !doctor.isFreemonday() && !doctor.isFreesaturday() && !doctor.isFreesunday() && !doctor.isFreetuesday() && !doctor.isFreewednesday() && !doctor.isFreethursday()) {

                            warning.setText("Hello Dr ,Please FIll you Days of work ,its required !");
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please FIll you Days of work ,its required !", Toast.LENGTH_LONG).show();
                            dialog.show();
                        } else if (doctor.getWilaya() == null || doctor.getWilaya().isEmpty() ||
                                doctor.getAdress() == null || doctor.getAdress().isEmpty()) {

                            warning.setText("Hello Dr ,Please FIll you full Adresse ,its required !");
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please FIll you full Adresse ,its required !", Toast.LENGTH_LONG).show();
                            dialog.show();
                        } else if (doctor.getMaxAtDay() == null || doctor.getMaxAtDay().isEmpty()) {

                            warning.setText("Hello Dr ,Please FIll you max possible RDV at one day ,its required !");
                            Toast.makeText(DoctorMain.this, "Hello Dr ,Please FIll you max possible RDV at one day ,its required !", Toast.LENGTH_LONG).show();
                            dialog.show();
                        }}
                    }
                });
    }

    public void doctorProfile(View v){
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("type","doctor");
        startActivity(intent);
    }
    private void pendingRDV(String userId) {
        Prdvs.clear();
        db.collection("RDV")
                .whereEqualTo("DoctorUid",userId)
                .whereEqualTo("etat","0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            Log.e("Firestore Error", error.getMessage(), error);
                            //  Toast.makeText(PendingRDV.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                ListDesRendezVous rdv = doc.getDocument().toObject(ListDesRendezVous.class);
                                if (rdv.getEtat().equals("0")) {
                                    Prdvs.add(rdv);
                                }
                            }else if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                ListDesRendezVous modifiedRDV = doc.getDocument().toObject(ListDesRendezVous.class);
                                if (modifiedRDV.getEtat().equals("1")){
                                for (int i = 0; i < Prdvs.size(); i++) {
                                    if (Prdvs.get(i).getDateStart().equals(modifiedRDV.getDateStart()) && Prdvs.get(i).getDateFinish().equals(modifiedRDV.getDateFinish())) {
                                        Prdvs.remove(i);
                                        break;
                                    }}
                                }
                            } else if (doc.getType() == DocumentChange.Type.REMOVED) {
                                ListDesRendezVous removedRDV = doc.getDocument().toObject(ListDesRendezVous.class);

                                for (int i = 0; i < Prdvs.size(); i++) {
                                    if (Prdvs.get(i).getDateStart().equals(removedRDV.getDateStart()) && Prdvs.get(i).getDateFinish().equals(removedRDV.getDateFinish())) {
                                        Prdvs.remove(i);
                                        break;
                                    }
                                }
                            }
                        }



                        Prdvs = msort(Prdvs);



                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }

    private ArrayList<ListDesRendezVous> msort(ArrayList<ListDesRendezVous> rdvs) {
        Collections.sort(rdvs, new Comparator<ListDesRendezVous>() {
            @Override
            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
            }
        });

        ArrayList<ListDesRendezVous> uniqueRdvs = new ArrayList<>();
        ListDesRendezVous previous = null;

        for (ListDesRendezVous current : rdvs) {
            if (previous == null || !current.getDateDepo().equals(previous.getDateDepo())) {
                uniqueRdvs.add(current);
            }
            previous = current;
        }
        rdvs.clear();
        rdvs.addAll(uniqueRdvs);
        return rdvs;
    }

    private void activeRDV(String doctorUid){
        Ardvs.clear();
        db.collection("ConfirmedRDV")//.orderBy("DateStart", Query.Direction.ASCENDING)
                .whereEqualTo("DoctorUid",doctorUid)
                .whereEqualTo("etat","1")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            //Toast.makeText(PendingRDV.this, "error.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                Ardvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }/*else if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                ListDesRendezVous modifiedRDV = doc.getDocument().toObject(ListDesRendezVous.class);
                                for (int i = 0; i < Prdvs.size(); i++) {
                                    if (Prdvs.get(i).getDateDepo().equals(modifiedRDV.getDateDepo()) && Prdvs.get(i).getUserUid().equals(modifiedRDV.getUserUid())) {
                                        Prdvs.remove(i);
                                        Prdvs.add(i, modifiedRDV);
                                        break;
                                    }
                                }
                            }*/

                        }
                        Ardvs = msort(Ardvs);
                        adapter.notifyDataSetChanged();
                    }
                });

    }
    public void blood(View v){
        Intent intent = new Intent(this, DoctorBloodRequest.class);
        intent.putExtra("type","doctor");
        startActivity(intent);
    }
}