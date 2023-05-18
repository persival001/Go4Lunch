package com.persival.go4lunch.ui.authentication;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.UserDto;

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
        if (firebaseUser != null) {
            String uId = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String avatarPictureUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;
            boolean isAtRestaurant = false;
            boolean isFavoriteRestaurant = false;

            UserDto userDto = new UserDto(uId, name, avatarPictureUrl, isAtRestaurant, isFavoriteRestaurant);
            firestoreRepository.setFirestoreUser(userDto);
        } else {
            Toast.makeText(null, "No user found", Toast.LENGTH_SHORT).show();
        }
    }
}
