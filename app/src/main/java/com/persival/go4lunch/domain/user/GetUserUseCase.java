package com.persival.go4lunch.domain.user;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.user.model.UserEntity;

import javax.inject.Inject;

public class GetUserUseCase {
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    private final RestaurantRepository restaurantRepository;

    @Inject
    public GetUserUseCase(
        GetLoggedUserUseCase getLoggedUserUseCase,
        RestaurantRepository restaurantRepository
    ) {
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.restaurantRepository = restaurantRepository;
    }

    @Nullable
    public UserEntity invoke() {
        LoggedUserEntity loggedUser = getLoggedUserUseCase.invoke();

        if (loggedUser == null) {
            return null;
        }

        String placeId = restaurantRepository.getPlaceIdUserIsGoingTo();

        return new UserEntity(
            loggedUser,
            placeId
        );
    }

    public interface RestaurantRepository {
        @Nullable
        String getPlaceIdUserIsGoingTo();
    }
}
