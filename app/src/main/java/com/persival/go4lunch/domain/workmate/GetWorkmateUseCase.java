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
import javax.inject.Singleton;

@Singleton
public class GetWorkmateUseCase {

    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetWorkmateUseCase(
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
            return Transformations.map(userRepository.getWorkmatesLiveData(), workmateEntities -> {
                List<WorkmateEntity> filteredWorkmateEntities = new ArrayList<>(workmateEntities.size());

                for (WorkmateEntity workmateEntity : workmateEntities) {
                    if (!currentUser.getId().equals(workmateEntity.getId())) {
                        filteredWorkmateEntities.add(workmateEntity);
                    }
                }

                return filteredWorkmateEntities;
            });
        }
    }
}
