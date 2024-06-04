package com.example.tabibiapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    ImageView imageProfile ;
    ImageView edit;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageProfile = findViewById(R.id.imageView8);
        edit = findViewById(R.id.imageView14);

        String type = getIntent().getStringExtra("type");
        if (type!=null){
            if (type.equals("doctor")){
                Button fill = findViewById(R.id.button17);
                fill.setVisibility(View.VISIBLE);
            }
        }
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfile);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                Uri imageUri =data.getData();

                uploadImageToFirebasa(imageUri);
            }
        }

    }

    private void uploadImageToFirebasa(Uri imageUri) {
        StorageReference fileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        ProgressBar progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageProfile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void back(View v){
        startActivity(new Intent(this, MaladeMain.class));
        finish();
    }
    public void signOutM(){
        ProgressBar progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void profil(View v){
        startActivity(new Intent(this, Profil.class));
    }
    public void privacy(View v){
        startActivity(new Intent(this, Privacy.class));
    }

    public void rating(View v){
        startActivity(new Intent(this, Rating.class));
    }
    public void confirmLogout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOutM();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MaladeMain.class));
        finish();
        super.onBackPressed();
    }
    public void fillInfo(View v){
        Intent intent = new Intent(this, DoctorInfo.class);
        startActivity(intent);
    }public void areyoudoctorU(View v){
        Intent intent = new Intent(this, NewDoctorRegistration.class);
        startActivity(intent);
    }
}