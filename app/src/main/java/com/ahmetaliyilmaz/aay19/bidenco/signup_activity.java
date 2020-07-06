package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup_activity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    EditText semailText, spasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        semailText = findViewById(R.id.semailText);
        spasswordText = findViewById(R.id.spasswordText);
    }

    public void signupClicked (View view){

        fAuth = FirebaseAuth.getInstance();
        String email = semailText.getText().toString();
        String password = spasswordText.getText().toString();

        fAuth.createUserWithEmailAndPassword(email,password)
                  .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(signup_activity.this,"User Created",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(signup_activity.this,feed_activity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(signup_activity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }
    public void preButton (View view){

        Intent intent = new Intent(signup_activity.this, signin_activity.class);
        startActivity(intent);

    }

}

