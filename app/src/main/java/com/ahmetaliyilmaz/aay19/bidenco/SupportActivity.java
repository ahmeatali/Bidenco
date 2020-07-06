package com.ahmetaliyilmaz.aay19.bidenco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] to = new String[]{"yusayalcin6@gmail.com"};
        String subject = "Bidenco/Destek";

        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}