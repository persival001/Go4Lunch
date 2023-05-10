package com.persival.go4lunch.ui.main.user_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.repository.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserListViewModel extends ViewModel {
    private final GooglePlacesRepository googlePlacesRepository;
    private final MutableLiveData<List<UserListViewState>> userListLiveData;

    @Inject
    public UserListViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
        userListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<UserListViewState>> populateUserListLiveData() {
        List<UserListViewState> usersList = new ArrayList<>();
        usersList.add(new UserListViewState(
                "1",
                "https://picsum.photos/200",
                "John",
                "Oh Resto !"
            )
        );
        usersList.add(new UserListViewState(
                "2",
                "https://picsum.photos/200",
                "Jane",
                "Once trimballe"
            )
        );

        userListLiveData.setValue(usersList);
        return userListLiveData;
    }

}