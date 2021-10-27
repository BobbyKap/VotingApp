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

    String Address;
    String messageThrough;
    EditText AddressInput;
    Button ButtonInput;
    Boolean contains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
    }

    public void sendMessage2(View view) {
        AddressInput = (EditText) findViewById(R.id.AddressInput);
        ButtonInput = (Button) findViewById(R.id.button2);
        if(AddressInput.getText().length() == 0){
            if (!AddressInput.getText().toString().equals(messageThrough)) {
                messageThrough = AddressInput.getText().toString();
            }
        }
        else {
            Log.e("Error", "No string inputted");
        }
        Intent intent2 = new Intent(this, Notifications.class);
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }
}