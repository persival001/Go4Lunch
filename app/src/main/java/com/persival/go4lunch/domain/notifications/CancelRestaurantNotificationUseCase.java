package com.persival.go4lunch.domain.notifications;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;

import java.util.UUID;

import javax.inject.Inject;

public class CancelRestaurantNotificationUseCase {
    @NonNull
    private final WorkManager workManager;
    @NonNull
    private final PreferencesRepository preferencesRepository;

    @Inject
    public CancelRestaurantNotificationUseCase(
        @NonNull WorkManager workManager,
        @NonNull PreferencesRepository preferencesRepository
    ) {
        this.workManager = workManager;
        this.preferencesRepository = preferencesRepository;
    }

    public void invoke() {
        String workId = preferencesRepository.getWorkId();
        if (workId != null) {
            workManager.cancelWorkById(UUID.fromString(workId));
            preferencesRepository.removeWorkId();
        }
    }
}
