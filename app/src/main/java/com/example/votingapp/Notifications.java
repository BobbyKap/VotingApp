package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Notifications extends AppCompatActivity {

    String messageThrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;

        SharedPreferences mPrefs = getSharedPreferences("bool", 0);
        Boolean get1 = mPrefs.getBoolean("key2", false);

        if(get1 != true)
            notif();
        else{
            Button button = findViewById(R.id.button4);
            button.performClick();
        }
    }

    public void notif() {
        SharedPreferences mPrefs = getSharedPreferences("address", 0);
        Boolean get1 = mPrefs.getBoolean("key2", false);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        Intent intent = new Intent(this, BaseInfo.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Notifications Example")
                .setContentText("This is a test notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putBoolean("key2", true).commit();
    }


    public void back(View view){
        Intent intent2 = new Intent(this, EnterAddress.class);
        SharedPreferences preferences = getSharedPreferences("Address", 0);
        preferences.edit().remove("key").commit();
        startActivity(intent2);
    }

    public void sendMessage3(View view) {
        Intent intent2 = new Intent(this, BaseInfo.class);
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }
}