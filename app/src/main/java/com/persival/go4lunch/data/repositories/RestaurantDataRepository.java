package com.persival.go4lunch.data.repositories;

import androidx.annotation.Nullable;

import com.persival.go4lunch.domain.user.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RestaurantDataRepository implements RestaurantRepository {

    @Inject
    public RestaurantDataRepository(
    ) {
    }

    @Nullable
    @Override
    public String getPlaceIdUserIsGoingTo() {
        return null;
    }
}
