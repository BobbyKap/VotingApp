package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterAddress extends AppCompatActivity {

    String messageThrough;
    String serverData;
    EditText AddressInput;
    String key = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address);

        SharedPreferences preferences = getSharedPreferences("address", 0);
        serverData = preferences.getString(key, null);
        if(serverData != null){
            Button button = findViewById(R.id.button2);
            button.performClick();
        }

    }

    public void sendMessage2(View view) {
        AddressInput = findViewById(R.id.AddressInput);
        if(serverData == null) {
            messageThrough = AddressInput.getText().toString();
            SharedPreferences preferencesEditor = getSharedPreferences("address", 0);
            preferencesEditor.edit().putString(key, messageThrough).apply();
        }
        else
            messageThrough = serverData;

        Intent intent2 = new Intent(this, Notifications.class);
        intent2.putExtra("message", messageThrough);
        Log.d("MessageThrough", messageThrough);
        startActivity(intent2);
    }
}