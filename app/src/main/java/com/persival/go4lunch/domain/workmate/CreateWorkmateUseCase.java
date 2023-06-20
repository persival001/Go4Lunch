package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateWorkmateUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public CreateWorkmateUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public void invoke() {
        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();
        if (loggedUserEntity != null) {
            String userId = loggedUserEntity.getId();
            String userName = loggedUserEntity.getName();
            String userPictureUrl = loggedUserEntity.getPictureUrl();

            List<String> likedRestaurantsId = new ArrayList<>();

            UserEntity newUser = new UserEntity(userId, userName, userPictureUrl, likedRestaurantsId);

            userRepository.createUser(newUser);
            userRepository.updateWorkmateInformation(loggedUserEntity, null, null);
        } else {
            throw new IllegalStateException("User is not logged in");
        }
    }

}

