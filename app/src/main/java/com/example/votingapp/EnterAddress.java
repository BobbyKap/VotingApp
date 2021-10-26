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
    EditText AddressInput;
    Button ButtonInput;
    String get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address);

        AddressInput = (EditText) findViewById(R.id.Address);
        ButtonInput = (Button) findViewById(R.id.button2);

        SharedPreferences mPrefs = getSharedPreferences("address", 0);
        get = mPrefs.getString("key", null);
        Log.d("Debug1", get);
        if (get == null){
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("key", AddressInput.getText().toString()).commit();
            String get2 = mPrefs.getString("key", null);
            get = get2;
        }
        else{
            ButtonInput.performClick();
        }

    }

    public void sendMessage2(View view) {
        Address = "1 Morningside Drive";
        Intent intent2 = new Intent(this, Notifications.class);
        intent2.putExtra("message", Address);
        startActivity(intent2);
    }
}