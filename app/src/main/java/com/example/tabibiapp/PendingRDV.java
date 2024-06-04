package com.example.tabibiapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class PendingRDV extends AppCompatActivity {
    private FirebaseAuth fAuth;
    public ArrayList<ListDesRendezVous> rdvs;
    private FirebaseFirestore fStore;
    private RdvAdapter rdvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_rdv);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String UserId = user.getUid();
        rdvs = new ArrayList<ListDesRendezVous>();
        pendingRDV(UserId);
        rdvAdapter = new RdvAdapter(this, rdvs);
        ListView listView = findViewById(R.id.listViewRDVS);
        listView.setAdapter(rdvAdapter);
        rdvAdapter.notifyDataSetChanged();

        Toast.makeText(this, "pending" +fAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();


    }

    private void pendingRDV(String userId) {

        fStore.collection("RDV")//.orderBy("DateDepo", Query.Direction.ASCENDING)
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
                                rdvs.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }

                        }

                        Collections.sort(rdvs, new Comparator<ListDesRendezVous>() {
                            @Override
                            public int compare(ListDesRendezVous rdv1, ListDesRendezVous rdv2) {
                                return rdv1.getDateDepo().compareTo(rdv2.getDateDepo());
                            }
                        });

                        rdvAdapter.notifyDataSetChanged();
                    }
                });
    }
}