package com.persival.go4lunch.domain.location;

import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.location.model.LocationEntity;

public interface LocationRepository {
    LiveData<LocationEntity> getLocationLiveData();

    void startLocationRequest();

    void stopLocationRequest();
}