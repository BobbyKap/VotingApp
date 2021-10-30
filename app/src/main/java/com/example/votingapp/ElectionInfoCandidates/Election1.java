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

public class Election1 extends AppCompatActivity {

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
            Log.d("Candidates", Arrays.toString(candidates.toArray()));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}