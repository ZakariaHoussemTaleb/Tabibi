package com.example.tabibiapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewDoctorRegistration extends AppCompatActivity {

    ImageView imageView,imageViewUploadpdf,how;
    ProgressBar progressBar;
    Uri imageUri,pdfUri;
    StorageReference fileRef;
    Button sendRequest;
    String userUid;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_doctor_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fAuth=FirebaseAuth.getInstance();
        userUid = fAuth.getCurrentUser().getUid();

        how =findViewById(R.id.imageView22);
        sendRequest = findViewById(R.id.button14);
        progressBar = findViewById(R.id.progressBar8);
        imageView = findViewById(R.id.imageView23);
        imageViewUploadpdf = findViewById(R.id.pdf);
        how.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogueBox();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imageViewUploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                selectPdf();
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebasa();
            }
        });

    }

    private void showDialogueBox() {
        Dialog dialog = new Dialog(NewDoctorRegistration.this);
        dialog.setContentView(R.layout.custom_dialigue_box_no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(NewDoctorRegistration.this, R.drawable.custom_dialogue));
        dialog.setCancelable(true);

        Button dialogueDepose = dialog.findViewById(R.id.exitinternet);
        TextView title = dialog.findViewById(R.id.textView53);
        TextView body = dialog.findViewById(R.id.textView54);
        title.setText("your informations");
        body.setText("Our doctor booking app employs a strict verification process to ensure the authenticity of participating doctors. Doctors are required to upload PDF documents of their diplomas and commerce registry, which undergo automated and manual verification. This includes cross-referencing with official databases and conducting background checks on their education and licenses. Verified doctors will have the ability to display their legitimate specialty, instilling trust in users");
        dialogueDepose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void uploadPDFToFirebas() {
        fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+userUid+"/IDfolder.pdf");
        progressBar.setVisibility(View.VISIBLE);
        if (pdfUri == null ){
            progressBar.setVisibility(View.INVISIBLE);
            return;}
        fileRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NewDoctorRegistration.this, "Your pdf has been uploaded successfuly! ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewDoctorRegistration.this, NewDoctorFillInfo.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewDoctorRegistration.this, "Failed , chack your ethernet and retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),35);
    }

    private void selectImage() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (requestCode == 35 && data != null) {
            pdfUri = data.getData();
            imageViewUploadpdf.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
        }
    }
    private void uploadImageToFirebasa() {
        fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+userUid+"/IDcard.jpg");
        progressBar.setVisibility(View.VISIBLE);
        if (imageUri == null ){
            progressBar.setVisibility(View.INVISIBLE);
            return;}
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef = FirebaseStorage.getInstance().getReference().child("Users/"+userUid+"/profile.jpg");
                imageUri = fAuth.getCurrentUser().getPhotoUrl();
                if (imageUri!=null){
                    fileRef.putFile(imageUri);}
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NewDoctorRegistration.this, "Your image has been uploaded successfuly! ", Toast.LENGTH_SHORT).show();
                uploadPDFToFirebas();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewDoctorRegistration.this, "Failed , chack your ethernet and retry", Toast.LENGTH_SHORT).show();
            }
        });


    }

}