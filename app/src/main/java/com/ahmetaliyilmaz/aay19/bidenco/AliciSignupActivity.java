package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AliciSignupActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    EditText semailText, spasswordText;
    public static final String TAG = "TAG";
    String userID;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alici_signup);

        semailText = findViewById(R.id.semailText);
        spasswordText = findViewById(R.id.spasswordText);
    }

    public void signupClicked(View view) {

        fAuth = FirebaseAuth.getInstance();
        final String email = semailText.getText().toString();
        String password = spasswordText.getText().toString();
        final String type = "alici";

        fStore = FirebaseFirestore.getInstance();

        if (TextUtils.isEmpty(email)) {
            semailText.setError("Email boş bırakılamaz.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            spasswordText.setError("Şifre boş bırakılamaz.");
            return;
        }

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fuser = fAuth.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AliciSignupActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                    Toast.makeText(AliciSignupActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference doc = fStore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("type", type);


                    doc.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), AliciFeedActivity.class));
                } else {
                    Toast.makeText(AliciSignupActivity.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void preButton(View view) {

        Intent intent = new Intent(AliciSignupActivity.this, SigninActivity.class);
        startActivity(intent);

    }

    public void saticiUyelikGecis(View view) {
        Intent intent = new Intent(AliciSignupActivity.this, SaticiSignupActivity.class);
        startActivity(intent);
    }
}

