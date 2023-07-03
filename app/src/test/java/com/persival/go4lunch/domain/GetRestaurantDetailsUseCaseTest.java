package com.persival.go4lunch.domain;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.details.GetRestaurantDetailsUseCase;
import com.persival.go4lunch.domain.restaurant.PlacesRepository;
import com.persival.go4lunch.domain.restaurant.model.PlaceDetailsEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class GetRestaurantDetailsUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private PlacesRepository placesRepository;

    private GetRestaurantDetailsUseCase getRestaurantDetailsUseCase;

    @Before
    public void setUp() {
        getRestaurantDetailsUseCase = new GetRestaurantDetailsUseCase(placesRepository);
    }

    @Test
    public void getRestaurantDetailsUseCase_withSuccess() {
        // Given
        String restaurantId = "1";
        PlaceDetailsEntity placeDetailsEntity = new PlaceDetailsEntity(
            "1",
            new ArrayList<>(),
            "resto",
            3.4f,
            "rue du resto",
            "1234",
            "https://resto.com"
        );

        MutableLiveData<PlaceDetailsEntity> liveData = new MutableLiveData<>();
        liveData.setValue(placeDetailsEntity);

        // When
        when(placesRepository.getRestaurantLiveData(restaurantId, MAPS_API_KEY)).thenReturn(liveData);

        // Then
        assertEquals(getRestaurantDetailsUseCase.invoke(restaurantId).getValue(), placeDetailsEntity);
    }

    @Test
    public void getRestaurantDetailsUseCase_noResponse() {
        // Given
        String restaurantId = "1";

        // When
        when(placesRepository.getRestaurantLiveData(restaurantId, MAPS_API_KEY)).thenReturn(null);

        // Then
        assertNull(getRestaurantDetailsUseCase.invoke(restaurantId));
    }

}
