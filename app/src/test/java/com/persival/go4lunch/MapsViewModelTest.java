package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;
import com.persival.go4lunch.domain.location.StopLocationRequestUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.permissions.IsLocationPermissionUseCase;
import com.persival.go4lunch.domain.permissions.RefreshGpsActivationUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.ui.main.maps.MapsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // Mocking use cases
    @Mock
    private GetLocationUseCase getLocationUseCase;
    @Mock
    private StartLocationRequestUseCase startLocationRequestUseCase;
    @Mock
    private StopLocationRequestUseCase stopLocationRequestUseCase;
    @Mock
    private RefreshGpsActivationUseCase refreshGpsActivationUseCase;
    @Mock
    private IsLocationPermissionUseCase isLocationPermissionUseCase;
    @Mock
    private IsGpsActivatedUseCase isGpsActivatedUseCase;
    @Mock
    private GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;

    private MapsViewModel viewModel;
    private LocationEntity locationEntity;
    private List<NearbyRestaurantsEntity> nearbyRestaurants;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        locationEntity = new LocationEntity(48.8566, 2.3522);
        nearbyRestaurants = new ArrayList<>();

        when(getLocationUseCase.invoke()).thenReturn(new MutableLiveData<>(locationEntity));
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(nearbyRestaurants));
        when(isGpsActivatedUseCase.invoke()).thenReturn(new MutableLiveData<>(true));
        when(isLocationPermissionUseCase.invoke()).thenReturn(new MutableLiveData<>(true));

        viewModel = new MapsViewModel(
            getLocationUseCase,
            startLocationRequestUseCase,
            stopLocationRequestUseCase,
            refreshGpsActivationUseCase,
            isLocationPermissionUseCase,
            isGpsActivatedUseCase,
            getNearbyRestaurantsUseCase,
            getParticipantsUseCase);
    }

    @Test
    public void testGetLocationLiveData() {
        // Given
        // Provided in setUp method.

        // When
        LiveData<LocationEntity> liveData = viewModel.getLocationLiveData();

        // Then
        assertNotNull(liveData);
        assertEquals(locationEntity, liveData.getValue());
    }

    @Test
    public void testGetLocationLiveDataFailure() {
        // Given
        when(getLocationUseCase.invoke()).thenReturn(new MutableLiveData<>(null));

        // When
        LiveData<LocationEntity> liveData = viewModel.getLocationLiveData();

        // Then
        assertNull(liveData.getValue());
    }

    @Test
    public void testGetNearbyRestaurants() {
        // Given
        // Provided in setUp method.

        // When
        LiveData<List<NearbyRestaurantsEntity>> liveData = viewModel.getNearbyRestaurants();

        // Then
        assertNotNull(liveData);
        assertEquals(nearbyRestaurants, liveData.getValue());
    }

    @Test
    public void testGetNearbyRestaurantsFailure() {
        // Given
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(null));

        // When
        LiveData<List<NearbyRestaurantsEntity>> liveData = viewModel.getNearbyRestaurants();

        // Then
        assertNull(liveData.getValue());
    }


    @Test
    public void testIsGpsActivatedLiveData() {
        // Given
        // Provided in setUp method.

        // When
        LiveData<Boolean> liveData = viewModel.isGpsActivatedLiveData();

        // Then
        assertNotNull(liveData);
        assertEquals(true, liveData.getValue());
    }

    @Test
    public void testIsGpsActivatedLiveDataFailure() {
        // Given
        when(isGpsActivatedUseCase.invoke()).thenReturn(new MutableLiveData<>(null));

        // When
        LiveData<Boolean> liveData = viewModel.isGpsActivatedLiveData();

        // Then
        assertNull(liveData.getValue());
    }

    @Test
    public void testRefreshGpsActivation() {
        // Given
        // Provided in setUp method.

        // When
        viewModel.refreshGpsActivation();

        // Then
        verify(refreshGpsActivationUseCase).invoke();
    }

    @Test(expected = Exception.class)
    public void testRefreshGpsActivationFailure() {
        // Given
        doThrow(new Exception("Mock Exception")).when(refreshGpsActivationUseCase).invoke();

        // When
        viewModel.refreshGpsActivation();
    }

    @Test
    public void testStopLocation() {
        // Given
        // Provided in setUp method.

        // When
        viewModel.stopLocation();

        // Then
        verify(stopLocationRequestUseCase).invoke();
    }

    @Test(expected = Exception.class)
    public void testStopLocationFailure() {
        // Given
        doThrow(new Exception("Mock Exception")).when(stopLocationRequestUseCase).invoke();

        // When
        viewModel.stopLocation();
    }

    @Test
    public void testOnResume() {
        // Given
        // Provided in setUp method.

        // When
        viewModel.onResume();

        // Then
        verify(startLocationRequestUseCase).invoke();
    }

    @Test(expected = Exception.class)
    public void testOnResumeFailure() {
        // Given
        doThrow(new Exception("Mock Exception")).when(startLocationRequestUseCase).invoke();

        // When
        viewModel.onResume();
    }

    @Test
    public void testOnResumeWithoutPermission() {
        // Given
        when(isLocationPermissionUseCase.invoke()).thenReturn(new MutableLiveData<>(false));

        // When
        viewModel.onResume();

        // Then
        verify(stopLocationRequestUseCase).invoke();
    }
}