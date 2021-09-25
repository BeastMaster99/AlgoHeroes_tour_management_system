package com.example.madfinalproject;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationHelper extends Application {

    public static final String ChannelId = "channel1";

    @Override
    public void onCreate(){
        super.onCreate();
        createNotificationOnChannels();
    }

    private void createNotificationOnChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ChannelId, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
