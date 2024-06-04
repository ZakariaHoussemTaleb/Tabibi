package com.example.tabibiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MaladeRDVAdapter extends ArrayAdapter<ListDesRendezVous> {
    private Context context;
    private ArrayList<ListDesRendezVous> listDesRDV;
    String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public MaladeRDVAdapter(Context context, ArrayList<ListDesRendezVous> listDesRDV) {
        super(context, 0, listDesRDV);
        this.context = context;
        this.listDesRDV = listDesRDV;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.confirmed_rdv_item, parent, false);
        }
        ListDesRendezVous currentRDV = listDesRDV.get(position);
        TextView doctorsname = listItemView.findViewById(R.id.DoctorNameRDV);
        TextView doctorspeciality = listItemView.findViewById(R.id.SpecialityRDV);
        TextView phonet = listItemView.findViewById(R.id.Phone);
        TextView from = listItemView.findViewById(R.id.FROM);
        TextView to = listItemView.findViewById(R.id.TO);

        ImageView imageView = listItemView.findViewById(R.id.imageView15);
        String etat = currentRDV.etat;
        if (etat.equals("0")){
            imageView.setImageResource(R.drawable.pending);
        } else if (etat.equals("1")) {
            imageView.setImageResource(R.drawable.confirmed);

        } else if (etat.equals("-1")) {
            imageView.setImageResource(R.drawable.cancel);

        }else if (etat.equals("done")) {
            imageView.setImageResource(R.drawable.done);

        }
        DocumentReference df = db.collection("Doctors").document(currentRDV.getDoctorUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name = "Dr."+documentSnapshot.getString("Name")+""+documentSnapshot.getString("FamillyName");
                String specialit = documentSnapshot.getString("Speciality");
                String phone = documentSnapshot.getString("Phone");
                doctorsname.setText(name);
                doctorspeciality.setText(specialit);
                phonet.setText(phone);
                from.setText(currentRDV.getDateStart());
                to.setText(currentRDV.getDateFinish());


                notifyDataSetChanged();
            }
        });


        phonet.setTextIsSelectable(true);


        return listItemView;
    }


}


