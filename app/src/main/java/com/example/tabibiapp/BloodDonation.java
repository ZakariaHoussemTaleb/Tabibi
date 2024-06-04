package com.example.tabibiapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodDonation extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    ListView listView;
    String zomra;
    public ArrayList<BloodRequest> requests;
    private BloodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blood_donation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        requests = new ArrayList<BloodRequest>();
        listView = findViewById(R.id.bloodNeeders);
        fStore.collection("Users").document(fAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                String bloodType = value.getString("blood");
                if (bloodType == null) {
                    showDialogue();
                } else {
                    fechdata(bloodType);
                }
            } else {
                showDialogue();
            }}
        });




        adapter = new BloodAdapter(this,requests);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();





    }

    private void fechdata(String bloodZomra) {

        fStore.collection("BloodRequests").whereEqualTo("blood",bloodZomra)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                requests.add(doc.getDocument().toObject(BloodRequest.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    private void showDialogue() {
        Dialog dialog = new Dialog(BloodDonation.this);
        dialog.setContentView(R.layout.dialogue_box_blood_zomra);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(BloodDonation.this, R.drawable.custom_dialogue));
        dialog.setCancelable(false);


        Spinner spinner = dialog.findViewById(R.id.spinner2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zomra = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button dialogueDepose = dialog.findViewById(R.id.booddodo);
        ArrayList<String> zomras = new ArrayList<String>();
        zomras.add("O+");
        zomras.add("O-");
        zomras.add("A+");
        zomras.add("A-");
        zomras.add("B+");
        zomras.add("B-");
        zomras.add("AB+");
        zomras.add("AB-");

        ArrayAdapter<String> adapterS = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,zomras);
        adapterS.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapterS);

        dialogueDepose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zomra != null ) {
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("blood",zomra);
                fStore.collection("Users").document(fAuth.getCurrentUser().getUid()).update(userInfo);
                    fechdata(zomra);
                    dialog.dismiss();
            }
            else {
                    Toast.makeText(BloodDonation.this, "select a zomra", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();


    }
}