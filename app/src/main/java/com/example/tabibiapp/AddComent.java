package com.example.tabibiapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddComent extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coment);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String fullName = getIntent().getStringExtra("name");
        String Uid = getIntent().getStringExtra("Uid");




        String userUid = fAuth.getCurrentUser().getUid();
        Button add = findViewById(R.id.button62);

        fStore.collection("Users").document(userUid)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String username = task.getResult().getString("UserName");
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddComent.this);
                                builder.setMessage("Are you sur you want to publish your opinion about Doctor :"+fullName);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                EditText commentf = findViewById(R.id.editTextTextMultiLine2);
                                String comment = commentf.getText().toString();
                                if (TextUtils.isEmpty(comment)){
                                    commentf.setError("Your coment is empty");
                                    return;
                                }
                                EditText ratingf = findViewById(R.id.editTextNumberDecimal);
                                String rating = ratingf.getText().toString();
                                if (TextUtils.isEmpty(rating)){
                                    ratingf.setError("please rate");
                                    return;
                                }
                                Map<String,Object> commentInfo = new HashMap<>();
                                commentInfo.put("userName",username);
                                commentInfo.put("UserUid",userUid);
                                commentInfo.put("DoctorUid",Uid);
                                commentInfo.put("comment",comment);
                                commentInfo.put("rating",rating);

                                fStore.collection("Coments").add(commentInfo);
                                Toast.makeText(AddComent.this, "Your Comment is public now thank you for your opinion .", Toast.LENGTH_LONG).show();
                                ratingf.setText("");
                                commentf.setText("");
                                    }
                        });
                           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           });
                           builder.create().show();
                            }
                        });
                    }
                    }
                });

    }
}