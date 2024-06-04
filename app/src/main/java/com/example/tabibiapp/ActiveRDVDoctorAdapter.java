package com.example.tabibiapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActiveRDVDoctorAdapter extends ArrayAdapter<ListDesRendezVous> {
    private Context context;
    private ArrayList<ListDesRendezVous> rdvs;
    public FirebaseFirestore fStore;
    StorageReference storageReference;

    public ActiveRDVDoctorAdapter(Context context, ArrayList<ListDesRendezVous> rdvs) {
        super(context, 0, rdvs);
        this.context = context;
        this.rdvs = rdvs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.activ_rdv_doctor_item, parent, false);
        }
        ListDesRendezVous currentRDV = rdvs.get(position);

        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        ImageView imageProfile = listItemView.findViewById(R.id.imageView811);
        ImageView done = listItemView.findViewById(R.id.done);
        ImageView skip = listItemView.findViewById(R.id.skip);
        TextView maladename = listItemView.findViewById(R.id.maladeName);
        TextView from = listItemView.findViewById(R.id.from);
        TextView to = listItemView.findViewById(R.id.to);
        if (position==0){done.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);}

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMedicalRecord = new Intent(context, MedicalRecordListView.class);
                showMedicalRecord.putExtra("natur","doctor");
                showMedicalRecord.putExtra("UserUid",currentRDV.getUserUid());
                context.startActivity(showMedicalRecord);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                markAsDone(currentRDV);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsSkipe(currentRDV);

            }
        });


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("Users/"+currentRDV.getUserUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfile);
            }
        });

        fStore.collection("Users").document(currentRDV.getUserUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            String userName = doc.getString("UserName");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            try {
                                Date date = sdf.parse(currentRDV.getDateStart());
                                LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                                from.setText(localDateTime.getDayOfWeek()+" \n"+"From : "+currentRDV.getDateStart());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }


                            maladename.setText("Mr."+userName);
                            to.setText("To : "+currentRDV.getDateFinish());
                            notifyDataSetChanged();

                        }
                    }
                });



        return listItemView;
    }

    private void markAsSkipe(ListDesRendezVous currentRDV) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("are you sure you want to skip this apointment?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fStore.collection("RDV")
                        //.whereEqualTo("DateStart",currentRDV.getDateStart())
                        .whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                        .whereEqualTo("UserUid",currentRDV.getUserUid())
                        .whereEqualTo("DateDepo",currentRDV.getDateDepo())
                        //.whereEqualTo("DateFinish",currentRDV.getDateFinish())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    String docID = task.getResult().getDocuments().get(0).getId();
                                    Map<String,Object> skiped = new HashMap<>();
                                    skiped.put("etat","skip");
                                    fStore.collection("RDV")
                                            .document(docID).update(skiped);
                                    fStore.collection("ConfirmedRDV")
                                            // .whereEqualTo("DateStart",currentRDV.getDateStart())
                                            .whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                                            .whereEqualTo("UserUid",currentRDV.getUserUid())
                                            .whereEqualTo("DateDepo",currentRDV.getDateDepo())
                                            //.whereEqualTo("DateFinish",currentRDV.getDateFinish())
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        String docID = task.getResult().getDocuments().get(0).getId();
                                                        fStore.collection("ConfirmedRDV")
                                                                .document(docID).delete();

                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                    notifyDataSetChanged();
                                }
                            }
                        });


                fStore.collection("Doctors").document(currentRDV.getDoctorUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            String drfullName = task.getResult().getString("FamillyName");
                            String drName = task.getResult().getString("Name");

                            SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");
                            Map<String,Object> newNotification = new HashMap<>();
                            newNotification.put("title","Apointment Skiped");
                            newNotification.put("DoctorUid",currentRDV.getDoctorUid());
                            newNotification.put("body","Dr."+drfullName+" "+drName+" skiped your apointment");
                            newNotification.put("UserUid",currentRDV.getUserUid());
                            newNotification.put("time",sdfN.format(new Date()));
                            newNotification.put("etas","not yet");

                            fStore.collection("Notify").add(newNotification);
                        }
                    }
                });

                fStore.collection("ConfirmedRDV")
                        .orderBy("DateDepo", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    Date NextapointmentStart = null;
                                    Date Nextapointmentfinish = null;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date currentDateStart = null;
                                    Date currentDatefinish = null;
                                    try {
                                        currentDateStart = sdf.parse(currentRDV.getDateStart());
                                        currentDatefinish = sdf.parse(currentRDV.getDateFinish());
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    for (DocumentSnapshot doc :task.getResult()){
                                        if (!doc.getString("etat").equals("1")) continue;
                                        String StringNextapointmentStart = doc.getString("DateStart");
                                        String StringNextapointmentfinish = doc.getString("DateFinish");
                                        try {
                                            NextapointmentStart = sdf.parse(StringNextapointmentStart);
                                            Nextapointmentfinish = sdf.parse(StringNextapointmentfinish);
                                            LocalDateTime nextRDVstart = LocalDateTime.ofInstant(NextapointmentStart.toInstant(), ZoneId.systemDefault());
                                            LocalDateTime nextRDVfinish = LocalDateTime.ofInstant(Nextapointmentfinish.toInstant(), ZoneId.systemDefault());
                                            LocalDateTime currentRDVstart = LocalDateTime.ofInstant(currentDateStart.toInstant(), ZoneId.systemDefault());
                                            LocalDateTime currentRDVfinish = LocalDateTime.ofInstant(currentDatefinish.toInstant(), ZoneId.systemDefault());
                                            Duration duration = Duration.between(currentRDVstart, currentRDVfinish);
                                            Duration durationCurentStarttoNextStart = Duration.between(currentRDVstart, nextRDVstart);//current start to next start
                                            Duration durationCFtoNF = Duration.between(nextRDVstart, nextRDVfinish);
                                            long durationMin = duration.toMinutes();
                                            long durationCStoNSMin = durationCurentStarttoNextStart.toMinutes();
                                            long durationCFtoNFMin = durationCFtoNF.toMinutes();
                                            if (nextRDVstart.isAfter(currentRDVstart) && nextRDVstart.toLocalDate().isEqual(currentRDVstart.toLocalDate())){
                                                LocalDateTime newStartTime= nextRDVstart.minusMinutes(durationMin);
                                                LocalDateTime newFinishTime= nextRDVfinish.minusMinutes(durationMin);

                                                Date dateStart = Date.from(newStartTime.atZone(ZoneId.systemDefault()).toInstant());
                                                Date datefinish = Date.from(newFinishTime.atZone(ZoneId.systemDefault()).toInstant());


                                                String newFinish = sdf.format(datefinish);
                                                String newStart = sdf.format(dateStart);






                                                Map<String,Object> rdvInfo = new HashMap<>();
                                                rdvInfo.put("DateFinish",newFinish);
                                                rdvInfo.put("DateStart",newStart);
                                                doc.getReference().update(rdvInfo);
                                                fStore.collection("RDV").whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                                                        .whereEqualTo("UserUid",currentRDV.getUserUid())
                                                        .whereEqualTo("DateDepo",currentRDV.getDateDepo())
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()){
                                                                    String docID = task.getResult().getDocuments().get(0).getId();
                                                                    fStore.collection("RDV")
                                                                            .document(docID).update(rdvInfo);

                                                                    notifyDataSetChanged();
                                                                }
                                                            }
                                                        });

                                                for (int i=0;i< rdvs.size(); i++){
                                                    if (rdvs.get(i).getDateStart().equals(StringNextapointmentStart) && rdvs.get(i).getDateFinish().equals(StringNextapointmentfinish)) {

                                                        ListDesRendezVous rdv = rdvs.get(i);
                                                        rdv.setDateStart(newStart);
                                                        rdv.setDateFinish(newFinish);
                                                        rdvs.remove(i);
                                                        rdvs.add(i, rdv);
                                                        break;}
                                                }

                                            }
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    rdvs.remove(currentRDV);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        notifyDataSetChanged();
    }

    private void markAsDone(ListDesRendezVous currentRDV)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("the patient apointments is finish?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Map<String,Object> rdvDone = new HashMap<>();
                rdvDone.put("etat","done");
                fStore.collection("RDV").whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                        .whereEqualTo("UserUid",currentRDV.getUserUid())
                        .whereEqualTo("etat","1")
                        .whereEqualTo("Description",currentRDV.getDescription())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    String docID = doc.getId();

                                    fStore.collection("RDV").document(docID)
                                            .update(rdvDone);
                                    notifyDataSetChanged();

                                }
                            }
                        });
                fStore.collection("ConfirmedRDV").whereEqualTo("DoctorUid",currentRDV.getDoctorUid())
                        .whereEqualTo("DateStart",currentRDV.getDateStart())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    String docID = task.getResult().getDocuments().get(0).getId();
                                    fStore.collection("ConfirmedRDV")
                                            .document(docID).delete();

                                    showCustomDialog(context,currentRDV);

                                    Toast.makeText(context, "done.", Toast.LENGTH_SHORT).show();
                                    rdvs.remove(currentRDV);
                                    notifyDataSetChanged();
                                }
                            }
                        });


                fStore.collection("Doctors").document(currentRDV.getDoctorUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            String drfullName = task.getResult().getString("FamillyName");
                            String drName = task.getResult().getString("Name");
                            SimpleDateFormat sdfN = new SimpleDateFormat("MM-dd HH:mm");

                            Map<String,Object> newNotification = new HashMap<>();
                            newNotification.put("title","Apointment Done");
                            newNotification.put("DoctorUid",currentRDV.getDoctorUid());
                            newNotification.put("body","Your appointment with Dr."+drfullName+" "+drName+" is compleated");
                            newNotification.put("UserUid",currentRDV.getUserUid());
                            newNotification.put("time",sdfN.format(new Date()));
                            newNotification.put("etas","not yet");

                            fStore.collection("Notify").add(newNotification);
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        notifyDataSetChanged();
    }

    private void showCustomDialog(Context context,ListDesRendezVous rdv) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custum_dialogue_uploading_images);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialogue));
        dialog.setCancelable(false);

        Button ok = dialog.findViewById(R.id.button1721);
        Button dialogueCancel = dialog.findViewById(R.id.button161);



        dialogueCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, UploadMedicalRecord.class);
                intent.putExtra("DoctorUid",rdv.getDoctorUid());
                intent.putExtra("UserUid",rdv.getUserUid());
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

}