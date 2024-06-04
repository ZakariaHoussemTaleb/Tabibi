package com.example.tabibiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewDoctorFillInfo extends AppCompatActivity {

    EditText name,fullname,msg;
    Button button;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_doctor_fill_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        name = findViewById(R.id.nameRequest);
        fullname = findViewById(R.id.fullNamerequest);
        msg = findViewById(R.id.msgforadmin);

        String names = user.getDisplayName();
        if (names!=null){
            name.setText(names);
        }

        button = findViewById(R.id.button1991);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewDoctorFillInfo.this);
                builder.setMessage("Are you sure you want to send the request");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String,Object> reqInfo = new HashMap<>();
                        reqInfo.put("Name",name.getText().toString());
                        reqInfo.put("FullName",fullname.getText().toString());
                        reqInfo.put("RequesterUid",fAuth.getCurrentUser().getUid());
                        reqInfo.put("Msg",msg.getText().toString());

                        fStore.collection("Requests").document(fAuth.getCurrentUser().getUid())
                                .set(reqInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(NewDoctorFillInfo.this,MaladeMain.class);
                                            Toast.makeText(NewDoctorFillInfo.this, "Please wait for admin confirmation", Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(NewDoctorFillInfo.this, "some thing went wrong!", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });


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


    }
}