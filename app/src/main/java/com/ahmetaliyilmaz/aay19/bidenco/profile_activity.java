package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class profile_activity extends AppCompatActivity {

    TextView sname, ssurname, sbusinessName, svergiNo, svergiDairesi, sphone, semail;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sname = findViewById(R.id.profileName);
        ssurname = findViewById(R.id.profileSurname);
        sbusinessName = findViewById(R.id.profileBusinessName);
        svergiNo = findViewById(R.id.profileVergiNo);
        svergiDairesi = findViewById(R.id.profileVergiDairesi);
        sphone = findViewById(R.id.profilePhone);
        semail = findViewById(R.id.profileEmail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                sname.setText(documentSnapshot.getString("name"));
                ssurname.setText(documentSnapshot.getString("surname"));
                sbusinessName.setText(documentSnapshot.getString("businessName"));
                svergiNo.setText(documentSnapshot.getString("vergiNo"));
                svergiDairesi.setText(documentSnapshot.getString("vergiDairesi"));
                sphone.setText(documentSnapshot.getString("phone"));
                semail.setText(documentSnapshot.getString("email"));
            }
        });
    }
}