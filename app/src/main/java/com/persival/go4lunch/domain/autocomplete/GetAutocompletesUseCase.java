package com.persival.go4lunch.domain.autocomplete;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

public class GetAutocompletesUseCase {

    private static final int AUTOCOMPLETE_SEARCH_RADIUs_METERS = 5000;

    private final AutocompleteRepository autocompleteRepository;

    @Inject
    public GetAutocompletesUseCase(
        AutocompleteRepository autocompleteRepository
    ) {
        this.autocompleteRepository = autocompleteRepository;
    }

    public LiveData<List<AutocompleteEntity>> invoke(String query) {
        return autocompleteRepository.getAutocompletesLiveData(query, AUTOCOMPLETE_SEARCH_RADIUs_METERS);
    }
}
