package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Notifications extends AppCompatActivity {

    String messageThrough;
    Boolean serverData;
    String formattedAddress;
    String key = "address";
    String key2 = "notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        CheckBox checkBox = findViewById(R.id.checkBox1);
        Bundle bundle = getIntent().getExtras();
        messageThrough = bundle.getString("message");
        Log.d("MessageThrough", messageThrough);

        SharedPreferences preferences = getSharedPreferences("bool", 0);
        serverData = preferences.getBoolean(key2, false);

        if(serverData){
            notification();
            Button button = findViewById(R.id.button4);
            button.performClick();
        }
        else {
            if(checkBox.isChecked()){
                notification();
                preferences.edit().putBoolean(key2, true).apply();
            }
        }
    }

    public void notification() {
        Intent intent = new Intent(this, BaseInfo.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        intent.putExtra("message", messageThrough);

        PollingLocation.DestinationClass destRunner = new PollingLocation.DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destRunner.execute(messageThrough)).get());
            String line1 = node.get("pollingLocations").get(0).get("address").get("line1").toString().replace("\"", "");
            String city = node.get("pollingLocations").get(0).get("address").get("city").toString().replace("\"", "");
            String state = node.get("pollingLocations").get(0).get("address").get("state").toString().replace("\"", "");
            String zip = node.get("pollingLocations").get(0).get("address").get("zip").toString().replace("\"", " ");
            formattedAddress = line1 + ", " + city + ", " + state + ", " + zip;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Your Polling Center is located at: " + formattedAddress)
                .setContentText("Get directions to your Polling Center!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());

        SharedPreferences preferences = getSharedPreferences("bool", 0);
        preferences.edit().putBoolean(key2, true).apply();
    }


    public void back(View view){
        Intent intent2 = new Intent(this, EnterAddress.class);
        SharedPreferences preferences = getSharedPreferences("address", 0);
        preferences.edit().remove(key).apply();
        startActivity(intent2);
    }

    public void sendMessage3(View view) {
        Intent intent2 = new Intent(this, BaseInfo.class);
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }
}