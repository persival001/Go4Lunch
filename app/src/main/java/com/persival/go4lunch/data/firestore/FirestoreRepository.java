package com.persival.go4lunch.data.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.persival.go4lunch.domain.user.UserRepository;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository implements UserRepository {

    private static final String USERS = "users";
    private static final String TAG = "FirestoreRepository";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public FirestoreRepository(
        @NonNull FirebaseAuth firebaseAuth,
        @NonNull FirebaseFirestore firebaseFirestore
    ) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<UserDto> getFirebaseUser() {
        MutableLiveData<UserDto> firestoreUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseFirestore  // Replaced FirebaseFirestore.getInstance()
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

    public LoggedUserEntity getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null || firebaseUser.getDisplayName() == null || firebaseUser.getEmail() == null) {
            return null;
        } else {
            return new LoggedUserEntity(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString()
            );
        }
    }

    @Override
    public LiveData<List<WorkmateEntity>> getWorkmatesLiveData() {
        MutableLiveData<List<WorkmateEntity>> workmatesLiveData = new MutableLiveData<>();
        firebaseFirestore
            .collection(USERS)
            .addSnapshotListener((value, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<WorkmateEntity> workmates = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    UserDto userDto = doc.toObject(UserDto.class);

                    WorkmateEntity workmateEntity = new WorkmateEntity(
                        userDto.getId(),
                        userDto.getName(),
                        userDto.getEmailAddress(),
                        userDto.getAvatarPictureUrl()
                    );

                    workmates.add(workmateEntity);
                }
                workmatesLiveData.setValue(workmates);
            });
        return workmatesLiveData;
    }

    /*@Override
    public LiveData<List<WorkmateEntity>> getWorkmatesLiveData() {
        MutableLiveData<List<WorkmateEntity>> workmatesLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseFirestore
                .collection(USERS)
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserDto userDto = documentSnapshot.toObject(UserDto.class);
                        WorkmateEntity workmateEntity = new WorkmateEntity(
                            userDto.getId(),
                            userDto.getName(),
                            userDto.getEmailAddress(),
                            userDto.getAvatarPictureUrl()
                        );
                        workmatesLiveData.setValue(Collections.singletonList(workmateEntity));
                    }
                });
        }
        return workmatesLiveData;
    }*/

    public void setNewUserName(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getLoggedUser() != null && firebaseUser != null) {
            String currentName = getLoggedUser().getName();
            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (!currentName.equals(username)) {
                profileUpdatesBuilder.setDisplayName(username);
            }

            UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update user profile in firestore
                        firebaseFirestore
                            .collection(USERS)
                            .document(getLoggedUser().getId())
                            .set(getLoggedUser())
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));

                        // Update user profile in firebase auth
                        //TODO Persival
                        UserProfileChangeRequest usernameUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                        firebaseUser.updateProfile(usernameUpdate);
                    }

                });
        }
    }

    public LiveData<List<LoggedUserDto>> getAllUsers() {
        MutableLiveData<List<LoggedUserDto>> usersLiveData = new MutableLiveData<>();
        firebaseFirestore
            .collection(USERS)
            .addSnapshotListener((value, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<LoggedUserDto> loggedUserDto = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    loggedUserDto.add(doc.toObject(LoggedUserDto.class));
                }
                usersLiveData.setValue(loggedUserDto);
            });
        return usersLiveData;
    }

}
