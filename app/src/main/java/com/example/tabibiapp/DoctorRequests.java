package com.example.tabibiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorRequests extends AppCompatActivity {
    public ArrayList<String> doctors;
    public ArrayList<String> uids;
    public ArrayAdapter<String> adapter;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fStore = FirebaseFirestore.getInstance();

        doctors = new ArrayList<String>();
        uids = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this,R.layout.item_request_doctor,R.id.doctorrequestname1,doctors);
        ListView listView = findViewById(R.id.requestListView);
        listView.setAdapter(adapter);
        fechRequests();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Uid = uids.get(position);
                uids.remove(position);
                doctors.remove(position);
                Intent intent = new Intent(DoctorRequests.this, AdminConfirmation.class);
                intent.putExtra("Uid", Uid);
                startActivity(intent);
            }
        });


    }
    private void fechRequests() {
        doctors.clear();
        fStore.collection("Requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null){
                            for (DocumentChange doc : value.getDocumentChanges()){
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    doctors.add(doc.getDocument().getString("FullName"));
                                    uids.add(doc.getDocument().getString("RequesterUid"));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}