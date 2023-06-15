package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetLikedRestaurantsUseCase {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public GetLikedRestaurantsUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LiveData<List<String>> invoke() {
        LoggedUserEntity user = getLoggedUserUseCase.invoke();
        if (user != null) {
            String userId = user.getId();
            return userRepository.getLikedRestaurantsForUser(userId);
        } else {
            return new MutableLiveData<>(new ArrayList<>());
        }
    }

}

