package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;
import com.persival.go4lunch.domain.workmate.model.UserEntity;

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
    private static final String LIKED_RESTAURANT_ID = "likedRestaurantsId";
    @NonNull
    private static final String RESTAURANT_ID = "restaurantId";
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

    public LiveData<List<UserEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData() {
        MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(USER_EAT_AT_RESTAURANT)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    return;
                }

                if (snapshots != null) {
                    List<UserEatAtRestaurantEntity> workmates = new ArrayList<>();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        UserEatAtRestaurantDto userEatAtRestaurantDto = document.toObject(UserEatAtRestaurantDto.class);
                        if (userEatAtRestaurantDto != null
                            && userEatAtRestaurantDto.getId() != null
                            && userEatAtRestaurantDto.getName() != null
                            && userEatAtRestaurantDto.getPictureUrl() != null) {

                            UserEatAtRestaurantEntity userEatAtRestaurantEntity = new UserEatAtRestaurantEntity(
                                userEatAtRestaurantDto.getId(),
                                userEatAtRestaurantDto.getName(),
                                userEatAtRestaurantDto.getPictureUrl(),
                                userEatAtRestaurantDto.getRestaurantId(),
                                userEatAtRestaurantDto.getRestaurantName(),
                                userEatAtRestaurantDto.getDateOfVisit());

                            workmates.add(userEatAtRestaurantEntity);
                        }
                    }
                    workmatesEatAtRestaurantLiveData.setValue(workmates);
                }
            });

        return workmatesEatAtRestaurantLiveData;
    }

    // ----- Create new workmate -----
    public void createUser(@NonNull UserEntity user) {
        firebaseFirestore.collection(USERS).document(user.getId())
            .set(user);
    }

    // ----- Update workmate information -----
    public void updateWorkmateInformation(
        @NonNull LoggedUserEntity loggedUserEntity,
        @NonNull String eatingRestaurantId,
        @NonNull String eatingRestaurantName
    ) {
        UserEatAtRestaurantEntity userEatAtRestaurantEntity = new UserEatAtRestaurantEntity(
            loggedUserEntity.getId(),
            loggedUserEntity.getName(),
            loggedUserEntity.getPictureUrl() != null ? loggedUserEntity.getPictureUrl() : "",
            eatingRestaurantId,
            eatingRestaurantName,
            new Date());

        firebaseFirestore.collection(USER_EAT_AT_RESTAURANT).document(loggedUserEntity.getId())
            .set(userEatAtRestaurantEntity);
    }

    public void updateLikedRestaurants(@NonNull LoggedUserEntity user, boolean isAdded, @NonNull String restaurantId) {
        DocumentReference docRef = firebaseFirestore.collection(USERS).document(user.getId());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> likedRestaurantsId = (List<String>) document.get(LIKED_RESTAURANT_ID);

                    if (isAdded) {
                        // Add id of liked restaurant to the list
                        firebaseFirestore.collection(USERS).document(user.getId())
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayUnion(restaurantId));
                        if (likedRestaurantsId != null) {
                            likedRestaurantsId.add(restaurantId);
                        }
                    } else {
                        // Erase id of liked restaurant from the list
                        firebaseFirestore.collection(USERS).document(user.getId())
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayRemove(restaurantId));
                        if (likedRestaurantsId != null) {
                            likedRestaurantsId.remove(restaurantId);
                        }
                    }
                }
            }
        });
    }

    // ----- Get liked restaurants -----
    public LiveData<List<String>> getLikedRestaurantsForUser(@NonNull String userId) {
        MutableLiveData<List<String>> likedRestaurants = new MutableLiveData<>();
        firebaseFirestore.collection(USERS).document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                List<String> restaurants = (List<String>) documentSnapshot.get(LIKED_RESTAURANT_ID);
                likedRestaurants.setValue(restaurants);
            });
        return likedRestaurants;
    }

    // ----- Change user name -----
    public void setNewUserName(@NonNull String userId, @NonNull String newUserName) {
        firebaseFirestore
            .collection(USERS)
            .document(userId)
            .update(NAME, newUserName);
    }

    // ----- Delete account -----
    public void deleteAccount() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser.getUid())
                .delete();
        }
        firebaseAuth.signOut();
    }

    // ----- Get restaurantId for current user -----
    public LiveData<String> getRestaurantIdForCurrentUser() {
        MutableLiveData<String> restaurantIdLiveData = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            firebaseFirestore.collection(USER_EAT_AT_RESTAURANT)
                .document(currentUserId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String restaurantId = documentSnapshot.getString(RESTAURANT_ID);
                        restaurantIdLiveData.setValue(restaurantId);
                    }
                });
        }

        return restaurantIdLiveData;
    }

}
