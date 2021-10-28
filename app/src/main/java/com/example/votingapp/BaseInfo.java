package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class BaseInfo extends AppCompatActivity {

    String messageThrough;
    String key2 = "notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;
    }

    public void back(View view){
        Intent intent2 = new Intent(this, Notifications.class);
        SharedPreferences preferences = getSharedPreferences("bool", 0);
        preferences.edit().remove(key2).apply();
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }

    public void sendMessageElectionInfo(View view) {
        Intent intent3 = new Intent(this, ElectionInfo.class);
        intent3.putExtra("message", messageThrough);
        startActivity(intent3);
    }

    public void sendMessagePollingCenter(View view) {
        Intent intent4 = new Intent(this, PollingLocation.class);
        intent4.putExtra("message", messageThrough);
        startActivity(intent4);
    }
}