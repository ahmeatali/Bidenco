package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SaticiProfileActivity extends AppCompatActivity {

    TextView sname, ssurname, sbusinessName, svergiNo, svergiDairesi, sphone, semail;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button changeProfileImage;
    ImageView profileImage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satici_profile);

        sname = findViewById(R.id.profileName);
        ssurname = findViewById(R.id.profileSurname);
        sbusinessName = findViewById(R.id.profileBusinessName);
        svergiNo = findViewById(R.id.profileVergiNo);
        svergiDairesi = findViewById(R.id.profileVergiDairesi);
        sphone = findViewById(R.id.profilePhone);
        semail = findViewById(R.id.profileEmail);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    sname.setText(documentSnapshot.getString("name"));
                    ssurname.setText(documentSnapshot.getString("surname"));
                    sbusinessName.setText(documentSnapshot.getString("businessName"));
                    svergiNo.setText(documentSnapshot.getString("vergiNo"));
                    svergiDairesi.setText(documentSnapshot.getString("vergiDairesi"));
                    sphone.setText(documentSnapshot.getString("phone"));
                    semail.setText(documentSnapshot.getString("email"));
                } else {
                    Log.d("tag", "onEvent:Document do not exists");
                }
            }
        });
        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SaticiEditProfileActivity.class);
                i.putExtra("name", sname.getText().toString());
                i.putExtra("surname", ssurname.getText().toString());
                i.putExtra("businessName", sbusinessName.getText().toString());
                i.putExtra("vergiNo", svergiNo.getText().toString());
                i.putExtra("vergiDairesi", svergiDairesi.getText().toString());
                i.putExtra("phone", sphone.getText().toString());
                i.putExtra("email", semail.getText().toString());

                startActivity(i);
            }
        });
    }
}