package com.example.tabibiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UploadMedicalRecord extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    Button ok,cancel;
    EditText editText , tritment;
    Uri imageUri;
    StorageReference fileRef;
    String doctorUid , userUid;
    FirebaseFirestore fStore;
    FirebaseAuth  fAuth;
    Doctor doctor;
    MedicalRecord medicalRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_medical_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fStore.collection("Doctors").document(fAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                doctor = value.toObject(Doctor.class);
            }
        });


         imageView = findViewById(R.id.imageView19);
         progressBar = findViewById(R.id.progressBar5);
         ok = findViewById(R.id.button199);
         cancel = findViewById(R.id.button1612);
         editText = findViewById(R.id.editTextTextMultiLine33);
         tritment = findViewById(R.id.editTextTextMultiLine);
         doctorUid = getIntent().getStringExtra("DoctorUid");
         userUid = getIntent().getStringExtra("UserUid");


        fStore.collection("MedicalRecords").document(doctorUid+userUid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        medicalRecord = value.toObject(MedicalRecord.class);
                        if (medicalRecord!=null){
                        editText.setText(medicalRecord.getDiagnosis());
                        tritment.setText(medicalRecord.getTritmentPlan());
                        }
                    }
                });

        fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+userUid+"/"+doctorUid+"/radio.jpg");

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaDiscriptionToFirebasa();
                uploadImageToFirebasa();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadMedicalRecord.this, DoctorMain.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void uploaDiscriptionToFirebasa() {
        String diagnosis = editText.getText().toString();
        String tritmentPlan = tritment.getText().toString();
        if (diagnosis.isEmpty() || tritmentPlan.isEmpty()) return;

        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("Diagnosis",diagnosis);
        userInfo.put("TritmentPlan",tritmentPlan);
        userInfo.put("DoctorUid",doctorUid);
        userInfo.put("UserUid",userUid);
        userInfo.put("DoctorFullName","Dr."+doctor.getName()+""+doctor.getFamillyName());
        userInfo.put("DoctorSpeciality",doctor.getSpeciality());
        fStore.collection("MedicalRecords").document(doctorUid+userUid).set(userInfo);
    }

    private void selectImage() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,100);
        /*
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private void uploadImageToFirebasa() {
        fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+userUid+"/"+doctorUid+"/radio.jpg");
        progressBar.setVisibility(View.VISIBLE);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadMedicalRecord.this, "Your image has been uploaded successfuly! ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadMedicalRecord.this, DoctorMain.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadMedicalRecord.this, "Failed , chack your ethernet and retry", Toast.LENGTH_SHORT).show();
            }
        });

    }
}