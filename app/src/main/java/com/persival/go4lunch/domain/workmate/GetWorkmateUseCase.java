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

public class GetWorkmateUseCase {

    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final WorkmateRepository workmateRepository;

    @Inject
    public GetWorkmateUseCase(
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull WorkmateRepository workmateRepository
    ) {
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.workmateRepository = workmateRepository;
    }

    public LiveData<List<WorkmateEntity>> invoke() {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        if (currentUser == null) {
            return new MutableLiveData<>(null);
        } else {
            return Transformations.map(workmateRepository.getWorkmatesLiveData(), workmateEntities -> {
                List<WorkmateEntity> filteredWorkmateEntities = new ArrayList<>(workmateEntities.size());

                for (WorkmateEntity workmateEntity : workmateEntities) {
                    if (!workmateEntity.getId().equals(currentUser.getId())) {
                        filteredWorkmateEntities.add(workmateEntity);
                    }
                }

                return filteredWorkmateEntities;
            });
        }
    }


    public interface WorkmateRepository {
        LiveData<List<WorkmateEntity>> getWorkmatesLiveData();
    }
}
