package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

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
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();
        if (currentUser != null) {
            String userId = currentUser.getId();
            String userName = currentUser.getName();
            String userPhotoUrl = currentUser.getAvatarPictureUrl();

            List<String> likedRestaurantsId = new ArrayList<>();

            WorkmateEntity newUser = new WorkmateEntity(userId, userPhotoUrl, userName, likedRestaurantsId);

            userRepository.createUser(newUser);
        } else {
            throw new IllegalStateException("User is not logged in");
        }
    }

}

