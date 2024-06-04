package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorsListView extends AppCompatActivity {
    private Adapter adapter;
    public ArrayList<Doctor> doctors;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_view);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        doctors = new ArrayList<Doctor>();

        String speciality = getIntent().getStringExtra("Speciality");



        adapter = new Adapter(this, doctors);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(DoctorsListView.this,Info.class);
                Toast.makeText(DoctorsListView.this, "confirm your email.",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra("name","Dr."+doctors.get(position).getName());
                startActivity(intent);
            }
        });
        if (speciality.equals("all")){
        eventChange();} else if (speciality.equals("cardio")) {
            cardio();
        }else if (speciality.equals("der")) {
            der();
        }else if (speciality.equals("gen")) {
            gen();
        }else if (speciality.equals("gyn")) {
            gyn();
        }else if (speciality.equals("odon")) {
            odon();
        }else if (speciality.equals("onco")) {
            onco();
        }else if (speciality.equals("oph")) {
            oph();
        }else if (speciality.equals("orth")) {
            orth();
        }
    }

    private void orth() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","orthopedics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void oph() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","ophtamology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onco() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","oncology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void odon() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","odontology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void gyn() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","gynecology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void gen() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","general medicine")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void der() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","dermatology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void cardio() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .whereEqualTo("Speciality","cardiology")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void eventChange() {
        fStore.collection("Doctors").orderBy("FamillyName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                doctors.add(doc.getDocument().toObject(Doctor.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });


    }
}