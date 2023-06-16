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
import com.persival.go4lunch.domain.restaurant.model.UserRestaurantRelationsEntity;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;
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

    public LiveData<List<WorkmateEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData() {
        MutableLiveData<List<WorkmateEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();
        
        firebaseFirestore.collection(USER_EAT_AT_RESTAURANT)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    return;
                }

                if (snapshots != null) {
                    List<WorkmateEatAtRestaurantEntity> workmates = new ArrayList<>();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        UserEatAtRestaurantDto userEatAtRestaurantDto = document.toObject(UserEatAtRestaurantDto.class);
                        if (userEatAtRestaurantDto != null
                            && userEatAtRestaurantDto.getUserId() != null
                            && userEatAtRestaurantDto.getUserName() != null
                            && userEatAtRestaurantDto.getUserPictureUrl() != null) {

                            WorkmateEatAtRestaurantEntity workmateEatAtRestaurantEntity = new WorkmateEatAtRestaurantEntity(
                                userEatAtRestaurantDto.getUserId(),
                                userEatAtRestaurantDto.getUserPictureUrl(),
                                userEatAtRestaurantDto.getUserName(),
                                userEatAtRestaurantDto.getRestaurantName(),
                                userEatAtRestaurantDto.getRestaurantId(),
                                userEatAtRestaurantDto.getDateOfVisit());

                            workmates.add(workmateEatAtRestaurantEntity);
                        }
                    }
                    workmatesEatAtRestaurantLiveData.setValue(workmates);
                }
            });

        return workmatesEatAtRestaurantLiveData;
    }

    // ----- Create new workmate -----
    public void createUser(@NonNull WorkmateEntity user) {
        firebaseFirestore.collection(USERS).document(user.getId())
            .set(user);
    }

    // ----- Update workmate information -----
    public void updateWorkmateInformation(
        LoggedUserEntity user,
        String eatingRestaurantId,
        String eatingRestaurantName
    ) {
        // Create new instance of UserRestaurantRelation
        UserRestaurantRelationsEntity userRestaurantRelationsEntity = new UserRestaurantRelationsEntity(
            user.getId(),
            user.getName(),
            user.getAvatarPictureUrl(),
            eatingRestaurantId,
            eatingRestaurantName,
            new Date());

        // Add userEatAtRestaurant at the collection 'userEatAtRestaurant'
        firebaseFirestore.collection(USER_EAT_AT_RESTAURANT).document(user.getId())
            .set(userRestaurantRelationsEntity);
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
