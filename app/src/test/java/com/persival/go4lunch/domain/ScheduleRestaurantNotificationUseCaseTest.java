package com.persival.go4lunch.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.persival.go4lunch.domain.notifications.PreferencesRepository;
import com.persival.go4lunch.domain.notifications.ScheduleRestaurantNotificationUseCase;
import com.persival.go4lunch.ui.notifications.RestaurantReminderWorker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleRestaurantNotificationUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ScheduleRestaurantNotificationUseCase scheduleRestaurantNotificationUseCase;

    @Mock
    private WorkManager mockWorkManager;

    @Mock
    private PreferencesRepository mockPreferencesRepository;

    @Before
    public void setUp() {
        scheduleRestaurantNotificationUseCase = new ScheduleRestaurantNotificationUseCase(
            mockWorkManager, mockPreferencesRepository);
    }

    @Test
    public void invoke_successful() {
        // Given
        String restaurantName = "Restaurant A";
        String restaurantAddress = "Address A";
        String workmates = "Workmate A";
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RestaurantReminderWorker.class)
            .build();

        // When
        scheduleRestaurantNotificationUseCase.invoke(restaurantName, restaurantAddress, workmates);

        // Then
        verify(mockPreferencesRepository).saveWorkId(anyString());
    }


    @Test(expected = IllegalStateException.class)
    public void invoke_failure() {
        // Given
        String restaurantName = "Restaurant A";
        String restaurantAddress = "Address A";
        String workmates = "Workmate A";
        when(mockWorkManager.enqueue(any(OneTimeWorkRequest.class))).thenThrow(IllegalStateException.class);

        // When
        scheduleRestaurantNotificationUseCase.invoke(restaurantName, restaurantAddress, workmates);

        // Then
        // An IllegalStateException is expected to be thrown
    }
}
