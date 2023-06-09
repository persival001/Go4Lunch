package com.persival.go4lunch.data.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.persival.go4lunch.domain.restaurant.model.UserEatAtRestaurantEntity;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository implements UserRepository {
    @NonNull
    private static final String USERS = "users";
    @NonNull
    private static final String NAME = "name";
    @NonNull
    private static final String USER_EAT_AT_RESTAURANT = "userEatAtRestaurant";
    @NonNull
    private static final String TAG = "FirestoreRepository";
    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
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
                    UserDto userDto = doc.toObject(UserDto.class);

                    if (userDto.getId() != null && userDto.getWorkmateName() != null) {
                        WorkmateEntity workmateEntity = new WorkmateEntity(
                            userDto.getId(),
                            userDto.getWorkmatePictureUrl(),
                            userDto.getWorkmateName(),
                            userDto.getLikedRestaurantsId() == null ? new ArrayList<>() : userDto.getLikedRestaurantsId()
                        );

                        workmates.add(workmateEntity);
                    }
                }
                workmatesLiveData.setValue(workmates);
            });
        return workmatesLiveData;
    }

    // ----- Create new workmate -----
    public void createUser(WorkmateEntity user) {
        firebaseFirestore.collection(USERS).document(user.getId())
            .set(user)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    // ----- Update workmate information -----
    public void updateWorkmateInformation(LoggedUserEntity user, List<String> newLikedRestaurantIds, String eatingRestaurantId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update user liked restaurants
        db.collection(USERS).document(user.getId())
            .update("likedRestaurantsId", newLikedRestaurantIds)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User liked restaurants updated successfully."))
            .addOnFailureListener(e -> Log.w(TAG, "Error updating user liked restaurants", e));

        // Create new instance of UserRestaurantRelation
        UserEatAtRestaurantEntity userEatAtRestaurantEntity = new UserEatAtRestaurantEntity(
            user.getId(),
            eatingRestaurantId,
            new Date());

        // Add userEatAtRestaurant at the collection 'userEatAtRestaurant'
        db.collection(USER_EAT_AT_RESTAURANT).document()
            .set(userEatAtRestaurantEntity)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User restaurant relation added successfully."))
            .addOnFailureListener(e -> Log.w(TAG, "Error adding user restaurant relation", e));
    }


    // ----- Change user name -----
    public void setNewUserName(@NonNull String userId, @NonNull String newUserName) {
        firebaseFirestore
            .collection(USERS)
            .document(userId)
            .update(NAME, newUserName)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
    }

    // ----- Delete account -----
    public void deleteAccount() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser.getUid())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Firestore User successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Firestore Error deleting user", e));
        }
    }

}
