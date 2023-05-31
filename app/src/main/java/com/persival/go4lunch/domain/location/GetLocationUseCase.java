package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import javax.inject.Inject;

public class GetLocationUseCase {

    @NonNull
    private final LocationDataRepository locationDataRepository;

    @Inject
    public GetLocationUseCase(
        @NonNull LocationDataRepository locationDataRepository) {
        this.locationDataRepository = locationDataRepository;
    }

    public LiveData<LocationEntity> invoke() {
        return locationDataRepository.getLocationLiveData();
    }

}


