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

    public void setNewUserName(String newUserName) {
        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

        if (!Objects.equals(firebaseAuth.getCurrentUser().getDisplayName(), newUserName)) {
            // Update user name in firebase auth
            profileUpdatesBuilder.setDisplayName(newUserName);
            UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update user name in firestore
                        firebaseFirestore
                            .collection(USERS)
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .update(NAME, newUserName)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
                    }
                });
        }
    }


    public void deleteAccount() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser.getUid())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Firestore User successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Firestore Error deleting user", e))
                .addOnCompleteListener(deleteFromFirestoreTask -> {
                    if (deleteFromFirestoreTask.isSuccessful()) {
                        firebaseUser.delete().addOnCompleteListener(deleteFromFirebaseTask -> {
                            if (deleteFromFirebaseTask.isSuccessful()) {
                                firebaseAuth.signOut();
                            } else {
                                Log.e(TAG, "Firebase Auth: Error deleting user", deleteFromFirebaseTask.getException());
                            }
                        });
                    } else {
                        Log.e(TAG, "Firestore: Error deleting user", deleteFromFirestoreTask.getException());
                    }
                });
        }
    }
}
