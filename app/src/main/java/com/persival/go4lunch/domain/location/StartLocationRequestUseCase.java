package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.persival.go4lunch.data.location.LocationDataRepository;

import javax.inject.Inject;

public class StartLocationRequestUseCase {
    @NonNull
    private final LocationRepository locationRepository;

    @Inject
    public StartLocationRequestUseCase(
        @NonNull LocationDataRepository locationDataRepository) {
        this.locationRepository = locationDataRepository;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void invoke() {
        locationRepository.startLocationRequest();
    }
}
