package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    private LocationEntity locationEntity;
    private List<NearbyRestaurantsEntity> nearbyRestaurants;
    private HashMap<String, Integer> participantsMap;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        locationEntity = new LocationEntity(48.8566, 2.3522);
        nearbyRestaurants = new ArrayList<>();
        participantsMap = new HashMap<>();

        when(getLocationUseCase.invoke()).thenReturn(new MutableLiveData<>(locationEntity));
        when(getParticipantsUseCase.invoke()).thenReturn(new MutableLiveData<>(participantsMap));
        when(isGpsActivatedUseCase.invoke()).thenReturn(new MutableLiveData<>(true));

        viewModel = new RestaurantsViewModel(
            getLocationUseCase,
            isGpsActivatedUseCase,
            getNearbyRestaurantsUseCase,
            getParticipantsUseCase
        );
    }


    @Test
    public void testGetSortedRestaurantsLiveData() {
        LiveData<List<RestaurantsViewState>> liveData = viewModel.getSortedRestaurantsLiveData();
        assertNotNull(liveData);
    }

    @Test
    public void testIsGpsActivatedLiveData() {
        LiveData<Boolean> liveData = viewModel.isGpsActivatedLiveData();
        assertNotNull(liveData);
        assertEquals(true, liveData.getValue());
    }
}

