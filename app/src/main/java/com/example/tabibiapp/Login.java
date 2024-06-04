package com.example.tabibiapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsAnimationControllerCompat;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText lPassword,lEmail;
    String nature="default";
    ProgressBar lProgressbar;
    private FirebaseAuth lAuth;
    private FirebaseFirestore fstore;
    ImageView buttonGoogleSignUp;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        Log.w(TAG, "admin");
        nature = getIntent().getStringExtra("newDoctor");
        lEmail = findViewById(R.id.editTextText);
        lPassword = findViewById(R.id.editTextText3);
        lProgressbar = findViewById(R.id.progressBar2);
        register = findViewById(R.id.textView4);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nature != null){
                if (nature.equals("newDoctor")){
                    Intent intent = new Intent(Login.this,SignUp.class);
                    intent.putExtra("newDoctor","newDoctor");
                    startActivity(intent);
                    finish();
                }}else {
                    Intent intent = new Intent(Login.this,SignUp.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        Log.d(TAG, "cbon zaki");
        if (lAuth.getCurrentUser() != null){
            updateUI(lAuth.getCurrentUser());
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth

        // [END initialize_auth]

        buttonGoogleSignUp = findViewById(R.id.imageView25);
        buttonGoogleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithGoogle();
            }
        });

        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/




        Log.d(TAG, "cbon zaki");
    }


    private void signUpWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = lAuth.getCurrentUser();
        //Toast.makeText(this, currentUser.getUid(), Toast.LENGTH_SHORT).show();
        //updateUIg(currentUser);
    }

    private void updateUIg(FirebaseUser user) {
        if (user!=null){
            if (nature!=null){
            if (nature.equals("newDoctor")){
                Intent intent = new Intent(Login.this,NewDoctorRegistration.class);
                intent.putExtra("newDoctor","newDoctor");
                startActivity(intent);
                finish();
                return;
            }}
            String uid = user.getUid();
            DocumentReference df = fstore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isDoctor")!=null){
                        Intent intent = new Intent(getApplicationContext(),DoctorMain.class);
                        startActivity(intent);
                        finish();

                    }else if (documentSnapshot.getString("isAdmin")!=null){
                        Intent intent = new Intent(getApplicationContext(),Admin.class);
                        startActivity(intent);
                        finish();
                    } else {
                    Intent intent = new Intent(getApplicationContext(),MaladeMain.class);
                    startActivity(intent);
                    finish();}
                }
            });
        }
        else {
            lProgressbar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        lAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = lAuth.getCurrentUser();
                            if (user != null){
                                DocumentReference df = fstore.collection("Users").document(user.getUid());
                                df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value != null && value.exists()) {
                                            String email = value.getString("Email");
                                        if (email==null){
                                            Map<String,Object> userInfo = new HashMap<>();
                                            userInfo.put("UserName",user.getDisplayName());
                                            userInfo.put("Email",user.getEmail());
                                            userInfo.put("Password","Google sign up");
                                            userInfo.put("isUser","1");
                                            userInfo.put("isDoctor",null);
                                            userInfo.put("blood",null);
                                            df.set(userInfo);}
                                    }}
                                });

                                lProgressbar.setVisibility(View.VISIBLE);
                                updateUIg(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUIg(null);
                        }
                    }
                });
    }




    public void register(View v){
        Log.d(TAG, "cbon zaki");
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
        finish();
    }
    public void areYouDoctor(View v){
        Intent intent = new Intent(this,AreYouAdoctor.class);
        startActivity(intent);
    }

    public void login(View v){
        String email = lEmail.getText().toString().trim();
        String password = lPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            lEmail.setError("email is required");
            return;
        }
        if (TextUtils.isEmpty(password)){
            lPassword.setError("password is required");
            return;
        }
        lProgressbar.setVisibility(View.VISIBLE);

        lAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (lAuth.getCurrentUser().isEmailVerified()){
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = lAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else {
                                Toast.makeText(Login.this, "confirm your email.",
                                        Toast.LENGTH_SHORT).show();
                                updateUIc();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user!=null){
            if (nature!=null){
                if (nature.equals("newDoctor")){
                    Intent intent = new Intent(Login.this,NewDoctorRegistration.class);
                    intent.putExtra("newDoctor","newDoctor");
                    startActivity(intent);
                    finish();
                    return;
                }}
            String uid = user.getUid();
            DocumentReference df = fstore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isDoctor")!=null){
                        Intent intent = new Intent(getApplicationContext(),DoctorMain.class);
                        startActivity(intent);
                        finish();

                    }else if (documentSnapshot.getString("isAdmin")!=null){
                        Toast.makeText(Login.this, "admin", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "admin");
                        Intent intent = new Intent(getApplicationContext(),Admin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(),MaladeMain.class);
                        startActivity(intent);
                        finish();}
                }

            });

        }
        else {
            lPassword.setError("password wrong");
            lEmail.setError("email wrong");
            lProgressbar.setVisibility(View.INVISIBLE);}
    }
    private void updateUIc(){
        lProgressbar.setVisibility(View.INVISIBLE);
    }


}