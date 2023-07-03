package com.persival.go4lunch.domain;

import static org.mockito.Mockito.verify;

import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.persival.go4lunch.domain.notifications.PreferencesRepository;
import com.persival.go4lunch.domain.notifications.ScheduleRestaurantNotificationUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleRestaurantNotificationUseCaseTest {

    @Mock
    private WorkManager mockWorkManager;

    @Mock
    private PreferencesRepository mockPreferencesRepository;

    private ScheduleRestaurantNotificationUseCase useCase;

    @Before
    public void setup() {
        useCase = new ScheduleRestaurantNotificationUseCase(mockWorkManager, mockPreferencesRepository);
    }

    @Test
    public void testScheduleRestaurantNotification() {
        String testRestaurantName = "Restaurant Name";
        String testRestaurantAddress = "Restaurant Address";
        String testWorkmates = "Workmates";

        // Invoke method to be tested
        useCase.invoke(testRestaurantName, testRestaurantAddress, testWorkmates);

        // Capture the WorkRequest passed to mockWorkManager.enqueue()
        ArgumentCaptor<WorkRequest> captor = ArgumentCaptor.forClass(WorkRequest.class);
        verify(mockWorkManager).enqueue(captor.capture());

        // Get the WorkRequest and its ID
        WorkRequest capturedRequest = captor.getValue();
        String workId = capturedRequest.getId().toString();

        // Verify that the workId was saved in preferencesRepository
        verify(mockPreferencesRepository).saveWorkId(workId);
    }
}

