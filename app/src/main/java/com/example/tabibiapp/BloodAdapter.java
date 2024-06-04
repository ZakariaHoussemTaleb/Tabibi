package com.example.tabibiapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BloodAdapter extends ArrayAdapter<BloodRequest> {
    private Context context;
    private ArrayList<BloodRequest> Requests;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore fStore;

    public BloodAdapter(Context context, ArrayList<BloodRequest> Requests) {
        super(context, 0, Requests);
        this.context = context;
        this.Requests = Requests;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_blooda, parent, false);
        }
        BloodRequest currentRequest = Requests.get(position);
        fStore = FirebaseFirestore.getInstance();

        TextView doctorName = listItemView.findViewById(R.id.textView2521);
        TextView hospital = listItemView.findViewById(R.id.textView2621);
        Button call = listItemView.findViewById(R.id.button25);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = currentRequest.getPhone();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(dialIntent);
            }
        });

        fStore.collection("Doctors").document(currentRequest.getDoctorUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){return;}
                if (value != null ){
                    if (value.getString("Name")!=null && value.getString("FamillyName")!=null){
                        doctorName.setText("Dr."+value.getString("Name")+" "+value.getString("FamillyName"));
                    }
                }
            }
        });
        hospital.setText("Wilaya de "+currentRequest.getWilaya()+"\n"+"adress: "+currentRequest.getAdress()+"\n"+"Hospital name: "+currentRequest.getHospital()+"\n"+"Blood zomra : "+currentRequest.getBlood()+"\nneed : "+currentRequest.getNeed());




        return listItemView;
    }
}
