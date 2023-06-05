package com.persival.go4lunch.data.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.persival.go4lunch.domain.user.UserRepository;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository implements UserRepository {

    private static final String USERS = "users";
    private static final String NAME = "name";
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

    @NonNull
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
                    WorkmateDto workmateDto = doc.toObject(WorkmateDto.class);

                    if (workmateDto.getId() != null && workmateDto.getWorkmateName() != null) {
                        WorkmateEntity workmateEntity = new WorkmateEntity(
                            workmateDto.getId(),
                            workmateDto.getWorkmatePictureUrl(),
                            workmateDto.getWorkmateName(),
                            workmateDto.getRestaurantId(),
                            workmateDto.getRestaurantName(),
                            workmateDto.getRestaurantAddress()
                        );

                        workmates.add(workmateEntity);
                    }
                }
                workmatesLiveData.setValue(workmates);
            });
        return workmatesLiveData;
    }

    // ----- Change user name -----
    public void setNewUserName(String newUserName) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && !Objects.equals(firebaseUser.getDisplayName(), newUserName)) {
            updateUserNameInFirebaseAuth(firebaseUser, newUserName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    updateUserNameInFirestore(firebaseUser, newUserName);
                } else {
                    Log.e(TAG, "Firebase Auth: Error updating user name", task.getException());
                }
            });
        }
    }

    // ----- Delete account -----
    public void deleteAccount() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            deleteUserFromFirestore(firebaseUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    deleteUserFromFirebaseAuth(firebaseUser);
                } else {
                    Log.e(TAG, "Firestore: Error deleting user", task.getException());
                }
            });
        }
    }

    private Task<Void> updateUserNameInFirebaseAuth(FirebaseUser firebaseUser, String newUserName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(newUserName)
            .build();

        return firebaseUser.updateProfile(profileUpdates);
    }

    private void updateUserNameInFirestore(FirebaseUser firebaseUser, String newUserName) {
        firebaseFirestore
            .collection(USERS)
            .document(firebaseUser.getUid())
            .update(NAME, newUserName)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
    }

    private Task<Void> deleteUserFromFirestore(FirebaseUser firebaseUser) {
        return firebaseFirestore.collection(USERS)
            .document(firebaseUser.getUid())
            .delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Firestore User successfully deleted!"))
            .addOnFailureListener(e -> Log.w(TAG, "Firestore Error deleting user", e));
    }

    private void deleteUserFromFirebaseAuth(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuth.signOut();
            } else {
                Log.e(TAG, "Firebase Auth: Error deleting user", task.getException());
            }
        });
    }

}
