package com.persival.go4lunch.data.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    // ----- Get the workmate list from Firestore and the relation with the restaurant he eat at -----
    public LiveData<List<WorkmateEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData() {
        MutableLiveData<List<WorkmateEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();
        List<WorkmateEatAtRestaurantEntity> workmates = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get all users
        db.collection(USERS)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        WorkmateDto user = document.toObject(WorkmateDto.class);

                        // For each user - get the relation
                        Task<DocumentSnapshot> userTask = db.collection(USER_EAT_AT_RESTAURANT).document(user.getId()).get();
                        tasks.add(userTask);
                    }

                    Tasks.whenAllComplete(tasks)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (int i = 0; i < tasks.size(); i++) {
                                    WorkmateDto user = task.getResult().getDocuments().get(i).toObject(WorkmateDto.class);
                                    DocumentSnapshot relationDoc = tasks.get(i).getResult();
                                    String restaurantName = null;
                                    if (relationDoc.exists()) {
                                        UserRestaurantRelationsDto relation = relationDoc.toObject(UserRestaurantRelationsDto.class);
                                        restaurantName = relation.getRestaurantName();
                                    }

                                    WorkmateEatAtRestaurantEntity workmateEatAtRestaurantEntity = new WorkmateEatAtRestaurantEntity(
                                        user.getId(),
                                        user.getPictureUrl(),
                                        user.getName(),
                                        restaurantName
                                    );
                                    Log.d(TAG, "USER: " + user.getId() + " - " + user.getName() + " - " + restaurantName);
                                    workmates.add(workmateEatAtRestaurantEntity);
                                }

                                workmatesEatAtRestaurantLiveData.setValue(workmates);
                            } else {
                                Log.d(TAG, "Error getting user documents: ", task1.getException());
                            }
                        });
                } else {
                    Log.d(TAG, "Error getting user documents: ", task.getException());
                }
            });

        return workmatesEatAtRestaurantLiveData;
    }

    // ----- Create new workmate -----
    public void createUser(WorkmateEntity user) {
        firebaseFirestore.collection(USERS).document(user.getId())
            .set(user)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
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
            .set(userRestaurantRelationsEntity)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User restaurant relation added successfully."))
            .addOnFailureListener(e -> Log.w(TAG, "Error adding user restaurant relation", e));
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
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayUnion(restaurantId))
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "RestaurantId added to liked restaurants successfully."))
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding RestaurantId to liked restaurants", e));
                        likedRestaurantsId.add(restaurantId);
                    } else {
                        // Erase id of liked restaurant from the list
                        db.collection(USERS).document(user.getId())
                            .update(LIKED_RESTAURANT_ID, FieldValue.arrayRemove(restaurantId))
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "RestaurantId removed from liked restaurants successfully."))
                            .addOnFailureListener(e -> Log.w(TAG, "Error removing RestaurantId from liked restaurants", e));
                        likedRestaurantsId.remove(restaurantId);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
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
            })
            .addOnFailureListener(e -> {
                Log.d(TAG, "Liked restaurants not found for user " + userId);
            });
        return likedRestaurants;
    }

    // ----- Get the id of the restaurant where the user is going to eat  -----
    public LiveData<UserRestaurantRelationsEntity> getRestaurantChosenToEat(String userId) {
        MutableLiveData<UserRestaurantRelationsEntity> userEatAtRestaurantLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(USER_EAT_AT_RESTAURANT)
            .document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserRestaurantRelationsDto dto = documentSnapshot.toObject(UserRestaurantRelationsDto.class);

                    // Convert DTO to Entity using the constructor
                    UserRestaurantRelationsEntity entity = new UserRestaurantRelationsEntity(
                        dto.getUserId(),
                        dto.getRestaurantId(),
                        dto.getRestaurantName(),
                        dto.getDateOfVisit()
                    );

                    userEatAtRestaurantLiveData.setValue(entity);
                } else {
                    Log.d(TAG, "No such document");
                }
            })
            .addOnFailureListener(e -> Log.w(TAG, "Error getting document", e));

        return userEatAtRestaurantLiveData;
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
