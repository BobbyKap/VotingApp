package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EnterAddress extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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

        Spinner spinner = (Spinner) findViewById(R.id.spinnerEA);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
        spinner.setSelection(1, false);
        spinner.setSelection(2, false);
        spinner.setSelection(3, false);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(pos==0){
            Intent intent = new Intent(this, MainActivity.class);
            SharedPreferences preferences = getSharedPreferences("address", 0);
            preferences.edit().remove("address").apply();
            preferences.edit().remove("notification").apply();
            startActivity(intent);
        }
        if(pos==1){
            Intent intent = new Intent(this, EnterAddress.class);
            SharedPreferences preferences = getSharedPreferences("address", 0);
            preferences.edit().remove("address").apply();
            preferences.edit().remove("notification").apply();
            intent.putExtra("message", messageThrough);
            startActivity(intent);
        }
        if(pos==2){
            Intent intent = new Intent(this, Notifications.class);
            SharedPreferences preferences = getSharedPreferences("address", 0);
            preferences.edit().remove("notification").apply();
            intent.putExtra("message", messageThrough);
            startActivity(intent);
        }
        if(pos==3){
            Intent intent = new Intent(this, BaseInfo.class);
            intent.putExtra("message", messageThrough);
            startActivity(intent);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

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
        startActivity(intent2);
    }
}