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

    public void setNewUserName(String newUserName) {
        if (getFirebaseUser() != null) {
            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (!getFirebaseUser().getDisplayName().equals(newUserName)) {
                // Update user name in firebase auth
                profileUpdatesBuilder.setDisplayName(newUserName);
                UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

                getFirebaseUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update user name in firestore
                            firebaseFirestore
                                .collection(USERS)
                                .document(getFirebaseUser().getUid())
                                .update(NAME, newUserName)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
                        }
                    });
            }
        }
    }


    public void deleteAccount() {

        if (getFirebaseUser() != null) {
            getFirebaseUser().delete();
            firebaseFirestore.collection(USERS)
                .document(getFirebaseUser().getUid())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Firestore User successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Firestore Error deleting user", e));
            FirebaseAuth.getInstance().signOut();
        }
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

}
