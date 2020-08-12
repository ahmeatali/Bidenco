package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup_activity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    EditText semailText, spasswordText;
    Button saticiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        semailText = findViewById(R.id.semailText);
        spasswordText = findViewById(R.id.spasswordText);

        saticiBtn = findViewById(R.id.saticiBtn);
        saticiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup_activity.this, SaticiSignupActivity.class));
            }
        });
    }

    public void signupClicked(View view) {

        fAuth = FirebaseAuth.getInstance();
        final String email = semailText.getText().toString();
        String password = spasswordText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            semailText.setError("Email boş bırakılamaz.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            spasswordText.setError("Şifre boş bırakılamaz.");
            return;
        }

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), feed_activity.class));
            finish();
        }

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(signup_activity.this, "Please Check Your E-mail for Verification", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup_activity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        startActivity(new Intent(getApplicationContext(), feed_activity.class));
    }

    public void preButton(View view) {
        startActivity(new Intent(signup_activity.this, signin_activity.class));
    }
}