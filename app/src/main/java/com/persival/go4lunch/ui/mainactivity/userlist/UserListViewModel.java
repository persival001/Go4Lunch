package com.persival.go4lunch.ui.mainactivity.userlist;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.repository.Repository;

public class UserListViewModel extends ViewModel {
    private final Resources resources;
    private final Repository repository;
    // TODO: Implement the ViewModel

    public UserListViewModel(
        @NonNull final Resources resources,
        @NonNull final Repository repository
    ) {
        this.resources = resources;
        this.repository = repository;
    }
}