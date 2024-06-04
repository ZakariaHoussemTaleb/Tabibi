package com.example.tabibiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private Context context;
    private ArrayList<Notification> notifications;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        }
        Notification currentNotification = notifications.get(position);

        ImageView imageView = listItemView.findViewById(R.id.imageView722noti);
        TextView title = listItemView.findViewById(R.id.textView252noti);
        TextView body = listItemView.findViewById(R.id.textView262noti);
        TextView time = listItemView.findViewById(R.id.textView76);

        time.setText(currentNotification.getTime());
        title.setText(currentNotification.getTitle());
        body.setText(currentNotification.getBody());
        setImage();


        return listItemView;
    }

    private void setImage() {
    }
}