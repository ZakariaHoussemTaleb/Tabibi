package com.example.tabibiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DrugsAdapter extends ArrayAdapter<MedicalRecord> {
    private Context context;
    private ArrayList<MedicalRecord> medicalRecords;
    TextView doctorName,speciality,tritment;
    Button consult;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    public DrugsAdapter(Context context, ArrayList<MedicalRecord> medicalRecords) {
        super(context, 0, medicalRecords);
        this.context = context;
        this.medicalRecords = medicalRecords;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_tritment, parent, false);
        }
        MedicalRecord currentmedicalRecord = medicalRecords.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        doctorName = listItemView.findViewById(R.id.textView2521);
        speciality = listItemView.findViewById(R.id.textView2621);
        tritment = listItemView.findViewById(R.id.textView61);

        doctorName.setText(currentmedicalRecord.getDoctorFullName());
        speciality.setText(currentmedicalRecord.getDoctorSpeciality());
        tritment.setText(currentmedicalRecord.getTritmentPlan());

        return listItemView;
    }
    }
