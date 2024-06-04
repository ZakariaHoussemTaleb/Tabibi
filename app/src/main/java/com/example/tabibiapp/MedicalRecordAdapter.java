package com.example.tabibiapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MedicalRecordAdapter extends ArrayAdapter<MedicalRecord> {
    private Context context;
    private ArrayList<MedicalRecord> medicalRecords;
    TextView doctorName,speciality;
    Button consult;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    public MedicalRecordAdapter(Context context, ArrayList<MedicalRecord> medicalRecords) {
        super(context, 0, medicalRecords);
        this.context = context;
        this.medicalRecords = medicalRecords;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_medical_record, parent, false);
        }
        MedicalRecord currentmedicalRecord = medicalRecords.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fStore=FirebaseFirestore.getInstance();

        doctorName = listItemView.findViewById(R.id.textView252);
        speciality = listItemView.findViewById(R.id.textView262);
        consult = listItemView.findViewById(R.id.button7p);

        doctorName.setText(currentmedicalRecord.getDoctorFullName());
        speciality.setText(currentmedicalRecord.getDoctorSpeciality());
        notifyDataSetChanged();



        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DossierMedical.class);
                intent.putExtra("Diagnosis",currentmedicalRecord.getDiagnosis());
                intent.putExtra("DoctorUid",currentmedicalRecord.getDoctorUid());
                intent.putExtra("TritmentPlan",currentmedicalRecord.getTritmentPlan());
                intent.putExtra("UserUid",currentmedicalRecord.getUserUid());

                context.startActivity(intent);
            }
        });

        return listItemView;
        }
    }
