package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ElectionInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_info);
    }

    public class ElectionInfoClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < address.length; i++) {
                sb.append(address[i]);
            }
            String addressReal = sb.toString();
            addressReal = addressReal.replace(" ", "%20");
            String parameters = "address=" + addressReal + "&" + "&CA&key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss";
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/geocode/" + output + "?" + parameters;
            Log.d("url5", url);
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

    public class GetIdClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < address.length; i++) {
                sb.append(address[i]);
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