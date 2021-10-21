package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Notifications extends AppCompatActivity {

    String messageThrough;
    Boolean yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;
    }

    public void sendMessage3(View view) {
        CheckBox checkBox = findViewById(R.id.checkBox);
        if(checkBox.isChecked()) {
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
        }
        //------------------------------------------------------------------------------------------------
        Intent intent2 = new Intent(this, BaseInfo.class);
        intent2.putExtra("message", messageThrough);
        startActivity(intent2);
    }
}