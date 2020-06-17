package com.ahmetaliyilmaz.aay19.bidenco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {
    Button send;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = findViewById(R.id.message);
                String m = message.getText().toString();
                sendEmail(m);
            }
        });
    }

    public void sendEmail(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] to = new String[]{"yusayalcin6@gmail.com"};
        String subject = "Bidenco/Destek";

        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
