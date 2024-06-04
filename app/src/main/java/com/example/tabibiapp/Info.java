package com.example.tabibiapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Info extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    private ArrayList<Coment> coments;
    private ComentsAdapter adapter;
    Dialog dialog;
    Button dialogueDepose , dialogueCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fStore = FirebaseFirestore.getInstance();

        String fullName = getIntent().getStringExtra("name");
        String Uid = getIntent().getStringExtra("Uid");
        TextView drname = findViewById(R.id.textView28);

        drname.setText(fullName);
        ImageView imageView = findViewById(R.id.imageView13);

        StorageReference profileRef = storageReference.child("Users/"+Uid+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        coments=new ArrayList<Coment>();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialigue_box_discription);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogue));
        dialog.setCancelable(false);
        dialogueDepose = dialog.findViewById(R.id.button172);
        dialogueCancel = dialog.findViewById(R.id.button16);

        dialogueCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogueDepose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText descriptionf = dialog.findViewById(R.id.editTextTextMultiLine);
                String description = descriptionf.getText().toString();
                Intent intent = new Intent(Info.this,RondezVous.class);
                intent.putExtra("Uid",Uid);
                intent.putExtra("Description",description);
                startActivity(intent);
                finish();
            }
        });


        Button button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        adapter=new ComentsAdapter(this,coments);
        ListView listView = findViewById(R.id.listView2);
        listView.setAdapter(adapter);
        fStore.collection("Coments").whereEqualTo("DoctorUid",Uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                coments.add(doc.getDocument().toObject(Coment.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }
}