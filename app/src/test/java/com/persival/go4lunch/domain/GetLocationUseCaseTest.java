package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetLocationUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    LocationDataRepository locationRepository;

    GetLocationUseCase getLocationUseCase;

    @Before
    public void setUp() {
        getLocationUseCase = new GetLocationUseCase(locationRepository);
    }

    @Test
    public void getLocation_withSuccess() {
        // Given
        LocationEntity locationEntity = new LocationEntity(0.0, 0.0);
        MutableLiveData<LocationEntity> liveData = new MutableLiveData<>();
        liveData.setValue(locationEntity);
        when(locationRepository.getLocationLiveData()).thenReturn(liveData);

        // When
        LiveData<LocationEntity> result = getLocationUseCase.invoke();

        // Then
        assertEquals(result.getValue(), locationEntity);
    }

    @Test
    public void getLocation_withNull() {
        // Given
        MutableLiveData<LocationEntity> liveData = new MutableLiveData<>();
        liveData.setValue(null);
        when(locationRepository.getLocationLiveData()).thenReturn(liveData);

        // When
        LiveData<LocationEntity> result = getLocationUseCase.invoke();

        // Then
        assertNull(result.getValue());
    }
}
