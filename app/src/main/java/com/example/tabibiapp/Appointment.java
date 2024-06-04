package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Appointment extends AppCompatActivity {
    private FirebaseAuth fAuth;
    public ArrayList<ListDesRendezVous> confirmedrdvs;
    public ArrayList<ListDesRendezVous> refusedrdvs;
    public ArrayList<ListDesRendezVous> pendingrdvs;
    public ArrayList<ListDesRendezVous> donerdvs;
    private FirebaseFirestore fStore;
    private MaladeRDVAdapter rdvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.rdv);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.rdv) {
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(this, MaladeMain.class));
                finish();
            }else if (itemId == R.id.profil) {
                startActivity(new Intent(this, Profile.class));
                finish();
            }
            return false;

        });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        confirmedrdvs = new ArrayList<ListDesRendezVous>();
        refusedrdvs = new ArrayList<ListDesRendezVous>();
        pendingrdvs = new ArrayList<ListDesRendezVous>();
        donerdvs = new ArrayList<ListDesRendezVous>();




    }

    public void confirmedRDV(View v) {
        pendingrdvs.clear();
        refusedrdvs.clear();
        confirmedrdvs.clear();
        rdvAdapter = new MaladeRDVAdapter(this, confirmedrdvs);
        TextView message = findViewById(R.id.textView47);
        message.setText("your confirmed apointments:");
        FirebaseUser user = fAuth.getCurrentUser();
        fStore.collection("ConfirmedRDV")//.orderBy("DateDepo", Query.Direction.ASCENDING)
                .whereEqualTo("UserUid",user.getUid())
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
                                confirmedrdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }

                        Collections.sort(confirmedrdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });
                        ListView listView = findViewById(R.id.maladeListViewRDV);
                        listView.setAdapter(rdvAdapter);
                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }
    public void refusedRDV(View v) {
        pendingrdvs.clear();
        refusedrdvs.clear();
        confirmedrdvs.clear();
        rdvAdapter = new MaladeRDVAdapter(this, refusedrdvs);
        TextView message = findViewById(R.id.textView47);
        message.setText("your refused apointments:");
        FirebaseUser user = fAuth.getCurrentUser();
        fStore.collection("RDV")//.orderBy("DateDepo", Query.Direction.ASCENDING)
                .whereEqualTo("UserUid",user.getUid())
                .whereEqualTo("etat","-1")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            //Toast.makeText(PendingRDV.this, "error.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                refusedrdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }
                        Collections.sort(refusedrdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });
                        ListView listView = findViewById(R.id.maladeListViewRDV);
                        listView.setAdapter(rdvAdapter);
                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }
    public void pendingRDV(View v) {
        pendingrdvs.clear();
        refusedrdvs.clear();
        confirmedrdvs.clear();
        rdvAdapter = new MaladeRDVAdapter(this, pendingrdvs);
        TextView message = findViewById(R.id.textView47);
        message.setText("your pending apointments:(These times are approximate and will change when the appointment is confirmed )");
        FirebaseUser user = fAuth.getCurrentUser();
        fStore.collection("RDV")//.orderBy("DateDepo", Query.Direction.ASCENDING)
                .whereEqualTo("UserUid",user.getUid())
                .whereEqualTo("etat","0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            //Toast.makeText(PendingRDV.this, "error.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                pendingrdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }
                        Collections.sort(pendingrdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });
                        ListView listView = findViewById(R.id.maladeListViewRDV);
                        listView.setAdapter(rdvAdapter);
                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }
    public void doneRDV(View v) {
        pendingrdvs.clear();
        refusedrdvs.clear();
        confirmedrdvs.clear();
        donerdvs.clear();
        rdvAdapter = new MaladeRDVAdapter(this, donerdvs);
        TextView message = findViewById(R.id.textView47);
        message.setText("your complete apointments:");
        FirebaseUser user = fAuth.getCurrentUser();
        fStore.collection("RDV")//.orderBy("DateDepo", Query.Direction.ASCENDING)
                .whereEqualTo("UserUid",user.getUid())
                .whereEqualTo("etat","done")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            //Toast.makeText(PendingRDV.this, "error.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                donerdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }
                        Collections.sort(donerdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });
                        ListView listView = findViewById(R.id.maladeListViewRDV);
                        listView.setAdapter(rdvAdapter);
                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }
}