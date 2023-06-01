package com.persival.go4lunch.domain.restaurant;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import java.util.List;

import javax.inject.Inject;

public class GetNearbyRestaurantsUseCase {

    @NonNull
    private final PlacesRepository placesRepository;
    @NonNull
    private final GetLocationUseCase getLocationUseCase;

    @Inject
    public GetNearbyRestaurantsUseCase(
        @NonNull PlacesRepository placesRepository,
        @NonNull GetLocationUseCase getLocationUseCase
    ) {
        this.placesRepository = placesRepository;
        this.getLocationUseCase = getLocationUseCase;
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> invoke() {
        LiveData<LocationEntity> locationLiveData = getLocationUseCase.invoke();

        return Transformations.switchMap(
            locationLiveData,
            location -> {
                String locationAsString = location.getLatitude() + "," + location.getLongitude();
                return placesRepository.getNearbyRestaurants(
                    locationAsString,
                    5000,
                    "restaurant",
                    MAPS_API_KEY
                );
            }
        );
    }
}

