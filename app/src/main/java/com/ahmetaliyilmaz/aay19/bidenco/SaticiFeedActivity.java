package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class SaticiFeedActivity extends AppCompatActivity {
    //call firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;

    ArrayList<String> userNameFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    SaticiFeedRecyclerAdapter saticiFeedRecyclerAdapter;


    //create option menu for exit
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.satici_options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //define items of menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_product) {

            Intent intenttoAddProduct = new Intent(SaticiFeedActivity.this, SaticiUrunEkleme.class);
            startActivity(intenttoAddProduct);

        } else if (item.getItemId() == R.id.sign_out) {

            firebaseAuth.signOut();
            Intent intenttoSignUp = new Intent(SaticiFeedActivity.this, SigninActivity.class);
            startActivity(intenttoSignUp);
        } else if (item.getItemId() == R.id.support) {
            Intent support = new Intent(SaticiFeedActivity.this, SupportActivity.class);
            startActivity(support);
        }
        else if (item.getItemId() == R.id.profile) {
            Intent support = new Intent(SaticiFeedActivity.this, SaticiProfileActivity.class);
            startActivity(support);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satici_feed);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userCommentFromFB = new ArrayList<>();
        userNameFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        getDataFromFirestore();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        saticiFeedRecyclerAdapter = new SaticiFeedRecyclerAdapter(userNameFromFB,userCommentFromFB,userImageFromFB);
        recyclerView.setAdapter(saticiFeedRecyclerAdapter);


    }

    public void getDataFromFirestore(){

        firebaseUser= firebaseAuth.getCurrentUser();
        String sellerEmail = firebaseUser.getEmail();


        CollectionReference collectionReference = firebaseFirestore.collection("Post");
        collectionReference.whereEqualTo("userEmail",sellerEmail).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(SaticiFeedActivity.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();

                        String productName = (String) data.get("product_name");
                        String productCommand = (String) data.get("product_command");
                        String downloadUrl = (String) data.get("downloadUrl");

                        System.out.println(productName);

                        userCommentFromFB.add(productCommand);
                        userImageFromFB.add(downloadUrl);
                        userNameFromFB.add(productName);

                        saticiFeedRecyclerAdapter.notifyDataSetChanged();

                    }
                }


            }
        });

    }
}