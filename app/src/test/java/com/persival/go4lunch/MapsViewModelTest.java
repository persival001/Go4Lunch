package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
            getNearbyRestaurantsUseCase
        );
    }

    @Test
    public void testGetLocationLiveData() {
        LiveData<LocationEntity> liveData = viewModel.getLocationLiveData();
        assertNotNull(liveData);
        assertEquals(locationEntity, liveData.getValue());
    }

    @Test
    public void testGetNearbyRestaurants() {
        LiveData<List<NearbyRestaurantsEntity>> liveData = viewModel.getNearbyRestaurants();
        assertNotNull(liveData);
        assertEquals(nearbyRestaurants, liveData.getValue());
    }

    @Test
    public void testIsGpsActivatedLiveData() {
        LiveData<Boolean> liveData = viewModel.isGpsActivatedLiveData();
        assertNotNull(liveData);
        assertEquals(true, liveData.getValue());
    }

    @Test
    public void testRefreshGpsActivation() {
        viewModel.refreshGpsActivation();
        verify(refreshGpsActivationUseCase).invoke();
    }

    @Test
    public void testStopLocation() {
        viewModel.stopLocation();
        verify(stopLocationRequestUseCase).invoke();
    }

    @Test
    public void testOnResume() {
        viewModel.onResume();
        verify(startLocationRequestUseCase).invoke();
    }

    @Test
    public void testOnResumeWithoutPermission() {
        when(isLocationPermissionUseCase.invoke()).thenReturn(new MutableLiveData<>(false));
        viewModel.onResume();
        verify(stopLocationRequestUseCase).invoke();
    }
}

