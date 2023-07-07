package com.persival.go4lunch.domain.notifications;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.persival.go4lunch.ui.notifications.RestaurantReminderWorker;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ScheduleRestaurantNotificationUseCase {
    @NonNull
    private final WorkManager workManager;
    @NonNull
    private final PreferencesRepository preferencesRepository;

    @Inject
    public ScheduleRestaurantNotificationUseCase(
        @NonNull WorkManager workManager,
        @NonNull PreferencesRepository preferencesRepository
    ) {
        this.workManager = workManager;
        this.preferencesRepository = preferencesRepository;
    }

    public void invoke(String restaurantName, String restaurantAddress, String workmates) {

        // Prepare data for the worker
        Data myData = new Data.Builder()
            .putString("restaurantName", restaurantName)
            .putString("restaurantAddress", restaurantAddress)
            .putString("workmates", workmates)
            .build();

        // Calculate the remaining time before the next notification
        Calendar now = Calendar.getInstance();
        Calendar noon = Calendar.getInstance();
        noon.set(Calendar.HOUR_OF_DAY, 12);
        noon.set(Calendar.MINUTE, 0);

        long delay = noon.getTimeInMillis() - now.getTimeInMillis();

        // If the current time is after noon, schedule for next day
        if (delay < 0) {
            // Add 24 hours to the delay
            delay += 24 * 60 * 60 * 1000;
        }

        // Create notification
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RestaurantReminderWorker.class)
            .setInputData(myData)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build();

        // Get the UUID of the WorkRequest
        UUID workId = workRequest.getId();
        // Enqueue the work
        workManager.enqueue(workRequest);
        // Save the UUID for future reference
        preferencesRepository.saveWorkId(workId.toString());
    }

}
