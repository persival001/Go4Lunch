package com.persival.go4lunch.ui.authentication;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.User;
import com.persival.go4lunch.ui.main.details.DetailsUserViewState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;

    @Inject
    public AuthenticationViewModel(
        @NonNull final FirestoreRepository firestoreRepository
    ) {
        this.firestoreRepository = firestoreRepository;
    }

    public void setFirestoreUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String uId = firebaseUser.getUid() ;
        String name = firebaseUser.getDisplayName();
        String eMailAddress = firebaseUser.getEmail();
        String avatarPictureUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;

        User user = new User(uId, name, eMailAddress, avatarPictureUrl);
        firestoreRepository.setFirestoreUser(user);
    }
}
