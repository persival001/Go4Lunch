package com.persival.go4lunch.data.firestore;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreRepository {

    private static final String USERS = "users";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    FirestoreRepository() {
    }

    public LiveData<UserDto> getFirebaseUser() {
        MutableLiveData<UserDto> firestoreUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            FirebaseFirestore.getInstance()
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

    public void setFirestoreUser(LoggedUserDto loggedUserDto) {
        db.collection(USERS)
            .document(loggedUserDto.getId())
            .set(loggedUserDto)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
    }

    public LiveData<List<LoggedUserDto>> getAllUsers() {
        MutableLiveData<List<LoggedUserDto>> usersLiveData = new MutableLiveData<>();
        db.collection(USERS)
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
