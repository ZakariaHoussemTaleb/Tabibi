package com.example.tabibiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RondezVous extends AppCompatActivity {

    int rdvJour,size,sh,fh;
    private RdvAdapter adapter;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    public ArrayList<ListDesRendezVous> listDesRDV;
    public ArrayList<LocalDateTime> listD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondez_vous);
        String Uid = getIntent().getStringExtra("Uid");
        String Description = getIntent().getStringExtra("Description");


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        listD=new ArrayList<LocalDateTime>();
        listDesRDV = new ArrayList<ListDesRendezVous>();
        TextView textView=findViewById(R.id.textView40);









                        ArrayList<LocalDateTime> listD = new ArrayList<LocalDateTime>();


                        fStore.collection("RDV")
                                .whereEqualTo("DoctorUid", Uid)
                                .whereEqualTo("etat", "0")
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

                                            fStore.collection("Doctors").document(Uid).get()
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
                                                                    lastRDVFinish = lastRDVFinish.plusDays(1).withHour(8).withMinute(0).withSecond(0);
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

                                                                }

                                                                Date date = Date.from(lastRDVFinish.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
                                                                Date dateStart = Date.from(lastRDVFinish.atZone(ZoneId.systemDefault()).toInstant());



                                                                String formattedDate = sdf.format(date);
                                                                String formattedDateS = sdf.format(dateStart);
                                                                Map<String,Object> rdvInfo = new HashMap<>();
                                                                rdvInfo.put("DateFinish",formattedDate);
                                                                rdvInfo.put("DateStart",formattedDateS);
                                                                rdvInfo.put("DoctorUid",Uid);
                                                                rdvInfo.put("UserUid",fAuth.getCurrentUser().getUid());
                                                                rdvInfo.put("Description",Description);
                                                                rdvInfo.put("DateDepo",new Timestamp(new Date()));
                                                                rdvInfo.put("etat","0");//0 sma panding 1 sma tvalida -1 sma trefusa
                                                                fStore.collection("RDV")
                                                                        .add(rdvInfo);



                                                                textView.setText("your apointment is seted from :\n"+formattedDateS+" to :\n"+formattedDate+" day of :\n"+lastRDVFinish.getDayOfWeek());




                                                                listD.clear();

                                                            }
                                                            else {
                                                                fStore.collection("RDV")
                                                                        .whereEqualTo("DoctorUid", Uid)
                                                                        .whereEqualTo("etat", "1")
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

                                                                                    fStore.collection("Doctors").document(Uid).get()
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
                                                                                                            lastRDVFinish = lastRDVFinish.plusDays(1).withHour(8).withMinute(0).withSecond(0);
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

                                                                                                        }

                                                                                                        Date date = Date.from(lastRDVFinish.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
                                                                                                        Date dateStart = Date.from(lastRDVFinish.atZone(ZoneId.systemDefault()).toInstant());



                                                                                                        String formattedDate = sdf.format(date);
                                                                                                        String formattedDateS = sdf.format(dateStart);
                                                                                                        Map<String,Object> rdvInfo = new HashMap<>();
                                                                                                        rdvInfo.put("DateFinish",formattedDate);
                                                                                                        rdvInfo.put("DateStart",formattedDateS);
                                                                                                        rdvInfo.put("DoctorUid",Uid);
                                                                                                        rdvInfo.put("UserUid",fAuth.getCurrentUser().getUid());
                                                                                                        rdvInfo.put("Description",Description);
                                                                                                        rdvInfo.put("DateDepo",new Timestamp(new Date()));
                                                                                                        rdvInfo.put("etat","0");//0 sma panding 1 sma tvalida -1 sma trefusa
                                                                                                        fStore.collection("RDV")
                                                                                                                .add(rdvInfo);



                                                                                                        textView.setText("your apointment is seted from :\n"+formattedDateS+" to :\n"+formattedDate+" day of :\n"+lastRDVFinish.getDayOfWeek());




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
                                                                                                        Date datefinish = Date.from(now.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
                                                                                                        String formattedDateF = sdf.format(datefinish);

                                                                                                        Map<String,Object> rdvInfo = new HashMap<>();
                                                                                                        rdvInfo.put("DateFinish",formattedDateF);
                                                                                                        rdvInfo.put("DateStart",formattedDateS);
                                                                                                        rdvInfo.put("DoctorUid",Uid);
                                                                                                        rdvInfo.put("UserUid",user.getUid());
                                                                                                        rdvInfo.put("Description",Description);
                                                                                                        rdvInfo.put("DateDepo",new Timestamp(new Date()));
                                                                                                        rdvInfo.put("etat","0");//0 sma panding 1 sma tvalida -1 sma trefusa
                                                                                                        fStore.collection("RDV")
                                                                                                                .add(rdvInfo)
                                                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                                                        //Toast.makeText(RondezVous.this, "Data stored initial", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                                    @Override
                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                        // Toast.makeText(RondezVous.this, "DATA Failed", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                });




                                                                                                        listD.clear();
                                                                                                        textView.setText("your apointment is seted from :\n"+formattedDateS+" to :\n"+formattedDateF);

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }


                                                                            }
                                                                        }); }
                                                        }
                                                    });
                                        }


                                    }
                                });

    }
    public void mainmenub(View v){
        Intent intent = new Intent(this, MaladeMain.class);
        startActivity(intent);
        finish();
    }

        /*arrayOfRDV(Uid, new OnDataReady() {
            @Override
            public void onDataReady(ArrayList<ListDesRendezVous> listDesRDV) {
                if (listDesRDV.size() > 0) {
                    size = listDesRDV.size() - 1;
                    ListDesRendezVous rdv = listDesRDV.get(size);

                    FirebaseUser user = fAuth.getCurrentUser();

                    DocumentReference df = fStore.collection("RDV").document(Uid);
                    Map<String,Object> rdvInfo = new HashMap<>();
                    rdvInfo.put("DateFinish",rdv.getDateFinish()+1);
                    rdvInfo.put("DateStart",rdv.getDateFinish());
                    rdvInfo.put("DoctorUid",Uid);
                    rdvInfo.put("UserUid",user.getUid());
                    df.set(rdvInfo);
                    listDesRDV.clear();
                    textView.setText("your apointment is seted from"+Integer.toString(rdv.getDateFinish())+"to"+Integer.toString(rdv.getDateFinish()+1));


                } else {
                    Toast.makeText(RondezVous.this, "No appointments found", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = fAuth.getCurrentUser();

                    fh=8;sh=7;
                    DocumentReference df = fStore.collection("RDV").document(Uid);
                    Map<String,Object> rdvInfo = new HashMap<>();
                    rdvInfo.put("DateFinish",9);
                    rdvInfo.put("DateStart",8);
                    rdvInfo.put("DoctorUid",Uid);
                    rdvInfo.put("UserUid",user.getUid());
                    df.set(rdvInfo);
                    listDesRDV.clear();
                    textView.setText("your apointment is seted from"+Integer.toString(sh)+"to"+Integer.toString(fh));

                }
            }
        });
        sh=8;
        fh=9;*/


        /*if (listDesRDV!=null) {
            size = listDesRDV.size() - 1;
            ListDesRendezVous rdv = listDesRDV.get(size);
            sh = rdv.getDateFinish();
            fh = sh + 1;

        }

        FirebaseUser user = fAuth.getCurrentUser();

        DocumentReference df = fStore.collection("RDV").document(Uid);
        Map<String,Object> rdvInfo = new HashMap<>();
        rdvInfo.put("DateFinish",fh);
        rdvInfo.put("DateStart",sh);
        rdvInfo.put("DoctorUid",Uid);
        rdvInfo.put("UserUid",user.getUid());
        df.set(rdvInfo);*/




            /*for (int i=0;i< listDesRDV.size();i++){
                if (listDesRDV.get(i).date.equals(d)){rdvJour++;}
            }
            if (rdvJour==15){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                calendar.add(Calendar.DATE, 1);
                Date nextDay = calendar.getTime();
                ListDesRendezVous newRDV= new ListDesRendezVous(nextDay,8,9,"zaki");
                list.add(newRDV);
            }
            else {
                ListDesRendezVous newRDV= new ListDesRendezVous(d,sh,fh,"zaki");
                list.add(newRDV);
            }
        }
        else {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            Date appointmentDate = Date.from(currentDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            int currentHour = currentTime.getHour()+1;
            int fh = currentHour+1;
            ListDesRendezVous newRDV= new ListDesRendezVous(appointmentDate,currentHour,fh,"zaki");
            list.add(newRDV);
        }
        doctorsProfile.setListDesRendezVous(list);


        adapter = new RdvAdapter(this,listDesRDV);
        ListView listView = findViewById(R.id.listView2);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/



    /*public void arrayOfRDV(String Uid/*, OnDataReady callback) {
        fStore.collection("RDV")
                .whereEqualTo("DoctorUid", Uid)
                //.orderBy("DateFinish", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()){
                                int date = Integer.parseInt(doc.get("DateFinish").toString());
                                listD.add(date);
                                Toast.makeText(RondezVous.this, listD.get(0).toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RondezVous.this, "Failed geting data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }*/





                /*.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(RondezVous.this, "error.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                listDesRDV.add(doc.getDocument().toObject(ListDesRendezVous.class));
                            }
                        }
                        if (callback != null) {
                            callback.onDataReady(listDesRDV); // Call the callback with data
                        }
                    }
                });
    }*/





}