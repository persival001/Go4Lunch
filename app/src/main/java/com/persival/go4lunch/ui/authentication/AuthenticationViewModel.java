package com.persival.go4lunch.ui.authentication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.workmate.CreateWorkmateUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel {
    @NonNull
    private final CreateWorkmateUseCase createWorkmateUseCase;

    @Inject
    public AuthenticationViewModel(
        @NonNull CreateWorkmateUseCase createWorkmateUseCase
    ) {
        this.createWorkmateUseCase = createWorkmateUseCase;
    }

    public void createNewWorkmate() {
        createWorkmateUseCase.invoke();
    }
}
