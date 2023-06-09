package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReadWorkmatesUseCase {

    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public ReadWorkmatesUseCase(
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull UserRepository userRepository
    ) {
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.userRepository = userRepository;
    }

    public LiveData<List<WorkmateEntity>> invoke() {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        if (currentUser == null) {
            return new MutableLiveData<>(new ArrayList<>());
        } else {
            WorkmateEntity currentWorkmate = mapLoggedUserToWorkmate(currentUser);

            return Transformations.map(userRepository.getWorkmatesLiveData(), workmateEntities -> {
                List<WorkmateEntity> workmatesWithCurrentUser = new ArrayList<>(workmateEntities);
                workmatesWithCurrentUser.add(currentWorkmate);

                return workmatesWithCurrentUser;
            });
        }
    }


    private WorkmateEntity mapLoggedUserToWorkmate(@NonNull LoggedUserEntity loggedUser) {
        List<String> restaurantIds = new ArrayList<>();
        restaurantIds.add("id1");
        restaurantIds.add("id2");
        restaurantIds.add("id3");
        restaurantIds.add("id4");
        restaurantIds.add("id5");

        return new WorkmateEntity(
            loggedUser.getId(),
            loggedUser.getAvatarPictureUrl(),
            loggedUser.getName(),
            restaurantIds
        );
    }



   /* public LiveData<List<WorkmateEntity>> invoke() {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        if (currentUser == null) {
            return new MutableLiveData<>(null);
        } else {
            return Transformations.map(userRepository.getWorkmatesLiveData(), workmateEntities -> {
                List<WorkmateEntity> reorderedWorkmateEntities = new ArrayList<>(workmateEntities);
                WorkmateEntity currentUserEntity = null;

                // Find the currentUserEntity in the list and remove it.
                for (WorkmateEntity workmateEntity : workmateEntities) {
                    if (currentUser.getId().equals(workmateEntity.getId())) {
                        currentUserEntity = workmateEntity;
                        reorderedWorkmateEntities.remove(workmateEntity);
                        break;
                    }
                }

                // If the currentUserEntity was found, add it at the beginning of the list.
                if (currentUserEntity != null) {
                    reorderedWorkmateEntities.add(0, currentUserEntity);
                }

                return reorderedWorkmateEntities;
            });
        }
    }*/

}
