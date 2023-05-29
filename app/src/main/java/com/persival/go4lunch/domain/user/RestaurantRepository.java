package com.persival.go4lunch.domain.user;

import androidx.annotation.Nullable;

public interface RestaurantRepository {
    @Nullable
    String getPlaceIdUserIsGoingTo();

}
