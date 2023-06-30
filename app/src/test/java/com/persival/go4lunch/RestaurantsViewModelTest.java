package com.persival.go4lunch;

import static com.persival.go4lunch.utils.TestUtil.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
    private RestaurantsViewState sortedRestaurant;
    private List<NearbyRestaurantsEntity> nearbyRestaurants;
    private List<RestaurantsViewState> sortedRestaurants;
    private HashMap<String, Integer> participantsMap;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        locationEntity = new LocationEntity(48.8566, 2.3522);
        nearbyRestaurants = new ArrayList<>();
        sortedRestaurants = new ArrayList<>();
        participantsMap = new HashMap<>();
        restaurant = new NearbyRestaurantsEntity(
            "1",
            "restaurant",
            "Rue du bois",
            true,
            2,
            new ArrayList<>(),
            48.8590,
            2.3470
        );
        sortedRestaurant = new RestaurantsViewState(
            "1",
            "restaurant",
            "Rue du bois",
            true,
            "200",
            "2",
            2,
            "https://image.com"
        );

        when(getLocationUseCase.invoke()).thenReturn(new MutableLiveData<>(locationEntity));
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(nearbyRestaurants));
        when(getParticipantsUseCase.invoke()).thenReturn(new MutableLiveData<>(participantsMap));
        when(isGpsActivatedUseCase.invoke()).thenReturn(new MutableLiveData<>(true));
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(nearbyRestaurants));

        viewModel = new RestaurantsViewModel(
            getLocationUseCase,
            isGpsActivatedUseCase,
            getNearbyRestaurantsUseCase,
            getParticipantsUseCase
        );
    }


    @Test
    public void testGetSortedRestaurantsLiveDataFailure() {
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        List<RestaurantsViewState> result = getValueForTesting(liveData);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateSearchString_success() {
        // Given
        String searchString = "restaurant";
        nearbyRestaurants.add(restaurant);
        participantsMap.put("1", 1);

        // When
        viewModel.updateSearchString(searchString);

        // Then
        List<RestaurantsViewState> filteredList = getValueForTesting(viewModel.getSortedRestaurantsLiveData());
        for (RestaurantsViewState restaurant : filteredList) {
            assertTrue(restaurant.getName().toLowerCase().contains(searchString));
        }
        assertFalse(filteredList.isEmpty());
    }


    @Test
    public void testUpdateSearchString_emptyString() {
        // Given
        // viewModel is already initialized

        // When
        viewModel.updateSearchString("");

        // Then
        List<RestaurantsViewState> filteredList = getValueForTesting(viewModel.getSortedRestaurantsLiveData());
        assertEquals(nearbyRestaurants.size(), filteredList.size());
    }

    @Test
    public void testUpdateSearchString_nullString() {
        // Given
        // viewModel is already initialized

        // When
        viewModel.updateSearchString(null);

        // Then
        List<RestaurantsViewState> filteredList = getValueForTesting(viewModel.getSortedRestaurantsLiveData());
        assertEquals(nearbyRestaurants.size(), filteredList.size());
    }

    @Test
    public void testIsGpsActivatedLiveData_whenGpsIsActivated() {
        // Given
        MutableLiveData<Boolean> gpsActivatedLiveData = new MutableLiveData<>();
        gpsActivatedLiveData.setValue(true);
        when(isGpsActivatedUseCase.invoke()).thenReturn(gpsActivatedLiveData);

        // When
        LiveData<Boolean> result = viewModel.isGpsActivatedLiveData();

        // Then
        assertEquals(Boolean.TRUE, result.getValue());
    }

    @Test
    public void testIsGpsActivatedLiveData_whenGpsIsDeactivated() {
        // Given
        MutableLiveData<Boolean> gpsDeactivatedLiveData = new MutableLiveData<>();
        gpsDeactivatedLiveData.setValue(false);
        when(isGpsActivatedUseCase.invoke()).thenReturn(gpsDeactivatedLiveData);

        // When
        LiveData<Boolean> result = viewModel.isGpsActivatedLiveData();

        // Then
        assertNotEquals(Boolean.TRUE, result.getValue());
    }

}

