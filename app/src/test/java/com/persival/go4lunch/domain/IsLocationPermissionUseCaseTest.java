package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;
import com.persival.go4lunch.domain.permissions.IsLocationPermissionUseCase;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsLocationPermissionUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    GpsPermissionRepository gpsPermissionRepository;

    IsLocationPermissionUseCase isLocationPermissionUseCase;

    @Before
    public void setUp() {
        isLocationPermissionUseCase = new IsLocationPermissionUseCase(gpsPermissionRepository);
    }

    @Test
    public void isLocationPermission_true() {
        // Given
        LiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>(true);
        when(gpsPermissionRepository.isLocationPermission()).thenReturn(locationPermissionLiveData);

        // When
        LiveData<Boolean> result = isLocationPermissionUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), true);
    }

    @Test
    public void isLocationPermission_false() {
        // Given
        LiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>(false);
        when(gpsPermissionRepository.isLocationPermission()).thenReturn(locationPermissionLiveData);

        // When
        LiveData<Boolean> result = isLocationPermissionUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), false);
    }
}
