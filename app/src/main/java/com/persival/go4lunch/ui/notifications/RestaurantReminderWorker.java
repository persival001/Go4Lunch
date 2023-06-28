package com.persival.go4lunch.ui.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class RestaurantReminderWorker extends Worker {

    public RestaurantReminderWorker(
        @NonNull Context context,
        @NonNull WorkerParameters workerParams
    ) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Retrieve the data
        String restaurantName = getInputData().getString("restaurantName");
        String restaurantAddress = getInputData().getString("restaurantAddress");
        String workmates = getInputData().getString("workmates");

        // Create a new NotificationHelper
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());

        // Create the notification channel
        notificationHelper.createNotificationChannel();

        // Build the notification
        Notification notification = notificationHelper.createNotification(restaurantName, restaurantAddress, workmates);

        // Get the NotificationManager
        NotificationManager notificationManager = (NotificationManager)
            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Show the notification
        notificationManager.notify(0, notification);

        return Result.success();
    }
}
