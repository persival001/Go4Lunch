package com.persival.go4lunch.ui.main.settings;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.User;
import com.persival.go4lunch.ui.main.details.DetailsUserViewState;
import com.persival.go4lunch.ui.main.details.DetailsViewState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;

    private MutableLiveData<List<DetailsUserViewState>> userListLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(
        @NonNull final FirestoreRepository firestoreRepository
    ) {
        this.firestoreRepository = firestoreRepository;
    }

    public LiveData<SettingsViewState> getFirestoreUserViewStateLiveData() {
        return Transformations.map(
            firestoreRepository.getFirestoreUser(),

            user -> new SettingsViewState(
                user.getuId() != null ? user.getuId() : "",
                user.getAvatarPictureUrl() != null ? user.getAvatarPictureUrl() : "",
                user.getName() != null ? user.getName() : "",
                user.getEmailAddress() != null ? user.getEmailAddress() : ""
            )
        );
    }

    public void getFirestoreUser() {
        firestoreRepository.getFirestoreUser();
    }
}
