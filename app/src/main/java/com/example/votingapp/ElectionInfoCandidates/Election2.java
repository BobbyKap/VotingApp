package com.example.votingapp.ElectionInfoCandidates;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.votingapp.PollingLocation;
import com.example.votingapp.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Election2 extends AppCompatActivity {

    String messageThrough;
    ArrayList<String> candidates = new ArrayList<>();
    ArrayList<String> parties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        setContentView(R.layout.activity_election2);
        TextView view1 = findViewById(R.id.candidate21);
        TextView view2 = findViewById(R.id.candidate22);
        TextView view3 = findViewById(R.id.candidate21party);
        TextView view4 = findViewById(R.id.candidate22party);
        run();
        view1.setText(candidates.get(0));
        view2.setText(candidates.get(1));
        view3.setText(parties.get(0));
        view4.setText(parties.get(1));
    }

    public void run(){
        PollingLocation.DestinationClass destRunner = new PollingLocation.DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destRunner.execute(messageThrough)).get());
            for(int i = 0; i <= node.get("contests").get(1).get("candidates").size() - 1; i++) {
                Log.d("I: ", String.valueOf(i));
                String candidate = node.get("contests").get(1).get("candidates").get(i).get("name").toString().replace("\"", "");
                String party = node.get("contests").get(1).get("candidates").get(i).get("party").toString().replace("\"", "");
                candidates.add(candidate);
                parties.add(party + " Party: ");
            }
            Log.d("Candidates", Arrays.toString(candidates.toArray()));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}