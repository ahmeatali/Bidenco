package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
*/
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class signin_activity extends AppCompatActivity {

   // private CallbackManager mCallbackManager;
    //call firebase
    private FirebaseAuth firebaseAuth;
    //call text and password from activity signin xml
    EditText emailText, passwordText;
    TextView forgot_password;
    SignInButton signInButton;
    int RC_SIGN_IN = 0;

    // private LoginButton loginButton;

    //   CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    //create activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //define firebase,e-mail and text
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        forgot_password = findViewById(R.id.forgot_password);

        signInButton = findViewById(R.id.sign_in_button);

        //      loginButton = findViewById(R.id.login_button);

        //    callbackManager = CallbackManager.Factory.create();

  /*      loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(signin_activity.this, feed_activity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

*/
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signin_activity.this, ResetPasswordActivity.class));
            }
        });
        //call back user information
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(signin_activity.this, feed_activity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //      callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(signin_activity.this, profile_activity.class);
            startActivity(intent);
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //define sign in click
    public void signInClicked(View view) {
        //export to string email and password information
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        //define firebase in signin clicked and check to success
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //if it is success, open feed activity
                Intent intent = new Intent(signin_activity.this, feed_activity.class);
                startActivity(intent);
                finish();
                // if it is fail, open pop-up message
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(signin_activity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    //with post button open signup activity
    public void postButton(View view) {
        Intent intent = new Intent(signin_activity.this, signup_activity.class);
        startActivity(intent);
    }
}