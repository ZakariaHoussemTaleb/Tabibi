package com.example.tabibiapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RdvAdapter extends ArrayAdapter<ListDesRendezVous> {
    private Context context;
    public int duration=30;
    private ArrayList<ListDesRendezVous> listDesRDV;
    String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public RdvAdapter(Context context, ArrayList<ListDesRendezVous> listDesRDV) {
        super(context, 0, listDesRDV);
        this.context = context;
        this.listDesRDV = listDesRDV;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.rdv_item, parent, false);
        }
        ListDesRendezVous currentRDV = listDesRDV.get(position);
        TextView nameTextView = listItemView.findViewById(R.id.textView30);
        TextView rating = listItemView.findViewById(R.id.textView32);
        TextView specialiteTextView = listItemView.findViewById(R.id.textView31);
        Button confirm = listItemView.findViewById(R.id.button12);
        Button refuse = listItemView.findViewById(R.id.button13);
        Button description = listItemView.findViewById(R.id.button15);
        DocumentReference df = db.collection("Users").document(currentRDV.getUserUid());


        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String description = currentRDV.getDescription();
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {

                       String un = documentSnapshot.getString("UserName");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Patient Name: "+un+"\n\nDescription:\n"+description);
                builder.setNeutralButton("set a duration of apointment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showTimePickerDialog(confirm);
                    }
                });
                builder.create().show();
                   }
        });
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to confirm this apointment with duration of: "+duration+" min?");
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {




                FirebaseUser user = fAuth.getCurrentUser();
                ArrayList<LocalDateTime> listD = new ArrayList<LocalDateTime>();


                db.collection("ConfirmedRDV")
                        .whereEqualTo("DoctorUid", user.getUid())
                        .whereEqualTo("etat","1")
                        //.orderBy("DateFinish", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        Date date = null;
                                        try {
                                            date = sdf.parse(doc.get("DateFinish").toString());
                                            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                                            listD.add(localDateTime);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    db.collection("Doctors").document(user.getUid()).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                int max = Integer.parseInt(documentSnapshot.getString("MaxAtDay"));
                                                LocalTime timeStart = LocalTime.parse(documentSnapshot.getString("TimeStartWorking"));
                                                LocalTime timeFinish = LocalTime.parse(documentSnapshot.getString("TimeFinishWorking"));

                                                int[][] dayOfWorks = new int[7][2];
                                                for (int i =0 ; i<7 ; i++){
                                                    dayOfWorks[i][0]=i+1;
                                                }
                                                //the first day of week is sunday
                                                if (documentSnapshot.getBoolean("Freesunday")){
                                                    dayOfWorks[6][1]=1;
                                                }else dayOfWorks[6][1]=0;
                                                if (documentSnapshot.getBoolean("Freemonday")){
                                                        dayOfWorks[0][1]=1;
                                                }else dayOfWorks[0][1]=0;
                                                if (documentSnapshot.getBoolean("Freetuesday")){
                                                        dayOfWorks[1][1]=1;
                                                }else dayOfWorks[1][1]=0;
                                                if (documentSnapshot.getBoolean("Freewednesday")){
                                                        dayOfWorks[2][1]=1;
                                                }else dayOfWorks[2][1]=0;
                                                if (documentSnapshot.getBoolean("Freethursday")){
                                                        dayOfWorks[3][1]=1;
                                                }else dayOfWorks[3][1]=0;
                                                if (documentSnapshot.getBoolean("Freefriday")){
                                                        dayOfWorks[4][1]=1;
                                                }else dayOfWorks[4][1]=0;
                                                if (documentSnapshot.getBoolean("Freesaturday")){
                                                        dayOfWorks[5][1]=1;
                                                }else dayOfWorks[5][1]=0;

                                    if (listD.size()> 0){
                                        LocalDateTime lastRDVFinish = null;
                                        int rdvInOneDay = 0;
                                        for (LocalDateTime date : listD) {
                                            if (lastRDVFinish == null || date.isAfter(lastRDVFinish)) {
                                                lastRDVFinish = date;
                                            }
                                        }
                                        for (LocalDateTime date : listD) {
                                            if (date.toLocalDate().isEqual(lastRDVFinish.toLocalDate())) {
                                                rdvInOneDay++;
                                            }

                                        }
                                        if (rdvInOneDay >= max){
                                            lastRDVFinish = lastRDVFinish.plusDays(1).withHour(timeStart.getHour()).withMinute(timeStart.getMinute()).withSecond(0);
                                        }
                                        if (lastRDVFinish.toLocalTime().isAfter(timeFinish) || lastRDVFinish.toLocalTime().equals(timeFinish)){
                                            lastRDVFinish = lastRDVFinish.plusDays(1).withHour(timeStart.getHour()).withMinute(timeStart.getMinute()).withSecond(0);
                                        }
                                        int day = lastRDVFinish.getDayOfWeek().getValue();
                                        int nextday = -1;
                                        if (dayOfWorks[day-1][1]==0){
                                            for (int k=day;k<7;k++){
                                            if (dayOfWorks[k][1]==1){
                                                nextday=dayOfWorks[k][0];
                                                break;
                                            }
                                            k=k%6;
                                            }
                                            lastRDVFinish = lastRDVFinish.with(TemporalAdjusters.next(DayOfWeek.of(nextday)));
                                            lastRDVFinish=lastRDVFinish.withHour(timeStart.getHour()).withMinute(timeStart.getMinute()).withSecond(0);

                                        }

                                        Date date = Date.from(lastRDVFinish.plusMinutes(duration).atZone(ZoneId.systemDefault()).toInstant());
                                        Date dateStart = Date.from(lastRDVFinish.atZone(ZoneId.systemDefault()).toInstant());



                                        String formattedDate = sdf.format(date);
                                        String formattedDateS = sdf.format(dateStart);
                                        Map<String,Object> rdvInfo = new HashMap<>();
                                        rdvInfo.put("DateFinish",formattedDate);
                                        rdvInfo.put("DateStart",formattedDateS);
                                        rdvInfo.put("DoctorUid",currentRDV.getDoctorUid());
                                        rdvInfo.put("UserUid",currentRDV.getUserUid());
                                        rdvInfo.put("Description",currentRDV.getDescription());
                                        rdvInfo.put("DateDepo",new Timestamp(new Date()));
                                        rdvInfo.put("etat","1");//0 sma panding 1 sma tvalida -1 sma trefusa
                                        db.collection("ConfirmedRDV")
                                                .add(rdvInfo);


                                        db.collection("Doctors").document(currentRDV.getDoctorUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){

                                                    String drfullName = task.getResult().getString("FamillyName");
                                                    String drName = task.getResult().getString("Name");
                                                    SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");

                                                    Map<String,Object> newNotification = new HashMap<>();
                                                    newNotification.put("title","Apointment Confirmation");
                                                    newNotification.put("DoctorUid",fAuth.getCurrentUser().getUid());
                                                    newNotification.put("body","Dr."+drfullName+" "+drName+" accept your apointment");
                                                    newNotification.put("UserUid",currentRDV.getUserUid());
                                                    newNotification.put("time",sdfN.format(new Date()));
                                                    newNotification.put("etas","not yet");

                                                    db.collection("Notify").add(newNotification);
                                                }
                                            }
                                        });



                                        db.collection("RDV")
                                                .whereEqualTo("DateStart",currentRDV.getDateStart())
                                                .whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                            String documentID = documentSnapshot.getId();
                                                            db.collection("RDV")
                                                                    .document(documentID)
                                                                    .update(rdvInfo);
                                                            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });



                                        listD.clear();

                                    }
                                    else {
                                        FirebaseUser user = fAuth.getCurrentUser();
                                        LocalDateTime now = LocalDateTime.now();
                                        if (now.toLocalTime().isAfter(timeFinish)){
                                            now = now.plusDays(1).withHour(timeStart.getHour()).withMinute(timeStart.getMinute()).withSecond(0);
                                        }
                                        int day = now.getDayOfWeek().getValue();
                                        int nextday = -1;
                                        if (dayOfWorks[day-1][1]==0){
                                            for (int k=day;k<7;k++){
                                                if (dayOfWorks[k][1]==1){
                                                    nextday=dayOfWorks[k][0];
                                                    break;
                                                }
                                                k=k%6;
                                            }
                                            now = now.with(TemporalAdjusters.next(DayOfWeek.of(nextday)));
                                            now=now.withHour(timeStart.getHour()).withMinute(timeStart.getMinute()).withSecond(0);

                                        }
                                        Date dateStart = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
                                        String formattedDateS = sdf.format(dateStart);
                                        Date datefinish = Date.from(now.plusMinutes(duration).atZone(ZoneId.systemDefault()).toInstant());
                                        String formattedDateF = sdf.format(datefinish);

                                        Map<String,Object> rdvInfo = new HashMap<>();
                                        rdvInfo.put("DateFinish",formattedDateF);
                                        rdvInfo.put("DateStart",formattedDateS);
                                        rdvInfo.put("DoctorUid",currentRDV.getDoctorUid());
                                        rdvInfo.put("UserUid",currentRDV.getUserUid());
                                        rdvInfo.put("Description",currentRDV.getDescription());
                                        rdvInfo.put("DateDepo",new Timestamp(new Date()));
                                        rdvInfo.put("etat","1");//0 sma panding 1 sma tvalida -1 sma trefusa
                                        db.collection("ConfirmedRDV")
                                                .add(rdvInfo);

                                        db.collection("Doctors").document(currentRDV.getDoctorUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){

                                                    String drfullName = task.getResult().getString("FamillyName");
                                                    String drName = task.getResult().getString("Name");
                                                    SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");

                                                    Map<String,Object> newNotification = new HashMap<>();
                                                    newNotification.put("title","Apointment Confirmation");
                                                    newNotification.put("DoctorUid",fAuth.getCurrentUser().getUid());
                                                    newNotification.put("body","Dr."+drfullName+" "+drName+" accept your apointment");
                                                    newNotification.put("UserUid",currentRDV.getUserUid());
                                                    newNotification.put("time",sdfN.format(new Date()));
                                                    newNotification.put("etas","not yet");

                                                    db.collection("Notify").add(newNotification);
                                                }
                                            }
                                        });


                                        db.collection("RDV")
                                                .whereEqualTo("DateStart",currentRDV.getDateStart())
                                                .whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                            String documentID = documentSnapshot.getId();
                                                            db.collection("RDV")
                                                                    .document(documentID)
                                                                    .update(rdvInfo);
                                                            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });




                                        listD.clear();

                                    }
                                    listDesRDV.remove(currentRDV);
                                    notifyDataSetChanged();
                                                }
                                            });
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

                notifyDataSetChanged();



            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> rdvRefused = new HashMap<>();
                rdvRefused.put("etat","-1");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to refuse it?");
                builder.setPositiveButton("refuse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                db.collection("RDV")
                        .whereEqualTo("DateStart",currentRDV.getDateStart())
                        .whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()){
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();
                                    db.collection("RDV")
                                            .document(documentID)
                                            .update(rdvRefused);
                                    Toast.makeText(context, "Deleated", Toast.LENGTH_SHORT).show();
                                    listDesRDV.remove(currentRDV);
                                    notifyDataSetChanged();
                                }
                            }
                        });

                        db.collection("Doctors").document(currentRDV.getDoctorUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){

                                    String drfullName = task.getResult().getString("FamillyName");
                                    String drName = task.getResult().getString("Name");
                                    SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");

                                    Map<String,Object> newNotification = new HashMap<>();
                                    newNotification.put("title","Apointment Refused");
                                    newNotification.put("DoctorUid",fAuth.getCurrentUser().getUid());
                                    newNotification.put("body","Dr."+drfullName+" "+drName+" refuse your apointment");
                                    newNotification.put("UserUid",currentRDV.getUserUid());
                                    newNotification.put("time",sdfN.format(new Date()));
                                    newNotification.put("etas","not yet");

                                    db.collection("Notify").add(newNotification);
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

                notifyDataSetChanged();

            }

        });



        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("UserName");
                specialiteTextView.setText(currentRDV.getDateStart());
                nameTextView.setText(name);
                rating.setText(currentRDV.getDateFinish());
            }
        });
        notifyDataSetChanged();



        return listItemView;
    }
    private void showTimePickerDialog(Button confirm) {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minuteOfHour) -> {
            // Calculate the duration in minutes
            int durationInMinutes = (hourOfDay * 60) + minuteOfHour;
            duration = durationInMinutes;
            confirm.performClick();
        }, hour, minute, true);
        timePickerDialog.setOnShowListener(dialog -> {
            Button okButton = timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE);
            okButton.setText("Confirm appointment");
        });
        timePickerDialog.show();
    }


}
