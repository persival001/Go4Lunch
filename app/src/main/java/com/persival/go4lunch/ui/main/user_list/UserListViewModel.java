package com.persival.go4lunch.ui.main.user_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.User;
import com.persival.go4lunch.data.places.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserListViewModel extends ViewModel {
    private FirestoreRepository firestoreRepository;

    @Inject
    public UserListViewModel(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
    }


    public LiveData<List<UserListViewState>> populateUserListLiveData() {
        return Transformations.map(firestoreRepository.getAllUsers(), users -> {
            List<UserListViewState> userListViewState = new ArrayList<>();

            for (User user : users) {
                userListViewState.add(
                    new UserListViewState(
                        user.getuId(),
                        user.getName(),
                        user.getEmailAddress(),
                        user.getAvatarPictureUrl()
                    )
                );

            }

            return userListViewState;
        });
    }


}