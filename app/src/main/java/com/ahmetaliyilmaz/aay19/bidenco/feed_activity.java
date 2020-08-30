package com.ahmetaliyilmaz.aay19.bidenco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class feed_activity extends AppCompatActivity {
    //call firebase
    private FirebaseAuth firebaseAuth;

    //create option menu for exit
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //define items of menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_product) {


        } else if (item.getItemId() == R.id.sign_out) {

            firebaseAuth.signOut();
            Intent intenttoSignUp = new Intent(feed_activity.this, signin_activity.class);
            startActivity(intenttoSignUp);
        } else if (item.getItemId() == R.id.support) {
            Intent support = new Intent(feed_activity.this, SupportActivity.class);
            startActivity(support);
        }
        else if (item.getItemId() == R.id.profile) {
            Intent support = new Intent(feed_activity.this, profile_activity.class);
            startActivity(support);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}