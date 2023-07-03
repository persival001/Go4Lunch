package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.PlacesRepository;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetNearbyRestaurantsUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    PlacesRepository placesRepository;

    @Mock
    GetLocationUseCase getLocationUseCase;

    GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;

    @Before
    public void setUp() {
        getNearbyRestaurantsUseCase = new GetNearbyRestaurantsUseCase(placesRepository, getLocationUseCase);
    }

    @Test
    public void getNearbyRestaurants_withSuccess() {
        // Given
        LocationEntity locationEntity = new LocationEntity(45.76, 4.85);
        LiveData<LocationEntity> liveDataLocation = new MutableLiveData<>(locationEntity);

        NearbyRestaurantsEntity restaurant = new NearbyRestaurantsEntity(
            "id",
            "name",
            "url",
            true,
            4.5f,
            new ArrayList<>(),
            47,
            5
        );
        List<NearbyRestaurantsEntity> restaurantList = Collections.singletonList(restaurant);
        LiveData<List<NearbyRestaurantsEntity>> liveDataRestaurant = new MutableLiveData<>(restaurantList);

        when(getLocationUseCase.invoke()).thenReturn(liveDataLocation);
        when(placesRepository.getNearbyRestaurants(anyString(), anyInt(), anyString(), anyString())).thenReturn(liveDataRestaurant);

        // When
        LiveData<List<NearbyRestaurantsEntity>> result = getNearbyRestaurantsUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), restaurantList);
    }

    @Test
    public void getNearbyRestaurants_withNullLocation() {
        // Given
        LocationEntity locationEntity = new LocationEntity(0.0, 0.0);
        LiveData<LocationEntity> liveDataLocation = new MutableLiveData<>(locationEntity);
        when(getLocationUseCase.invoke()).thenReturn(liveDataLocation);

        // When
        LiveData<List<NearbyRestaurantsEntity>> result = getNearbyRestaurantsUseCase.invoke();

        // Then
        assertNull(TestUtil.getValueForTesting(result));
    }

}