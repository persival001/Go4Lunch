package com.persival.go4lunch.ui.main.user_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.FirestoreUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserListViewModel extends ViewModel {
    private final FirestoreRepository firestoreRepository;

    @Inject
    public UserListViewModel(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
    }

    public LiveData<List<UserListViewState>> populateUserListLiveData() {
        return Transformations.map(firestoreRepository.getAllUsers(), users -> {
            List<UserListViewState> userListViewState = new ArrayList<>();

            for (FirestoreUser firestoreUser : users) {
                userListViewState.add(
                    new UserListViewState(
                        firestoreUser.getuId(),
                        firestoreUser.getName(),
                        firestoreUser.getAvatarPictureUrl(),
                        firestoreUser.getAvatarPictureUrl()
                    )
                );

            }

            return userListViewState;
        });
    }


}