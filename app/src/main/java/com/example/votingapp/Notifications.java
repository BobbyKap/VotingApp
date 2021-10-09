package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Notifications extends AppCompatActivity {

    String messageThrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;
    }

    public void sendMessage3(View view) {
        Intent intent2 = new Intent(this, BaseInfo.class);
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }
}