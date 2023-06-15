package com.persival.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TestUtil {

    public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
        liveData.observeForever(t -> {
        });

        return liveData.getValue();
    }
}