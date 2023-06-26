package com.persival.go4lunch.ui.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.persival.go4lunch.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "GO4LUNCH_CHANNEL_ID";
    private static final String CHANNEL_NAME = "Go4Lunch Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for Go4Lunch app";

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public Notification createNotification(String restaurantName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("It's lunch time!")
            .setContentText("You're dining at " + restaurantName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }
}

