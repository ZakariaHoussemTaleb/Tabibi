package com.example.tabibiapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DossierMedical extends AppCompatActivity {

    TextView diagnosis,TritmentPlan;
    ImageView imageView ;
    String diag,trim;
    ProgressBar progressBar;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dossier_medical);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        diagnosis = findViewById(R.id.textView57);
        TritmentPlan = findViewById(R.id.textView59);
        progressBar = findViewById(R.id.progressBar6);
        diag = getIntent().getStringExtra("Diagnosis");
        diagnosis.setText(diag);
        trim = getIntent().getStringExtra("TritmentPlan");
        TritmentPlan.setText(trim);
        imageView = findViewById(R.id.imageView20);
        storageReference = FirebaseStorage.getInstance().getReference();
        String userUid = getIntent().getExtras().getString("UserUid");
        String doctorUid = getIntent().getExtras().getString("DoctorUid");

        StorageReference fileRef = storageReference.child("Users/"+userUid+"/"+doctorUid+"/radio.jpg");

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }
}