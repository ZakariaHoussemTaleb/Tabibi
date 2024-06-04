package com.example.tabibiapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Rating extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ArrayList<String> doctorsUidDuplicated;
    private ArrayList<String> doctorsUid;
    private ArrayList<Doctor> doctors;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

         doctorsUidDuplicated = new ArrayList<String>();
         doctorsUid = new ArrayList<String>();
         doctors = new ArrayList<Doctor>();

         adapter=new Adapter(this,doctors);
        ListView listView = findViewById(R.id.ListViewDone);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fStore.collection("RDV").whereEqualTo("UserUid",fAuth.getCurrentUser().getUid())
                .whereEqualTo("etat","done")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    return;
                }
                if (value != null){
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String doctorUid = doc.getDocument().getString("DoctorUid");
                        if (doctorUid != null) {
                            doctorsUidDuplicated.add(doctorUid);
                        }
                    }

                }
                doctorsUid = doctorsUidDuplicated.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                if (!doctorsUid.isEmpty()){
                    for (String uid : doctorsUid) {
                        DocumentReference doctorDoc = fStore.collection("Doctors").document(uid);
                        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Doctor doctor = document.toObject(Doctor.class);
                                        if (doctor != null) {
                                            doctors.add(doctor);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }

                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }

                }
            }}
        });

    }
}