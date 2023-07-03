package com.persival.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import android.location.Location;
import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.data.shared_prefs.SharedPreferencesRepository;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class LocationDataRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FusedLocationProviderClient mockFusedLocationProviderClient;

    @Mock
    private SharedPreferencesRepository mockSharedPreferencesRepository;

    private LocationDataRepository locationDataRepository;

    @Before
    public void setUp() {
        locationDataRepository = new LocationDataRepository(mockFusedLocationProviderClient, mockSharedPreferencesRepository);
    }

    // Test when location request starts successfully
    @Test
    public void testStartLocationRequestSuccess() {
        // Given
        Location location = new Location("dummyprovider");
        location.setLatitude(48.8566);
        location.setLongitude(2.3522);
        LocationResult locationResult = LocationResult.create(Collections.singletonList(location));

        Task<Void> mockTask = Tasks.forResult(null);
        lenient().doAnswer(invocation -> {
                LocationCallback callback = invocation.getArgument(1);
                callback.onLocationResult(locationResult);
                return mockTask;
            })
            .when(mockFusedLocationProviderClient)
            .requestLocationUpdates(any(LocationRequest.class),
                any(LocationCallback.class),
                any(Looper.class));

        // When
        locationDataRepository.startLocationRequest();

        // Then
        TestUtil.observeForTesting(locationDataRepository.getLocationLiveData(), locationEntity -> {
            LocationEntity actualLocation = TestUtil.getValueForTesting(locationDataRepository.getLocationLiveData());
            LocationEntity expectedLocation = new LocationEntity(48.8566, 2.3522);
            assertEquals(expectedLocation, actualLocation);
        });
    }

    @Test
    public void testStartLocationRequestFailure() {
        // Given
        Task<Void> mockTask = Tasks.forException(new Exception("Location request failed"));
        lenient().doAnswer(invocation -> {
            LocationCallback callback = invocation.getArgument(1);
            callback.onLocationResult(null);
            return mockTask;
        }).when(mockFusedLocationProviderClient).requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class));

        // When
        locationDataRepository.startLocationRequest();

        // Then
        TestUtil.observeForTesting(locationDataRepository.getLocationLiveData(), locationEntity -> {
            LocationEntity actualLocation = TestUtil.getValueForTesting(locationDataRepository.getLocationLiveData());
            LocationEntity expectedLocation = new LocationEntity(48.8566, 2.3522);
            assertEquals(expectedLocation, actualLocation);
        });
    }

}
