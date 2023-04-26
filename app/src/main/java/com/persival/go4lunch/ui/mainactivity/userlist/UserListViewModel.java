package com.persival.go4lunch.ui.mainactivity.userlist;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.repository.Repository;

public class UserListViewModel extends ViewModel {
    private final Repository repository;
    // TODO: Implement the ViewModel

    public UserListViewModel(
        @NonNull final Repository repository
    ) {
        this.repository = repository;
    }
}