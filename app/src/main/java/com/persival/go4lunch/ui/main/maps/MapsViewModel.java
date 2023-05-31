package com.persival.go4lunch.ui.main.maps;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permissions.PermissionChecker;
import com.persival.go4lunch.data.places.GooglePlacesRepository;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.RefreshLocationUseCase;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    private final GetLocationUseCase getLocationUseCase;
    private final StartLocationRequestUseCase startLocationRequestUseCase;
    private final RefreshLocationUseCase refreshLocationUseCase;
    private final PermissionChecker permissionChecker;
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
    private final LiveData<List<NearbyRestaurantsResponse.Place>> nearbyRestaurantsLiveData;

    @Inject
    public MapsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull RefreshLocationUseCase refreshLocationUseCase,
        @NonNull GooglePlacesRepository googlePlacesRepository,
        @NonNull PermissionChecker permissionChecker
    ) {
        this.getLocationUseCase = getLocationUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.refreshLocationUseCase = refreshLocationUseCase;
        this.permissionChecker = permissionChecker;

        LiveData<LocationEntity> locationLiveData = getLocationUseCase.invoke();

        nearbyRestaurantsLiveData = Transformations.switchMap(
            locationLiveData,
            location -> {
                String locationAsString = location.getLatitude() + "," + location.getLongitude();
                return googlePlacesRepository.getNearbyRestaurants(
                    locationAsString,
                    5000,
                    "restaurant",
                    MAPS_API_KEY
                );
            }
        );
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants() {
        return nearbyRestaurantsLiveData;
    }

    public boolean hasLocationPermission() {
        return permissionChecker.hasLocationPermission();
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return getLocationUseCase.invoke();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        startLocationRequestUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        refreshLocationUseCase.invoke();
    }
}



