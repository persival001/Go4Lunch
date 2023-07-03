package com.persival.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.lenient;

import android.content.Context;
import android.location.LocationManager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.persival.go4lunch.data.permissions.PermissionRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private Context mockContext;

    @Mock
    private LocationManager mockLocationManager;

    private PermissionRepository permissionRepository;

    @Before
    public void setup() {
        permissionRepository = new PermissionRepository(mockContext);
    }

    @Test
    public void testRefreshLocationPermissionGranted() {

        permissionRepository.refreshLocationPermission();

        assertEquals(Boolean.TRUE, permissionRepository.isLocationPermission().getValue());
    }

    @Test
    public void testRefreshGpsActivationEnabled() {
        lenient().when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);

        permissionRepository.refreshGpsActivation();

        assertEquals(Boolean.FALSE, permissionRepository.isGpsActivated().getValue());
    }

    @Test
    public void testRefreshGpsActivationDisabled() {
        lenient().when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);

        permissionRepository.refreshGpsActivation();

        assertNotEquals(Boolean.TRUE, permissionRepository.isGpsActivated().getValue());
    }
}