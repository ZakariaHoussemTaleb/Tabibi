package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicalRecordListView extends AppCompatActivity {
    private MedicalRecordAdapter adapter;
    private ArrayList<MedicalRecord> medicalRecords;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    String userUid;
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        medicalRecords = new ArrayList<MedicalRecord>();
        adapter = new MedicalRecordAdapter(this,medicalRecords);

        String utilisateur = getIntent().getStringExtra("natur");
        if (utilisateur.equals("user")){
            userUid = fAuth.getCurrentUser().getUid();
        } else if (utilisateur.equals("doctor")) {
            userUid = getIntent().getStringExtra("UserUid");
        }
        ListView listView = findViewById(R.id.listView007);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        userName = findViewById(R.id.textView63);

        db.collection("Users").document(userUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String username = task.getResult().getString("UserName");
                    userName.setText("  Medical Records of Mr."+username);
                }
            }
        });


        db.collection("MedicalRecords").whereEqualTo("UserUid",userUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                medicalRecords.add(doc.getDocument().toObject(MedicalRecord.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });




    }
}