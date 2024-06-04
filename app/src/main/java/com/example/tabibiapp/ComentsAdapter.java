package com.example.tabibiapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComentsAdapter extends ArrayAdapter<Coment> {
    private Context context;
    private ArrayList<Coment> coments;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;

    public ComentsAdapter(Context context, ArrayList<Coment> coments) {
        super(context, 0, coments);
        this.context = context;
        this.coments = coments;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_coment, parent, false);
        }
        Coment currentComment = coments.get(position);


        ImageView imageView = listItemView.findViewById(R.id.imageView722);
        StorageReference profileRef = storageReference.child("Users/"+currentComment.getUserUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        TextView namee = listItemView.findViewById(R.id.textView252);
        namee.setText(currentComment.getUserName());
        TextView come = listItemView.findViewById(R.id.textView262);
        come.setText(currentComment.getComment());
        TextView ratingf = listItemView.findViewById(R.id.textView272);
        ratingf.setText(currentComment.getRating());
        notifyDataSetChanged();


        return listItemView;
    }
}
