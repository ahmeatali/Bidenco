package com.ahmetaliyilmaz.aay19.bidenco;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SaticiSignupActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText sname, ssurname, sbusinessName, svergiNo, svergiDairesi, sphone, semail, spassword;
    Button btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satici_signup);

        sname = findViewById(R.id.name);
        ssurname = findViewById(R.id.surname);
        sbusinessName = findViewById(R.id.businessName);
        svergiNo = findViewById(R.id.vergiNo);
        svergiDairesi = findViewById(R.id.vergiDairesi);
        sphone = findViewById(R.id.phone);
        semail = findViewById(R.id.email);
        spassword = findViewById(R.id.password);
        btn = findViewById(R.id.signUpBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = sname.getText().toString();
                final String surname = ssurname.getText().toString();
                final String businessName = sbusinessName.getText().toString();
                final String vergiNo = svergiNo.getText().toString();
                final String vergiDairesi = svergiDairesi.getText().toString();
                final String phone = sphone.getText().toString();
                final String email = semail.getText().toString().trim();
                String password = spassword.getText().toString().trim();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SaticiSignupActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Toast.makeText(SaticiSignupActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference doc = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("surname", surname);
                            user.put("businessName", businessName);
                            user.put("vergiNo", vergiNo);
                            user.put("vergiDairesi", vergiDairesi);
                            user.put("email", email);
                            user.put("phone", phone);

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
                        } else {
                            Toast.makeText(SaticiSignupActivity.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}