package com.persival.go4lunch.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.work.WorkManager;

import com.persival.go4lunch.domain.notifications.CancelRestaurantNotificationUseCase;
import com.persival.go4lunch.domain.notifications.PreferencesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class CancelRestaurantNotificationUseCaseTest {
    @Mock
    WorkManager workManager;

    @Mock
    PreferencesRepository preferencesRepository;

    CancelRestaurantNotificationUseCase cancelRestaurantNotificationUseCase;

    @Before
    public void setUp() {
        cancelRestaurantNotificationUseCase = new CancelRestaurantNotificationUseCase(workManager, preferencesRepository);
    }

    @Test
    public void cancelNotificationWithWorkId() {
        // Given
        String workId = "123e4567-e89b-12d3-a456-426614174000";

        // When
        when(preferencesRepository.getWorkId()).thenReturn(workId);
        cancelRestaurantNotificationUseCase.invoke();

        // Then
        verify(workManager).cancelWorkById(UUID.fromString(workId));
        verify(preferencesRepository).removeWorkId();
    }


    @Test
    public void cancelNotificationWithoutWorkId() {
        // Given
        when(preferencesRepository.getWorkId()).thenReturn(null);

        // When
        cancelRestaurantNotificationUseCase.invoke();

        // Then
        verify(workManager, never()).cancelWorkById(any(UUID.class));
        verify(preferencesRepository, never()).removeWorkId();
    }

}