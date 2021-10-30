package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class BaseInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String messageThrough;
    String date;
    String key2 = "notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        run();

        TextView dateView = findViewById(R.id.Date);
        dateView.setText(date);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
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
            preferences.edit().remove(key2).apply();
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

    public void run(){
        PollingLocation.DestinationClass destRunner = new PollingLocation.DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destRunner.execute(messageThrough)).get());
            String dateWrong = node.get("election").get("electionDay").toString().replace("\"", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            date = LocalDate.parse(dateWrong, formatter).format(formatter2);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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