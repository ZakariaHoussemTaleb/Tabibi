package com.example.tabibiapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText muserName,mpassWord,memail,mconfPassword;
    ProgressBar mprogressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    String nature="default";
    TextView have;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        Log.d(TAG, "signup zaki");
        muserName = findViewById(R.id.editTextText);
        mpassWord = findViewById(R.id.editTextText3);
        mconfPassword = findViewById(R.id.editTextText4);
        memail = findViewById(R.id.editTextText2);
        nature = getIntent().getStringExtra("newDoctor");
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        have = findViewById(R.id.textView2);

        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nature!=null){
                    Intent intent = new Intent(SignUp.this,Login.class);
                    intent.putExtra("newDoctor","newDoctor");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SignUp.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mprogressBar = findViewById(R.id.progressBar);



    }



    public void Signup(View v){
        String email = memail.getText().toString().trim();
        String password = mpassWord.getText().toString().trim();
        String confPassword = mconfPassword.getText().toString().trim();
        String userName = muserName.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            memail.setError("email is required");
            return;
        }
        if (TextUtils.isEmpty(userName)){
            muserName.setError("user name is required");
            return;
        }
        if (TextUtils.isEmpty(password)){
            mpassWord.setError("password is required");
            return;
        }
        if (password.length()<8){
            mpassWord.setError("password very short");
            return;
        }
        if (!password.equals(confPassword)){
            mconfPassword.setError("password false");
            return;
        }
        mprogressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){

                               Log.d(TAG, "createUserWithEmail:success");
                               FirebaseUser user = auth.getCurrentUser();

                               DocumentReference df = fstore.collection("Users").document(user.getUid());
                               Map<String,Object> userInfo = new HashMap<>();
                               userInfo.put("UserName",userName);
                               userInfo.put("Email",email);
                               userInfo.put("Password",password);
                               userInfo.put("isUser","1");
                               userInfo.put("isDoctor",null);
                               userInfo.put("blood",null);
                               df.set(userInfo);


                               updateUI(user);
                           }
                           else {
                               Log.w(TAG, "createUserWithEmail:failure", task.getException());
                               Toast.makeText(SignUp.this, "please verify your email than log in.",
                                       Toast.LENGTH_LONG).show();
                               updateUI(null);
                           }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUp.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });



    }
    private void updateUI(FirebaseUser user) {
        if (user!=null){
            if (nature!=null){
                Intent intent = new Intent(SignUp.this,NewDoctorRegistration.class);
                intent.putExtra("newDoctor","newDoctor");
                startActivity(intent);
                finish();
                return;
            }
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();}
        else {
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
    }
    public void areYouDoctors(View v){
        Intent intent = new Intent(this,AreYouAdoctor.class);
        startActivity(intent);
        finish();
    }
}