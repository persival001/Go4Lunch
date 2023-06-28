package com.persival.go4lunch.ui.main.maps;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;
import com.persival.go4lunch.domain.location.StopLocationRequestUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.permissions.IsLocationPermissionUseCase;
import com.persival.go4lunch.domain.permissions.RefreshGpsActivationUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetParticipantsUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    @NonNull
    private final GetLocationUseCase getLocationUseCase;
    @NonNull
    private final StartLocationRequestUseCase startLocationRequestUseCase;
    @NonNull
    private final StopLocationRequestUseCase stopLocationRequestUseCase;
    @NonNull
    private final RefreshGpsActivationUseCase refreshGpsActivationUseCase;
    @NonNull
    private final IsLocationPermissionUseCase isLocationPermissionUseCase;
    @NonNull
    private final IsGpsActivatedUseCase isGpsActivatedUseCase;
    private final MediatorLiveData<Map<NearbyRestaurantsEntity, Integer>> restaurantsWithParticipants;

    @Inject
    public MapsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull StartLocationRequestUseCase startLocationRequestUseCase,
        @NonNull StopLocationRequestUseCase stopLocationRequestUseCase,
        @NonNull RefreshGpsActivationUseCase refreshGpsActivationUseCase,
        @NonNull IsLocationPermissionUseCase isLocationPermissionUseCase,
        @NonNull IsGpsActivatedUseCase isGpsActivatedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase,
        @NonNull GetParticipantsUseCase getParticipantsUseCase
    ) {
        this.getLocationUseCase = getLocationUseCase;
        this.startLocationRequestUseCase = startLocationRequestUseCase;
        this.stopLocationRequestUseCase = stopLocationRequestUseCase;
        this.refreshGpsActivationUseCase = refreshGpsActivationUseCase;
        this.isLocationPermissionUseCase = isLocationPermissionUseCase;
        this.isGpsActivatedUseCase = isGpsActivatedUseCase;
        this.restaurantsWithParticipants = new MediatorLiveData<>();

        LiveData<List<NearbyRestaurantsEntity>> restaurantsLiveData = getNearbyRestaurantsUseCase.invoke();
        LiveData<HashMap<String, Integer>> participantsLiveData = getParticipantsUseCase.invoke();

        restaurantsWithParticipants.addSource(restaurantsLiveData, value -> combineLatestData(restaurantsLiveData, participantsLiveData));
        restaurantsWithParticipants.addSource(participantsLiveData, value -> combineLatestData(restaurantsLiveData, participantsLiveData));
    }

    private void combineLatestData(
        LiveData<List<NearbyRestaurantsEntity>> restaurantsLiveData,
        LiveData<HashMap<String, Integer>> participantsLiveData
    ) {
        List<NearbyRestaurantsEntity> restaurants = restaurantsLiveData.getValue();
        HashMap<String, Integer> participants = participantsLiveData.getValue();

        if (restaurants != null && participants != null) {
            Map<NearbyRestaurantsEntity, Integer> combinedData = new HashMap<>();
            for (NearbyRestaurantsEntity restaurant : restaurants) {
                combinedData.put(restaurant, participants.get(restaurant.getId()));
            }
            restaurantsWithParticipants.setValue(combinedData);
        }
    }

    public LiveData<Map<NearbyRestaurantsEntity, Integer>> getRestaurantsWithParticipants() {
        return restaurantsWithParticipants;
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return getLocationUseCase.invoke();
    }

    public LiveData<Boolean> isGpsActivatedLiveData() {
        return isGpsActivatedUseCase.invoke();
    }

    public void refreshGpsActivation() {
        refreshGpsActivationUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void stopLocation() {
        stopLocationRequestUseCase.invoke();
    }

    @SuppressLint("MissingPermission")
    public void onResume() {
        boolean hasGpsPermission = Boolean.TRUE.equals(isLocationPermissionUseCase.invoke().getValue());

        if (hasGpsPermission) {
            startLocationRequestUseCase.invoke();
        } else {
            stopLocationRequestUseCase.invoke();
        }
    }
}



