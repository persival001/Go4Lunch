package com.persival.go4lunch.ui.mainactivity.details;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.repository.Repository;

public class DetailsViewModel extends ViewModel {
    private final Repository repository;
    // TODO: Implement the ViewModel

    public DetailsViewModel(
        @NonNull final Repository repository
    ) {
        this.repository = repository;
    }
}