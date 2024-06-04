package com.example.tabibiapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class Adapter extends ArrayAdapter<Doctor> {
    private Context context;
    private ArrayList<Doctor> doctors;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    public Adapter(Context context, ArrayList<Doctor> doctors) {
        super(context, 0, doctors);
        this.context = context;
        this.doctors = doctors;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        }
        Doctor currentDoctor = doctors.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        ImageView imageView = listItemView.findViewById(R.id.imageView722);
        StorageReference profileRef = storageReference.child("Users/"+ currentDoctor.Uid+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        TextView nameTextView = listItemView.findViewById(R.id.textView252);
        nameTextView.setText("Dr."+currentDoctor.getName()+""+currentDoctor.getFamillyName());
        TextView specialiteTextView = listItemView.findViewById(R.id.textView262);
        specialiteTextView.setText(currentDoctor.getSpeciality());
        TextView rating = listItemView.findViewById(R.id.textView272);
        rating.setText(String.valueOf(currentDoctor.getRating()));
        notifyDataSetChanged();
        Button info = listItemView.findViewById(R.id.button7);
        if (context instanceof DoctorsListView){
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Info.class);
                intent.putExtra("name","Dr."+currentDoctor.getName()+""+currentDoctor.getFamillyName());
                intent.putExtra("Uid",currentDoctor.getUid());
                //intent.putExtra("doctor",currentDoctor);*/
                context.startActivity(intent);
            }
        });} else if (context instanceof Rating) {
            info.setText("Rate");
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,AddComent.class);
                    intent.putExtra("name","Dr."+currentDoctor.getName()+""+currentDoctor.getFamillyName());
                    intent.putExtra("Uid",currentDoctor.getUid());
                    //intent.putExtra("doctor",currentDoctor);*/
                    context.startActivity(intent);
                }
            });
        }
        return listItemView;
    }
}
