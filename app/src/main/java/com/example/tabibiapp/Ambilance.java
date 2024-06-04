package com.example.tabibiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Ambilance extends AppCompatActivity {

    public ArrayList<String> nums;
    public ArrayAdapter<String> adapter;
    FirebaseFirestore fStore;
    int wilaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ambilance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nums = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this,R.layout.item_ambulence,R.id.textViewsimpleadapter,nums);
        ListView listView = findViewById(R.id.ablistview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneNumber = nums.get(position);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
            }
        });

        fStore = FirebaseFirestore.getInstance();

        TextView msg = findViewById(R.id.textView65);
        EditText numWilaya = findViewById(R.id.editTextTextPostalAddress);
        Button ok = findViewById(R.id.button10);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nums.clear();
                msg.setVisibility(View.INVISIBLE);
                ok.setBackgroundColor(getResources().getColor(R.color.color2));
                String numWilayaText = numWilaya.getText().toString();

                if (numWilayaText.isEmpty()) {
                    msg.setVisibility(View.VISIBLE);
                    msg.setText("Please enter a wilaya code");
                    msg.setTextColor(getResources().getColor(R.color.red));
                    return;
                }


                try {
                    wilaya = Integer.parseInt(numWilayaText);
                    if (wilaya>=1 && wilaya<=58) {
                        fechAmbulenceNum(wilaya);
                    } else {
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Code wilaya wrong, insert a code between 1-58");
                        msg.setTextColor(getResources().getColor(R.color.red));
                    }
                } catch (NumberFormatException e) {
                    msg.setVisibility(View.VISIBLE);
                    msg.setText("Code wilaya wrong insert a code between 1-58");
                    msg.setTextColor(getResources().getColor(R.color.red)); // change the text color to red
                }
            }
        });

    }

    private void fechAmbulenceNum(int wilaya) {
        nums.clear();
        fStore.collection("Ambilance")
                .whereEqualTo("wilaya",wilaya)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentChange doc : task.getResult().getDocumentChanges()){
                        nums.add(doc.getDocument().getString("num"));

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}