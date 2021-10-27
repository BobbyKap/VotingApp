package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ElectionInfo extends AppCompatActivity {

    String messageThrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        String id;

        GetIdClass getIdRunner = new GetIdClass();
        ElectionInfoClass electionInfoRunner = new ElectionInfoClass();

        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((getIdRunner.execute(messageThrough)).get());
            if(node.get("election").get("id") != null) {
                id = node.get("election").get("id").toString();
                Log.d("id", id);
            }
            else {
                Log.d("ElectionAvailable:", "No elections are available right now.");
            }
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((electionInfoRunner.execute(messageThrough)).get());
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_election_info);
    }

    public static class ElectionInfoClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuilder sb = new StringBuilder();
            for (String s : address) {
                sb.append(s);
            }
            String addressReal = sb.toString();
            addressReal = addressReal.replace(" ", "%20");
            String parameters = "key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss&address=" + addressReal;
            String url = "https://www.googleapis.com/civicinfo/v2/voterinfo?" + parameters;
            Log.d("url6", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class GetIdClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuffer sb = new StringBuffer();
            for (String s : address) {
                sb.append(s);
            }
            String addressReal = sb.toString();
            addressReal = addressReal.replace(" ", "%20");
            String parameters = "key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss&address=" + addressReal;
            String url = "https://www.googleapis.com/civicinfo/v2/voterinfo?" + parameters;
            Log.d("url6", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}