package com.example.tabibiapp;

import android.os.Bundle;
import android.widget.ListView;

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
import java.util.Collections;
import java.util.Comparator;

public class DoctorActivRDVListView extends AppCompatActivity {
    private ActiveRDVDoctorAdapter adapter;
    private ArrayList<ListDesRendezVous> rdvs;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_activ_rdvlist_view);

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        rdvs = new ArrayList<ListDesRendezVous>();
        adapter = new ActiveRDVDoctorAdapter(this,rdvs);

        String doctorUid = fAuth.getCurrentUser().getUid();
        ListView listView = findViewById(R.id.doctorActiveRDV);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
                                rdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }
                        Collections.sort(rdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}