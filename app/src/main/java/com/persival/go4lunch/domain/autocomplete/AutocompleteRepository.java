package com.persival.go4lunch.domain.autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public interface AutocompleteRepository {
    LiveData<List<AutocompleteEntity>> getAutocompletesLiveData(
        @NonNull String query,
        int radius
    );
}
