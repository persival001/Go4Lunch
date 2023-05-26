package com.persival.go4lunch.ui.main.user_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.domain.workmate.GetWorkmateUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserListViewModel extends ViewModel {
    private final FirestoreRepository firestoreRepository;
    private final LiveData<List<WorkmateEntity>> workmatesLiveData;

    @Inject
    public UserListViewModel(
        @NonNull FirestoreRepository firestoreRepository,
        @NonNull GetWorkmateUseCase getWorkmateUseCase
    ) {
        this.firestoreRepository = firestoreRepository;

        this.workmatesLiveData = getWorkmateUseCase.invoke();
    }

    public LiveData<List<UserListViewState>> populateUserListLiveData() {
        return Transformations.map(workmatesLiveData, users -> {
            List<UserListViewState> userListViewState = new ArrayList<>();

            for (WorkmateEntity workmateEntity : users) {
                userListViewState.add(
                    new UserListViewState(
                        workmateEntity.getId(),
                        workmateEntity.getUsername(),
                        workmateEntity.getEmail(),
                        workmateEntity.getPhotoUrl()
                    )
                );
            }

            return userListViewState;
        });
    }
}