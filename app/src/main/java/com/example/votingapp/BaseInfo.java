package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BaseInfo extends AppCompatActivity {

    String messageThrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;
    }

    public void sendMessageElectionInfo(View view) {
        Intent intent3 = new Intent(this, ElectionInfo.class);
        startActivity(intent3);
    }

    public void sendMessagePollingCenter(View view) {
        Intent intent4 = new Intent(this, PollingCenterMap.class);
        intent4.putExtra("message", messageThrough);
        startActivity(intent4);
    }
}