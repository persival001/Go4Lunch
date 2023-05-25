package com.persival.go4lunch.ui.authentication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;
    private final GetLoggedUserUseCase getLoggedUserUseCase;


    @Inject
    public AuthenticationViewModel(
        @NonNull final FirestoreRepository firestoreRepository,
        GetLoggedUserUseCase getLoggedUserUseCase) {
        this.firestoreRepository = firestoreRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LoggedUserEntity getLoggedUser() {
        return getLoggedUserUseCase.invoke();
    }

    /*public void setFirestoreUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firestoreRepository.setFirestoreUser(getLoggedUser());
        } else {
            Toast.makeText(null, "No user found", Toast.LENGTH_SHORT).show();
        }
    }*/
}
