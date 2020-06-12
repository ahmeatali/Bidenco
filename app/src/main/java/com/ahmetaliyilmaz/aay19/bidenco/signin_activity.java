package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signin_activity extends AppCompatActivity {
//call firebase
    private FirebaseAuth firebaseAuth;
//call text and password from activity signin xml
    EditText emailText, passwordText;
//create activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

//define firebase,e-mail and text
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
//call back user information
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(signin_activity.this,feed_activity.class);
            startActivity(intent);
            finish();
        }
    }
//define sign in click
    public void signInClicked (View view){
//export to string email and password information
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
//define firebase in signin clicked and check to success
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
//if it is success, open feed activity
                Intent intent = new Intent (signin_activity.this, feed_activity.class);
                startActivity(intent);
                finish();
// if it is fail, open pop-up message
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(signin_activity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }

    //with post button open signup activity
    public void postButton (View view){

         Intent intent = new Intent(signin_activity.this,signup_activity.class);
         startActivity(intent);

    }


}

