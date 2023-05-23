package com.persival.go4lunch.data.firestore;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository {

    private static final String USERS = "users";

    @Inject
    FirestoreRepository() {
    }

    public LiveData<UserDto> getFirestoreUser() {
        MutableLiveData<UserDto> firestoreUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firestoreUserLiveData.setValue(documentSnapshot.toObject(UserDto.class));
                    }
                });
        }
        return firestoreUserLiveData;
    }

    public void setFirestoreUser(LoggedUserEntity loggedUserEntity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS)
            .document(loggedUserEntity.getId())
            .set(loggedUserEntity)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
    }

    public LiveData<List<UserDto>> getAllUsers() {
        MutableLiveData<List<UserDto>> usersLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS)
            .addSnapshotListener((value, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<UserDto> userDtos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    userDtos.add(doc.toObject(UserDto.class));
                }
                usersLiveData.setValue(userDtos);
            });
        return usersLiveData;
    }

    public LiveData<LoggedUserEntity> getAuthenticatedUser() {
        MutableLiveData<LoggedUserEntity> authenticatedUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String id = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            Uri photoUri = firebaseUser.getPhotoUrl();
            String photoUrl = (photoUri != null) ? photoUri.toString() : "";
            if (name != null && email != null) {
                LoggedUserEntity loggedUserEntity = new LoggedUserEntity(id, name, email, photoUrl);

                authenticatedUserLiveData.setValue(loggedUserEntity);
            }
        }
        return authenticatedUserLiveData;
    }
}
