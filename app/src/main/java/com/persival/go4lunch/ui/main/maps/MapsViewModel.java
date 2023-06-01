package com.persival.go4lunch.ui.main.maps;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.places.GooglePlacesRepository;
import com.persival.go4lunch.domain.location.GetLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.HasLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.RefreshLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.RequestLocationPermissionUseCase;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    private final GetLocationUseCase getLocationUseCase;
    private final StartLocationRequestUseCase startLocationRequestUseCase;
    private final RefreshLocationPermissionUseCase refreshLocationPermissionUseCase;
    private final GetLocationPermissionUseCase getLocationPermissionUseCase;
    private final RequestLocationPermissionUseCase requestLocationPermissionUseCase;
    private final HasLocationPermissionUseCase hasLocationPermissionUseCase;

    private final LiveData<List<NearbyRestaurantsResponse.Place>> nearbyRestaurantsLiveData;

    @Inject
    public MapsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull RefreshLocationPermissionUseCase refreshLocationPermissionUseCase,
        @NonNull GetLocationPermissionUseCase getLocationPermissionUseCase,
        @NonNull RequestLocationPermissionUseCase requestLocationPermissionUseCase,
        @NonNull HasLocationPermissionUseCase hasLocationPermissionUseCase,
        @NonNull GooglePlacesRepository googlePlacesRepository
    ) {
        this.getLocationUseCase = getLocationUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.refreshLocationPermissionUseCase = refreshLocationPermissionUseCase;
        this.getLocationPermissionUseCase = getLocationPermissionUseCase;
        this.requestLocationPermissionUseCase = requestLocationPermissionUseCase;
        this.hasLocationPermissionUseCase = hasLocationPermissionUseCase;

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

    public LiveData<Boolean> getLocationPermission() {
        return getLocationPermissionUseCase.invoke();
    }

    public boolean hasLocationPermission() {
        return hasLocationPermissionUseCase.invoke();
    }

    public void refreshLocationPermission() {
        refreshLocationPermissionUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        startLocationRequestUseCase.invoke();
    }

    public void requestLocationPermission(Activity activity, int requestCode) {
        requestLocationPermissionUseCase.invoke(activity, requestCode);
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants() {
        return nearbyRestaurantsLiveData;
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return getLocationUseCase.invoke();
    }

}



