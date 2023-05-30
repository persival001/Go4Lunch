package com.persival.go4lunch.data.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

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

    private FirebaseUser getFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

    public LiveData<LoggedUserEntity> getFirestoreUser() {
        MutableLiveData<UserDto> firestoreUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = getFirebaseUser();

        if (firebaseUser != null) {
            firebaseFirestore
                .collection(USERS)
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserDto userDto = documentSnapshot.toObject(UserDto.class);
                        firestoreUserLiveData.setValue(userDto);
                    }
                });
        }

        return Transformations.map(firestoreUserLiveData, userDto -> {
            if (userDto == null) {
                return null;
            } else {
                return new LoggedUserEntity(
                    userDto.getId(),
                    userDto.getName(),
                    userDto.getEmailAddress(),
                    userDto.getAvatarPictureUrl()
                );
            }
        });
    }


    public LoggedUserEntity getLoggedUser() {
        FirebaseUser firebaseUser = getFirebaseUser();

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

                if (value == null) {
                    Log.w(TAG, "Snapshot data is null");
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


    public void setNewUserName(String username) {
        FirebaseUser firebaseUser = getFirebaseUser();

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
                        // Update user name in firestore
                        firebaseFirestore
                            .collection(USERS)
                            .document(getLoggedUser().getId())
                            .set(getLoggedUser())
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));

                        // Update user name in firebase auth
                        setNewUserNameInFirebaseAuth(username);
                    }
                });
        }
    }

    public void deleteAccount() {
        FirebaseUser firebaseUser = getFirebaseUser();

        if (firebaseUser != null) {
            firebaseUser.delete();
            firebaseFirestore.collection(USERS)
                .document(firebaseUser.getUid())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Firestore User successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Firestore Error deleting user", e));
            FirebaseAuth.getInstance().signOut();
        }
    }

    public void setNewUserNameInFirebaseAuth(String username) {
        FirebaseUser firebaseUser = getFirebaseUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build();

        if (firebaseUser != null) {
            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });
        }
    }

}
