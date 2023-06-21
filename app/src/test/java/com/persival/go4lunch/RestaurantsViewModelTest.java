package com.persival.go4lunch;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.persival.go4lunch.utils.TestUtil.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetParticipantsUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetLocationUseCase getLocationUseCase;
    @Mock
    private IsGpsActivatedUseCase isGpsActivatedUseCase;
    @Mock
    private GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;
    @Mock
    private GetParticipantsUseCase getParticipantsUseCase;

    private RestaurantsViewModel viewModel;
    private NearbyRestaurantsEntity restaurant;
    private LocationEntity locationEntity;
    private List<NearbyRestaurantsEntity> nearbyRestaurants;
    private HashMap<String, Integer> participantsMap;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        locationEntity = new LocationEntity(48.8566, 2.3522);
        nearbyRestaurants = new ArrayList<>();
        participantsMap = new HashMap<>();
        restaurant = new NearbyRestaurantsEntity(
            "1",
            "Test Restaurant",
            "Rue du bois",
            true,
            2,
            new ArrayList<>(),
            48.8590,
            2.3470
        );

        when(getLocationUseCase.invoke()).thenReturn(new MutableLiveData<>(locationEntity));
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(nearbyRestaurants));
        when(getParticipantsUseCase.invoke()).thenReturn(new MutableLiveData<>(participantsMap));
        when(isGpsActivatedUseCase.invoke()).thenReturn(new MutableLiveData<>(true));

        viewModel = new RestaurantsViewModel(
            getLocationUseCase,
            isGpsActivatedUseCase,
            getNearbyRestaurantsUseCase,
            getParticipantsUseCase
        );
    }

   /* public void testMapHaversineDistanceSuccess() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());

        assertEquals("465 m", result.get(0).getDistance());

        verifyNoMoreInteractions(isGpsActivatedUseCase);
    }*/

    @Test
    public void testGetSortedRestaurantsLiveDataSuccess() {
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetSortedRestaurantsLiveDataFailure() {
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertTrue(result.isEmpty());

    }

    /*@Test
    public void testSortRestaurantViewStates() {
        // Given
        List<RestaurantsViewState> restaurants = new ArrayList<>();
        restaurants.add(new RestaurantsViewState("1", "Restaurant A", "Address A", true, "400 m", "2", 3f, "http://photoA.com"));
        restaurants.add(new RestaurantsViewState("2", "Restaurant B", "Address B", true, "200 m", "3", 2f, "http://photoB.com"));
        restaurants.add(new RestaurantsViewState("3", "Restaurant C", "Address C", true, "300 m", "1", 1f, "http://photoC.com"));

        // When
        List<RestaurantsViewState> sortedRestaurants = viewModel.sortRestaurantViewStates(restaurants);

        // Then
        assertEquals("2", sortedRestaurants.get(0).getId());  // Restaurant B should be first (200m away)
        assertEquals("3", sortedRestaurants.get(1).getId());  // Restaurant C should be second (300m away)
        assertEquals("1", sortedRestaurants.get(2).getId());  // Restaurant A should be third (400m away)
    }*/


    @Test
    public void testIsGpsActivatedLiveData() {
        LiveData<Boolean> liveData = viewModel.isGpsActivatedLiveData();
        assertNotNull(liveData);
        assertEquals(true, liveData.getValue());
    }

    @Test
    public void testMapHaversineDistanceSuccess() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());

        assertEquals("465 m", result.get(0).getDistance());

        verifyNoMoreInteractions(isGpsActivatedUseCase);
    }


    @Test
    public void testMapHaversineDistanceFailure() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());

        assertEquals("465 m", result.get(0).getDistance());

        verifyNoMoreInteractions(isGpsActivatedUseCase);
    }

    @Test
    public void testMapRating() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());
        assertEquals(1.2f, result.get(0).getRating(), 0.01f);
    }

    @Test
    public void testMapRatingFailure() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());
        assertNotEquals(2.0f, result.get(0).getRating(), 0.01f);
    }


    @Test
    public void testMapPictureUrl() {
        ArrayList<String> photos = new ArrayList<>();
        photos.add("photoReference");
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());
        assertEquals("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=photoReference&key=" + MAPS_API_KEY,
            result.get(0).getPictureUrl());
    }

    @Test
    public void testMapFormattedName() {
        nearbyRestaurants.add(restaurant);
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertEquals(1, result.size());
        assertEquals("Test Restaurant", result.get(0).getName());
    }
}

