package com.persival.go4lunch.data.firestore;

import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ----- Get the workmate list from Firestore and the relation with the restaurant he eat at -----
    public LiveData<List<WorkmateEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData() {
        MutableLiveData<List<WorkmateEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();
        Map<String, WorkmateDto> usersMap = new HashMap<>();
        Map<String, UserRestaurantRelationsDto> relationsMap = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Listener for USERS collection
        db.collection(USERS)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    return;
                }

                if (snapshots != null) {
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        WorkmateDto user = document.toObject(WorkmateDto.class);
                        usersMap.put(user.getId(), user);
                    }
                    updateWorkmatesLiveData(usersMap, relationsMap, workmatesEatAtRestaurantLiveData);
                }
            });

        // Listener for USER_EAT_AT_RESTAURANT collection
        db.collection(USER_EAT_AT_RESTAURANT)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    return;
                }

                if (snapshots != null) {
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        UserRestaurantRelationsDto relation = document.toObject(UserRestaurantRelationsDto.class);
                        relationsMap.put(relation.getUserId(), relation);
                    }
                    updateWorkmatesLiveData(usersMap, relationsMap, workmatesEatAtRestaurantLiveData);
                }
            });

        return workmatesEatAtRestaurantLiveData;
    }

    private void updateWorkmatesLiveData(Map<String, WorkmateDto> usersMap,
                                         Map<String, UserRestaurantRelationsDto> relationsMap,
                                         MutableLiveData<List<WorkmateEatAtRestaurantEntity>> liveData) {

        List<WorkmateEatAtRestaurantEntity> workmates = new ArrayList<>();

        for (Map.Entry<String, WorkmateDto> entry : usersMap.entrySet()) {
            String userId = entry.getKey();
            WorkmateDto user = entry.getValue();
            UserRestaurantRelationsDto relation = relationsMap.get(userId);

            String restaurantName = null;
            String restaurantId = null;
            if (relation != null) {
                restaurantName = relation.getRestaurantName();
                restaurantId = relation.getRestaurantId();
            }

            WorkmateEatAtRestaurantEntity workmateEatAtRestaurantEntity = new WorkmateEatAtRestaurantEntity(
                user.getId(),
                user.getPictureUrl(),
                user.getName(),
                restaurantName,
                restaurantId
            );
            workmates.add(workmateEatAtRestaurantEntity);
        }

        liveData.setValue(workmates);
    }


    // ----- Create new workmate -----
    public void createUser(WorkmateEntity user) {
        firebaseFirestore.collection(USERS).document(user.getId())
            .set(user);
    }

    // ----- Update workmate information -----
    public void updateWorkmateInformation(
        LoggedUserEntity user,
        String eatingRestaurantId,
        String eatingRestaurantName
    ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create new instance of UserRestaurantRelation
        UserRestaurantRelationsEntity userRestaurantRelationsEntity = new UserRestaurantRelationsEntity(
            user.getId(),
            eatingRestaurantId,
            eatingRestaurantName,
            new Date());

        // Add userEatAtRestaurant at the collection 'userEatAtRestaurant'
        db.collection(USER_EAT_AT_RESTAURANT).document(user.getId())
            .set(userRestaurantRelationsEntity);
    }

    public void updateLikedRestaurants(LoggedUserEntity user, boolean isAdded, String restaurantId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(USERS).document(user.getId());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> likedRestaurantsId = (List<String>) document.get(LIKED_RESTAURANT_ID);

                    if (isAdded) {
                        // Add id of liked restaurant to the list
                        db.collection(USERS).document(user.getId())
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayUnion(restaurantId));
                        likedRestaurantsId.add(restaurantId);
                    } else {
                        // Erase id of liked restaurant from the list
                        db.collection(USERS).document(user.getId())
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayRemove(restaurantId));
                        likedRestaurantsId.remove(restaurantId);
                    }
                }
            }
        });
    }

    // ----- Get liked restaurants -----
    public LiveData<List<String>> getLikedRestaurantsForUser(String userId) {
        MutableLiveData<List<String>> likedRestaurants = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS).document(userId)
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
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String restaurantId = document.getString("restaurantId");
                            Log.e("ID DU RESTO", restaurantId);
                            restaurantIdLiveData.setValue(restaurantId);
                        }
                    } else {
                        Log.e("ID DU RESTO", "Error getting document", task.getException());
                    }
                });

        }
        return restaurantIdLiveData;
    }

}
