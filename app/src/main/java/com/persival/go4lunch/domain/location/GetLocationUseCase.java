package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import javax.inject.Inject;

public class GetLocationUseCase {

    @NonNull
    private final LocationRepository locationRepository;

    @Inject
    public GetLocationUseCase(
        @NonNull LocationDataRepository locationDataRepository) {
        this.locationRepository = locationDataRepository;
    }

    public LiveData<LocationEntity> invoke() {
        return locationRepository.getLocationLiveData();
    }

}


