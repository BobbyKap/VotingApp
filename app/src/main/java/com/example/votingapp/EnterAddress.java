package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterAddress extends AppCompatActivity {

    String Address;
    EditText AddressInput;
    Button ButtonInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        AddressInput = (EditText) findViewById(R.id.Address);
        ButtonInput = (Button) findViewById(R.id.button2);
        ButtonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address = AddressInput.getText().toString();
            }
        });
    }

    public void sendMessage2(View view) {
        Intent intent2 = new Intent(this, Notifications.class);
        intent2.putExtra("message", Address);
        startActivity(intent2);
    }
}