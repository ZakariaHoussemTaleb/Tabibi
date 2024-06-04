package com.example.tabibiapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class BellNotification extends AppCompatActivity {
    ListView listView;
    ArrayList<Notification> notifications;
    NotificationAdapter adapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell_notification);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        notifications = new ArrayList<Notification>();
        adapter = new NotificationAdapter(this, notifications);
        listView = findViewById(R.id.notificationListView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        fStore.collection("Notify")
                .whereEqualTo("UserUid",fAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                Notification notify = doc.getDocument().toObject(Notification.class);
                                notifications.add(notify);

                            }

                        }
                        SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");
                        Collections.sort(notifications, new Comparator<Notification>() {
                            @Override
                            public int compare(Notification rdv1, Notification rdv2) {
                                try {
                                    Date date1 =sdfN.parse(rdv1.getTime());
                                    Date date2 =sdfN.parse(rdv2.getTime());
                                    return date1.compareTo(date2);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });


    }
}