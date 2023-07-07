package com.persival.go4lunch.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.autocomplete.AutocompleteEntity;
import com.persival.go4lunch.domain.autocomplete.GetAutocompletesUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetRestaurantIdForCurrentUserUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.domain.user.GetUserNameChangedUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.ui.main.MainViewModel;
import com.persival.go4lunch.ui.main.MainViewState;
import com.persival.go4lunch.ui.main.MainViewStateAutocomplete;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    private final MutableLiveData<List<NearbyRestaurantsEntity>> nearbyRestaurants = new MutableLiveData<>();
    private final List<NearbyRestaurantsEntity> restaurantList = new ArrayList<>();
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase;
    @Mock
    private GetUserNameChangedUseCase getUserNameChangedUseCase;
    @Mock
    private GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;
    @Mock
    private GetAutocompletesUseCase getAutocompletesUseCase;
    private MainViewModel viewModel;

    @Before
    public void setUp() {
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity("1", "Emilie", "https://image.com", "emilie@test.com");

        List<String> photos = new ArrayList<>();
        restaurantList.add(new NearbyRestaurantsEntity("1", "Restaurant 1", "Image 1", true, 3.5f, photos, 48.0, 2.0));
        restaurantList.add(new NearbyRestaurantsEntity("2", "Not Matching", "Image 2", false, 4f, photos, 48.2, 2.2));
        restaurantList.add(new NearbyRestaurantsEntity("3", "Restaurant 3", "Image 3", true, 5f, photos, 48.4, 2.4));

        when(getUserNameChangedUseCase.invoke()).thenReturn(new MutableLiveData<>(loggedUserEntity));
        when(getRestaurantIdForCurrentUserUseCase.invoke()).thenReturn(new MutableLiveData<>("123"));

        viewModel = new MainViewModel(
            getRestaurantIdForCurrentUserUseCase,
            getUserNameChangedUseCase,
            getNearbyRestaurantsUseCase,
            getAutocompletesUseCase
        );
    }

    @Test
    public void testGetAuthenticatedUserLiveData() {
        // Given

        // When
        MainViewState result = TestUtil.getValueForTesting(viewModel.getAuthenticatedUserLiveData());

        // Then
        assertEquals("1", result.getId());
        assertEquals("Emilie", result.getAvatarName());
        assertEquals("emilie@test.com", result.getEmail());
        assertEquals("https://image.com", result.getAvatarPictureUrl());
    }

    @Test
    public void testGetAuthenticatedUserLiveDataFailure() {
        // Given
        when(getUserNameChangedUseCase.invoke()).thenReturn(new MutableLiveData<>(null));
        viewModel = new MainViewModel(getRestaurantIdForCurrentUserUseCase, getUserNameChangedUseCase, getNearbyRestaurantsUseCase, getAutocompletesUseCase);

        // When
        MainViewState result = TestUtil.getValueForTesting(viewModel.getAuthenticatedUserLiveData());

        // Then
        assertNull(result);
    }

    @Test
    public void testGetRestaurantIdForCurrentUserLiveDataSuccess() {
        // Given
        MutableLiveData<String> restaurantIdLiveData = new MutableLiveData<>("123");
        when(getRestaurantIdForCurrentUserUseCase.invoke()).thenReturn(restaurantIdLiveData);

        // When
        String result = TestUtil.getValueForTesting(viewModel.getRestaurantIdForCurrentUserLiveData());

        // Then
        assertEquals("123", result);
    }

    @Test
    public void testGetRestaurantIdForCurrentUserLiveDataFailure() {
        // Given
        when(getRestaurantIdForCurrentUserUseCase.invoke()).thenReturn(new MutableLiveData<>(null));
        viewModel = new MainViewModel(getRestaurantIdForCurrentUserUseCase, getUserNameChangedUseCase, getNearbyRestaurantsUseCase, getAutocompletesUseCase);

        // When
        String result = TestUtil.getValueForTesting(viewModel.getRestaurantIdForCurrentUserLiveData());

        // Then
        assertNull(result);
    }

    @Test
    public void testGetAutocompletesLiveDataSuccess() {
        // Given
        MutableLiveData<List<AutocompleteEntity>> autocompleteLiveData = new MutableLiveData<>();
        List<AutocompleteEntity> autocompleteEntities = new ArrayList<>();
        autocompleteEntities.add(new AutocompleteEntity("1", "Restaurant 1"));
        autocompleteEntities.add(new AutocompleteEntity("2", "Restaurant 2"));
        autocompleteLiveData.setValue(autocompleteEntities);
        when(getAutocompletesUseCase.invoke(anyString())).thenReturn(autocompleteLiveData);
        viewModel.updateSearchString("Res");

        // When
        List<MainViewStateAutocomplete> result = TestUtil.getValueForTesting(viewModel.getAutocompletesLiveData());

        // Then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getPlaceId());
        assertEquals("Restaurant 1", result.get(0).getName());
        assertEquals("2", result.get(1).getPlaceId());
        assertEquals("Restaurant 2", result.get(1).getName());
    }

    @Test
    public void testGetSelectedRestaurantLiveDataSuccess() {
        // Given
        MutableLiveData<List<NearbyRestaurantsEntity>> nearbyRestaurantsLiveData = new MutableLiveData<>(restaurantList);
        when(getNearbyRestaurantsUseCase.invoke()).thenReturn(nearbyRestaurantsLiveData);
        viewModel.onAutocompleteSelected(new MainViewStateAutocomplete("1", "Restaurant 1"));

        // When
        NearbyRestaurantsEntity result = TestUtil.getValueForTesting(viewModel.getSelectedRestaurantLiveData());

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Restaurant 1", result.getName());
    }

}

