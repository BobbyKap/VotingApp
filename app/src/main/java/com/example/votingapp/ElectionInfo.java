package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.votingapp.ElectionInfoCandidates.Election1;
import com.example.votingapp.ElectionInfoCandidates.Election2;
import com.example.votingapp.ElectionInfoCandidates.Election3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ElectionInfo extends AppCompatActivity {

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
        Log.d("Candidates", Arrays.toString(candidates.toArray()));
        button1.setText(candidates.get(0));
        button2.setText(candidates.get(1));
        button3.setText(candidates.get(2));

    }

    public void run(){
        PollingLocation.DestinationClass destRunner = new PollingLocation.DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destRunner.execute(messageThrough)).get());
            for(int i = 0; i <= 3; i++) {
                String office = node.get("contests").get(i).get("office").toString().replace("\"", "");
                candidates.add(office);
            }
            Log.d("Candidates", Arrays.toString(candidates.toArray()));
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