package com.example.tabibiapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorInfo extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;

    String timeStar ;
    String timeFinish ;
    boolean isSunday,isMonday,isTuesday,isWednesday,isThursday,isFriday,isSaturday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);



        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



        EditText timeStarte = findViewById(R.id.editTextTime1);
        EditText timeFinishe = findViewById(R.id.editTextTime2);

        timeStarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorInfo.this,(view, hourOfDay, minuteOfHour) -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minuteOfHour);
                    timeStar = sdf.format(calendar.getTime());
                    timeStarte.setText(timeStar);
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
        timeFinishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorInfo.this,(view, hourOfDay, minuteOfHour) -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minuteOfHour);
                    timeFinish = sdf.format(calendar.getTime());
                    timeFinishe.setText(timeFinish);
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        Button sunday = findViewById(R.id.Sunday);
        Button monday = findViewById(R.id.Monday);
        Button tuesday = findViewById(R.id.Tuesday);
        Button wednesday = findViewById(R.id.Wednesday);
        Button thursday = findViewById(R.id.Thursday);
        Button friday = findViewById(R.id.Friday);
        Button saturday = findViewById(R.id.Saturday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSunday){
                sunday.setBackgroundColor(getResources().getColor(R.color.color1));
                }
                else {
                    sunday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isSunday=!isSunday;
            }
        });
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isMonday){
                monday.setBackgroundColor(getResources().getColor(R.color.color1));}
                else {
                    monday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isMonday=!isMonday;
            }
        });
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isTuesday){
                    tuesday.setBackgroundColor(getResources().getColor(R.color.color1));}
                else {
                    tuesday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isTuesday=!isTuesday;
            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isWednesday){
                    wednesday.setBackgroundColor(getResources().getColor(R.color.color1));}
                else {
                    wednesday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isWednesday=!isWednesday;
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isThursday){
                    thursday.setBackgroundColor(getResources().getColor(R.color.color1));}
                else {
                    thursday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isThursday=!isThursday;
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFriday){
                    friday.setBackgroundColor(getResources().getColor(R.color.color1));
                }
                else {
                    friday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isFriday=!isFriday;
            }
        });
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSaturday){
                    saturday.setBackgroundColor(getResources().getColor(R.color.color1));}
                else {
                    saturday.setBackgroundColor(getResources().getColor(R.color.white));
                }
                isSaturday=!isSaturday;
            }
        });

        Button button = findViewById(R.id.ButtonDoctorUpload);
        EditText dname = findViewById(R.id.DoctorName);
        EditText dfname = findViewById(R.id.DoctorFName);
        EditText dphone = findViewById(R.id.DoctorPhone);
        EditText dwilaya = findViewById(R.id.DoctorWilaya);
        EditText dfadresse = findViewById(R.id.DoctorAdress);
        EditText dmax = findViewById(R.id.max);

        String Uid = fAuth.getCurrentUser().getUid();
        oldInfo(Uid);
        ProgressBar progressBar=findViewById(R.id.progressBar4);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorInfo.this);

                builder.setMessage("Are you sure you want to publish these information?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = fAuth.getCurrentUser();
                String name = dname.getText().toString();
                String fullName = dfname.getText().toString();
                String phone = dphone.getText().toString();
                String wilaya = dwilaya.getText().toString();
                String fadresse = dfadresse.getText().toString();
                String max = dmax.getText().toString();

                DocumentReference df = fstore.collection("Doctors").document(user.getUid());
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("Name",name);
                userInfo.put("FamillyName",fullName);
                userInfo.put("Phone",phone);
                userInfo.put("TimeStartWorking",timeStar);
                userInfo.put("TimeFinishWorking",timeFinish);
                userInfo.put("Freetuesday",isTuesday);
                userInfo.put("Freesaturday",isSaturday);
                userInfo.put("Freesunday",isSunday);
                userInfo.put("Freemonday",isMonday);
                userInfo.put("Freewednesday",isWednesday);
                userInfo.put("Freethursday",isThursday);
                userInfo.put("Freefriday",isFriday);
                userInfo.put("MaxAtDay",max);
                userInfo.put("Wilaya",wilaya);
                userInfo.put("Adress",fadresse);
                userInfo.put("Uid",Uid);
                df.set(userInfo);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DoctorInfo.this, "Your informations are successfuly uploaded ,thank you doctor!", Toast.LENGTH_SHORT).show();
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

    private void oldInfo(String uid) {
        DocumentReference df = fstore.collection("Doctors").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() ){
                            DocumentSnapshot doc  = task.getResult();
                            Doctor doctor =doc.toObject(Doctor.class);
                            if (doctor==null) return;
                            Button sunday = findViewById(R.id.Sunday);
                            Button monday = findViewById(R.id.Monday);
                            Button tuesday = findViewById(R.id.Tuesday);
                            Button wednesday = findViewById(R.id.Wednesday);
                            Button thursday = findViewById(R.id.Thursday);
                            Button friday = findViewById(R.id.Friday);
                            Button saturday = findViewById(R.id.Saturday);
                            EditText dname = findViewById(R.id.DoctorName);
                            EditText dfname = findViewById(R.id.DoctorFName);
                            EditText dphone = findViewById(R.id.DoctorPhone);
                            EditText dwilaya = findViewById(R.id.DoctorWilaya);
                            EditText dfadresse = findViewById(R.id.DoctorAdress);
                            EditText dmax = findViewById(R.id.max);
                            isSunday=doctor.isFreesunday();
                             isMonday=doctor.isFreemonday();
                             isTuesday=doctor.isFreetuesday();
                             isWednesday=doctor.isFreewednesday();
                             isThursday=doctor.isFreethursday();
                             isFriday=doctor.isFreefriday();
                             isSaturday=doctor.isFreesaturday();
                            EditText timeStarte = findViewById(R.id.editTextTime1);
                            EditText timeFinishe = findViewById(R.id.editTextTime2);

                            if (isSaturday){
                                    saturday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                    saturday.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            if (isFriday){
                                friday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                friday.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (isThursday){
                                thursday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                thursday.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (isWednesday){
                                wednesday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                wednesday.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (isTuesday){
                                tuesday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                tuesday.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (isMonday){
                                monday.setBackgroundColor(getResources().getColor(R.color.color1));}
                            else {
                                monday.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            if (isSunday){
                                sunday.setBackgroundColor(getResources().getColor(R.color.color1));
                            }
                            else {
                                sunday.setBackgroundColor(getResources().getColor(R.color.white));
                            }


                            dname.setText(doctor.getName());
                            dfname.setText(doctor.getFamillyName());
                            dphone.setText(doctor.getPhone());
                            timeStarte.setText(doctor.getTimeStartWorking());
                            timeFinishe.setText(doctor.getTimeFinishWorking());
                            dwilaya.setText(doctor.getWilaya());
                            dfadresse.setText(doctor.getAdress());
                            dmax.setText(doctor.getMaxAtDay());


                        }
                    }
                });
    }


}