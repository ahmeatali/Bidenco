package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaticiUrunEkleme extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Bitmap selectedImage;
    ImageView imageView;
    EditText productName;
    EditText commandText;
    EditText priceText;
    Uri imageData;
    String userID;
    Spinner spinner;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String item;

    public HashMap<String, Object> postData = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satici_urun_ekleme);

        imageView = findViewById(R.id.addImage);
        productName = findViewById(R.id.productName);
        commandText = findViewById(R.id.productCommand);
        spinner = findViewById(R.id.spinner);
        priceText= findViewById(R.id.priceText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance().getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dataGetFromFirestore();

        StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");


    }



    public void dataGetFromFirestore (){

        userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    String businessName = value.getString("businessName");
                    String eMail =  value.getString("email");
                    String nameSeller = value.getString("name");
                    String surnameSeller = value.getString("surname");
                    String phoneSeller = value.getString("phone");
                    String vergiDairesi = value.getString("vergiDairesi");
                    String vergiNo = value.getString("vergiNo");

                    postData.put("businessName", businessName);
                    postData.put("email", eMail);
                    postData.put("sellerName",nameSeller);
                    postData.put("sellerSurname",surnameSeller);
                    postData.put("phoneNumber",phoneSeller);
                    postData.put("vergiDairesi",vergiDairesi);
                    postData.put("vergiNo", vergiNo);

                }else{
                    Log.d("tag", "onEvent:Document do not exists");
                }

            }
        });
    }


        public void addProduct(View view){

        if (imageData != null) {

            UUID uuid = UUID.randomUUID();
            final String imageName = "images/productPhoto/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl= uri.toString();

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();
                            String name  = productName.getText().toString();
                            String command = commandText.getText().toString();
                            String categories = item;
                            String price = priceText.getText().toString();

                            postData.put("userEmail", userEmail);
                            postData.put("downloadUrl",downloadUrl);
                            postData.put("product_name", name);
                            postData.put("product_command", command);
                            postData.put("date", FieldValue.serverTimestamp());
                            postData.put("categories",categories);
                            postData.put("price",price);

                            dataGetFromFirestore();

                            firebaseFirestore.collection("Post").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {


                                    Intent intent = new Intent(SaticiUrunEkleme.this,SaticiFeedActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SaticiUrunEkleme.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(SaticiUrunEkleme.this,e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent intentToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGalery,2);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intentToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGalery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if(requestCode==2 && resultCode == RESULT_OK && data!=null){

             imageData =  data.getData();

             try {

                 if (Build.VERSION.SDK_INT >= 28){
                     ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                     selectedImage= ImageDecoder.decodeBitmap(source);
                     imageView.setImageBitmap(selectedImage);
                 }else{
                     selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                     imageView.setImageBitmap(selectedImage);
                 }


             } catch (IOException e) {
                 e.printStackTrace();
             }

         }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}