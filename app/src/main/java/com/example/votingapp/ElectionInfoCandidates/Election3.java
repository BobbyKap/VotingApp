package com.example.votingapp.ElectionInfoCandidates;

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

public class Election3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String messageThrough;
    ArrayList<String> candidates = new ArrayList<>();
    ArrayList<String> parties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        setContentView(R.layout.activity_election3);
        TextView view1 = findViewById(R.id.candidate31);
        TextView view2 = findViewById(R.id.candidate32);
        TextView view3 = findViewById(R.id.candidate31party);
        TextView view4 = findViewById(R.id.candidate32party);
        run();
        view1.setText(candidates.get(0));
        view2.setText(candidates.get(1));
        view3.setText(parties.get(0));
        view4.setText(parties.get(1));

        Spinner spinner = (Spinner) findViewById(R.id.spinnerE3);
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
            for(int i = 0; i <= node.get("contests").get(2).get("candidates").size() - 1; i++) {
                Log.d("I: ", String.valueOf(i));
                String candidate = node.get("contests").get(2).get("candidates").get(i).get("name").toString().replace("\"", "");
                String party = node.get("contests").get(2).get("candidates").get(i).get("party").toString().replace("\"", "");
                candidates.add(candidate);
                parties.add(party + " Party: ");
            }
            Log.d("Candidates", Arrays.toString(candidates.toArray()));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}