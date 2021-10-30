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

import com.example.votingapp.ElectionInfoCandidates.Election1;
import com.example.votingapp.ElectionInfoCandidates.Election2;
import com.example.votingapp.ElectionInfoCandidates.Election3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ElectionInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String messageThrough;
    ArrayList<String> candidates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        setContentView(R.layout.activity_election_info);

        Button button1 = findViewById(R.id.choice1);
        Button button2 = findViewById(R.id.choice2);
        Button button3 = findViewById(R.id.choice3);
        run();
        button1.setText(candidates.get(0));
        button2.setText(candidates.get(1));
        button3.setText(candidates.get(2));
        Spinner spinner = (Spinner) findViewById(R.id.spinnerEI);
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
    public void run(){
        PollingLocation.DestinationClass destRunner = new PollingLocation.DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destRunner.execute(messageThrough)).get());
            for(int i = 0; i <= 3; i++) {
                String office = node.get("contests").get(i).get("office").toString().replace("\"", "");
                candidates.add(office);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void choice1(View v){
        Intent intent = new Intent(this, Election1.class);
        intent.putExtra("message", messageThrough);
        startActivity(intent);
    }

    public void choice2(View v){
        Intent intent = new Intent(this, Election2.class);
        intent.putExtra("message", messageThrough);
        startActivity(intent);
    }

    public void choice3(View v){
        Intent intent = new Intent(this, Election3.class);
        intent.putExtra("message", messageThrough);
        startActivity(intent);
    }
}