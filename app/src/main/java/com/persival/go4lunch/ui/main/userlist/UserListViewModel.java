package com.persival.go4lunch.ui.main.userlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.repository.GooglePlacesRepository;

public class UserListViewModel extends ViewModel {
    private final GooglePlacesRepository googlePlacesRepository;
    // TODO: Implement the ViewModel

    public UserListViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
    }
}