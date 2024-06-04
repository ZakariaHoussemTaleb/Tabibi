package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorBloodRequest extends AppCompatActivity {
    EditText wilaya,adress,phone,hospital,need;
    Button upload;
    Spinner spinner;
    String zomra="O+";
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_blood_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fAuth=FirebaseAuth.getInstance();;
        fStore=FirebaseFirestore.getInstance();;
        need = findViewById(R.id.needa);
        hospital = findViewById(R.id.hopitaalaa);
        wilaya = findViewById(R.id.wilayaa);
        adress = findViewById(R.id.adressa);
        phone = findViewById(R.id.phonaaa);
        upload = findViewById(R.id.ButtonDoctorUploadBlood);
        spinner = findViewById(R.id.spinnerBlood);
        progressBar = findViewById(R.id.progressBarBlood);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zomra = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String wilayaa = wilaya.getText().toString();
                String adresse = adress.getText().toString();
                String phonee = phone.getText().toString();
                String hosp = hospital.getText().toString();
                String needed = need.getText().toString();

                if (wilayaa.isEmpty() || adresse.isEmpty() || phonee.isEmpty() || hosp.isEmpty() || needed.isEmpty()){
                    Toast.makeText(DoctorBloodRequest.this, "fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,Object> newRequest = new HashMap<>();
                newRequest.put("blood",zomra);
                newRequest.put("DoctorUid",fAuth.getCurrentUser().getUid());
                newRequest.put("Wilaya",wilayaa);
                newRequest.put("Adress",adresse);
                newRequest.put("Hospital",hosp);
                newRequest.put("Need",needed);
                newRequest.put("phone",phonee);

                fStore.collection("BloodRequests").add(newRequest).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DoctorBloodRequest.this, "Your request is sended", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorBloodRequest.this,DoctorMain.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoctorBloodRequest.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

                progressBar.setVisibility(View.INVISIBLE);

            }

        });



    }
}