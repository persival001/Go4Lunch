package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.UserRepository;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetWorkmatesUseCase {

    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetWorkmatesUseCase(
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull UserRepository userRepository
    ) {
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.userRepository = userRepository;
    }

    public LiveData<List<WorkmateEntity>> invoke() {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        if (currentUser == null) {
            return new MutableLiveData<>(null);
        } else {
            WorkmateEntity currentWorkmate = mapLoggedUserToWorkmate(currentUser);

            return Transformations.map(userRepository.getWorkmatesLiveData(), workmateEntities -> {
                List<WorkmateEntity> workmatesWithCurrentUser = new ArrayList<>(workmateEntities);
                workmatesWithCurrentUser.add(currentWorkmate);

                return workmatesWithCurrentUser;
            });
        }
    }


    private WorkmateEntity mapLoggedUserToWorkmate(LoggedUserEntity loggedUser) {
        if (loggedUser == null) {
            return null;
        } else {
            return new WorkmateEntity(
                loggedUser.getId(),
                loggedUser.getAvatarPictureUrl(),
                loggedUser.getName(),
                "123456,",
                null,
                "123456"
            );
        }
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
