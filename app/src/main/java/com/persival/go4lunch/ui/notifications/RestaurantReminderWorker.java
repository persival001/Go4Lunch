package com.persival.go4lunch.ui.notifications;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.persival.go4lunch.R;


public class RestaurantReminderWorker extends Worker {

    private static final String CHANNEL_ID = "GO4LUNCH_CHANNEL_ID";

    public RestaurantReminderWorker(
        @NonNull Context context,
        @NonNull WorkerParameters workerParams
    ) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        NotificationManager notificationManager = (NotificationManager)
            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_lunch_dining_24)
            .setContentTitle("Restaurant Reminder")
            .setContentText("Don't forget to go to your chosen restaurant!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        notificationManager.notify(0, builder.build());

        return Result.success();
    }

}
