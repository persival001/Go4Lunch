package com.persival.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TestUtil {
    public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
        liveData.observeForever(t -> {
        });

        return liveData.getValue();
    }

    /*public static <T> int getEmitCountForTesting(@NonNull final SingleLiveEvent<T> singleLiveEvent) {
        final int[] emitCount = {0};
        singleLiveEvent.observeForever(t -> emitCount[0]++);

        return emitCount[0];
    }*/
}
