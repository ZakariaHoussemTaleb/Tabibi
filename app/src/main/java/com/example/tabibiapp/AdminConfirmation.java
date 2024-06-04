package com.example.tabibiapp;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminConfirmation extends AppCompatActivity {
    FirebaseFirestore fStore;
    String speciality="general medicine";
    String uid;
    Uri iuri;
    Button valider,refuse;
    TextView name,fullName,msg;
    ImageView imageView,idcard;
    private DoctorRequest request;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idcard = findViewById(R.id.imageView29);
        name = findViewById(R.id.textView73);
        fullName = findViewById(R.id.textView74);
        msg = findViewById(R.id.textView75);
        imageView = findViewById(R.id.imageView26);
        refuse = findViewById(R.id.button26);
        fStore = FirebaseFirestore.getInstance();
        uid = getIntent().getStringExtra("Uid");
        valider = findViewById(R.id.valider);
        storageReference = FirebaseStorage.getInstance().getReference();
        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speciality = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> specialities = new ArrayList<String>();
        specialities.add("general medicine");
        specialities.add("orthopedics");
        specialities.add("cardiology");
        specialities.add("dermatology");
        specialities.add("gynecology");
        specialities.add("odontology");
        specialities.add("oncology");
        specialities.add("ophtamology");

        ArrayAdapter<String> adapterS = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,specialities);
        adapterS.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapterS);


        StorageReference profileRef = storageReference.child("Users/"+uid+"/IDcard.jpg");
        StorageReference pdf = storageReference.child("Users/"+uid+"/IDfolder.pdf");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        openPDF(uri);
                    }
                });

            }
        });


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                iuri = uri;
            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStore.collection("Requests").document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminConfirmation.this, "Refused !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminConfirmation.this,DoctorRequests.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminConfirmation.this, "some thing went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminConfirmation.this,FullScreenImage.class);
                intent.setData(iuri);
                startActivity(intent);
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminConfirmation.this);
                builder.setMessage("Are you sure you want to give doctor access?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDoctor();
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
        });

        fechData();


    }

    private void openPDF(Uri pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUrl,"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent chooser = Intent.createChooser(intent ,"Open with 7abayb: ");
        try {
            startActivity(chooser);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "no application to view pdf ", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDoctor() {
        if (request != null){
            DocumentReference df = fStore.collection("Doctors").document(uid);
            Map<String,Object> userInfo = new HashMap<>();
            userInfo.put("Name",request.getName());
            userInfo.put("FamillyName",request.getFullName());
            userInfo.put("Uid",request.getRequesterUid());
            userInfo.put("Speciality",speciality);
            df.set(userInfo);

            DocumentReference df2= fStore.collection("Users").document(uid);
            Map<String,Object> usersInfo = new HashMap<>();
            userInfo.put("isUser",null);
            usersInfo.put("isDoctor","1");
            df2.update(usersInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Intent intent = new Intent(AdminConfirmation.this,DoctorRequests.class);
                    Toast.makeText(AdminConfirmation.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminConfirmation.this, "failed", Toast.LENGTH_SHORT).show();
                }
            });

            fStore.collection("Requests").document(uid).delete();
        }
    }

    private void fechData() {
        fStore.collection("Requests")
                .whereEqualTo("RequesterUid",uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                             request = task.getResult().getDocuments().get(0).toObject(DoctorRequest.class);
                            name.setText(request.getName());
                            fullName.setText(request.getFullName());
                            msg.setText(request.getMsg());
                        }
                    }
                });
    }
}