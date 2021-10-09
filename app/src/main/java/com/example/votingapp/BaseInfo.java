package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BaseInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
    }

    public void sendMessageElectionInfo(View view) {
        Intent intent = new Intent(this, ElectionInfo.class);
        startActivity(intent);
    }

    public void sendMessagePollingCenter(View view) {
        Intent intent = new Intent(this, PollingCenterMap.class);
        startActivity(intent);
    }
}