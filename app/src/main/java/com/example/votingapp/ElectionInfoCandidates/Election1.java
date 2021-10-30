package com.example.votingapp.ElectionInfoCandidates;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.votingapp.BaseInfo;
import com.example.votingapp.EnterAddress;
import com.example.votingapp.MainActivity;
import com.example.votingapp.Notifications;
import com.example.votingapp.PollingLocation;
import com.example.votingapp.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Election1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String messageThrough;
    ArrayList<String> candidates = new ArrayList<>();
    ArrayList<String> parties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        setContentView(R.layout.activity_election1);
        TextView view1 = findViewById(R.id.candidate11);
        TextView view2 = findViewById(R.id.candidate12);
        TextView view3 = findViewById(R.id.candidate13);
        TextView view4 = findViewById(R.id.candidate11party);
        TextView view5 = findViewById(R.id.candidate12party);
        TextView view6 = findViewById(R.id.candidate13party);
        run();
        view1.setText(candidates.get(0));
        view2.setText(candidates.get(1));
        view3.setText(candidates.get(2));
        view4.setText(parties.get(0));
        view5.setText(parties.get(1));
        view6.setText(parties.get(2));

        Spinner spinner = (Spinner) findViewById(R.id.spinnerE1);
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
            for(int i = 0; i <= node.get("contests").get(0).get("candidates").size() - 1; i++) {
                String candidate = node.get("contests").get(0).get("candidates").get(i).get("name").toString().replace("\"", "");
                String party = node.get("contests").get(0).get("candidates").get(i).get("party").toString().replace("\"", "");
                candidates.add(candidate);
                parties.add(party + " Party: ");
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}