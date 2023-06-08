package com.persival.go4lunch.ui.main.maps;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.location.GetLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.HasLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.location.RefreshLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;
import com.persival.go4lunch.domain.location.StopLocationRequestUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    private final GetLocationUseCase getLocationUseCase;
    private final StartLocationRequestUseCase startLocationRequestUseCase;
    private final StopLocationRequestUseCase stopLocationRequestUseCase;
    private final RefreshLocationPermissionUseCase refreshLocationPermissionUseCase;
    private final GetLocationPermissionUseCase getLocationPermissionUseCase;
    private final HasLocationPermissionUseCase hasLocationPermissionUseCase;
    private final IsGpsActivatedUseCase isGpsActivatedUseCase;
    private final GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;

    @Inject
    public MapsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull StopLocationRequestUseCase stopLocationRequestUseCase,
        @NonNull RefreshLocationPermissionUseCase refreshLocationPermissionUseCase,
        @NonNull GetLocationPermissionUseCase getLocationPermissionUseCase,
        @NonNull HasLocationPermissionUseCase hasLocationPermissionUseCase,
        @NonNull IsGpsActivatedUseCase isGpsActivatedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase
    ) {
        this.getLocationUseCase = getLocationUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.stopLocationRequestUseCase = stopLocationRequestUseCase;
        this.refreshLocationPermissionUseCase = refreshLocationPermissionUseCase;
        this.getLocationPermissionUseCase = getLocationPermissionUseCase;
        this.hasLocationPermissionUseCase = hasLocationPermissionUseCase;
        this.isGpsActivatedUseCase = isGpsActivatedUseCase;
        this.getNearbyRestaurantsUseCase = getNearbyRestaurantsUseCase;
    }

    public LiveData<Boolean> getLocationPermission() {
        return getLocationPermissionUseCase.invoke();
    }

    public boolean hasLocationPermission() {
        return hasLocationPermissionUseCase.invoke();
    }

    public void onResume() {
        refreshLocationPermissionUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        startLocationRequestUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void stopLocation() {
        stopLocationRequestUseCase.invoke();
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants() {
        return getNearbyRestaurantsUseCase.invoke();
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return getLocationUseCase.invoke();
    }

    public LiveData<Boolean> isGpsActivatedLiveData() {
        return isGpsActivatedUseCase.invoke();
    }

}



