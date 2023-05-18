package com.persival.go4lunch.data.firestore;

import static android.content.ContentValues.TAG;

import android.net.Uri;
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

    @Inject
    FirestoreRepository() {
    }

    public LiveData<FirestoreUser> getFirestoreUser() {
        MutableLiveData<FirestoreUser> firestoreUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firestoreUserLiveData.setValue(documentSnapshot.toObject(FirestoreUser.class));
                    }
                });
        }
        return firestoreUserLiveData;
    }

    public void setFirestoreUser(FirestoreUser firestoreUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS)
            .document(firestoreUser.getuId())
            .set(firestoreUser)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully written!"))
            .addOnFailureListener(e -> Log.w(TAG, "Error writing user", e));
    }

    public LiveData<List<FirestoreUser>> getAllUsers() {
        MutableLiveData<List<FirestoreUser>> usersLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(USERS)
            .addSnapshotListener((value, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<FirestoreUser> firestoreUsers = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    firestoreUsers.add(doc.toObject(FirestoreUser.class));
                }
                usersLiveData.setValue(firestoreUsers);
            });
        return usersLiveData;
    }

    public LiveData<AuthenticatedUser> getAuthenticatedUser() {
        MutableLiveData<AuthenticatedUser> authenticatedUserLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            Uri photoUri = firebaseUser.getPhotoUrl();
            String photoUrl = (photoUri != null) ? photoUri.toString() : "";
            if (name != null && email != null) {
                AuthenticatedUser authenticatedUser = new AuthenticatedUser(uid, name, email, photoUrl);

                authenticatedUserLiveData.setValue(authenticatedUser);
            }
        }
        return authenticatedUserLiveData;
    }
}
