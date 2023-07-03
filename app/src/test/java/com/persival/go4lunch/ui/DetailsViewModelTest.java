package com.persival.go4lunch.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.persival.go4lunch.domain.details.GetLikedRestaurantsUseCase;
import com.persival.go4lunch.domain.details.GetRestaurantDetailsUseCase;
import com.persival.go4lunch.domain.details.SetLikedRestaurantUseCase;
import com.persival.go4lunch.domain.details.SetRestaurantChosenToEatUseCase;
import com.persival.go4lunch.domain.notifications.CancelRestaurantNotificationUseCase;
import com.persival.go4lunch.domain.notifications.ScheduleRestaurantNotificationUseCase;
import com.persival.go4lunch.domain.restaurant.model.PlaceDetailsEntity;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;
import com.persival.go4lunch.ui.main.details.DetailsFragment;
import com.persival.go4lunch.ui.main.details.DetailsRestaurantViewState;
import com.persival.go4lunch.ui.main.details.DetailsViewModel;
import com.persival.go4lunch.ui.main.details.DetailsWorkmateViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(JUnit4.class)
public class DetailsViewModelTest {
    private final MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesEatAtRestaurant = new MutableLiveData<>();
    private final LiveData<PlaceDetailsEntity> placeDetailsEntities = new MutableLiveData<>();
    private final LiveData<List<UserEatAtRestaurantEntity>> workmatesLiveData = new MutableLiveData<>();
    private final LiveData<List<String>> likedRestaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRestaurantLikedLiveData = new MutableLiveData<>();
    private final String restaurantId = "1";
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DetailsViewModel detailsViewModel;
    @Mock
    private Resources mockResources;
    @Mock
    private GetRestaurantDetailsUseCase getRestaurantDetailsUseCase;
    @Mock
    private SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase;
    @Mock
    private GetLikedRestaurantsUseCase getLikedRestaurantsUseCase;
    @Mock
    private SetLikedRestaurantUseCase setLikedRestaurantUseCase;
    @Mock
    private GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase;
    @Mock
    private GetLoggedUserUseCase getLoggedUserUseCase;
    @Mock
    private ScheduleRestaurantNotificationUseCase scheduleRestaurantNotificationUseCase;
    @Mock
    private CancelRestaurantNotificationUseCase cancelRestaurantNotificationUseCase;
    @Mock
    private SavedStateHandle savedStateHandle;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
            "1",
            "Emilie",
            "https://image.com",
            "001"
        );

        savedStateHandle = new SavedStateHandle();
        savedStateHandle.set(DetailsFragment.KEY_RESTAURANT_ID, restaurantId);

        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(workmatesEatAtRestaurant);
        when(getRestaurantDetailsUseCase.invoke(anyString())).thenReturn(placeDetailsEntities);
        when(getLikedRestaurantsUseCase.invoke()).thenReturn(likedRestaurantsLiveData);
        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(workmatesLiveData);
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        detailsViewModel = new DetailsViewModel(
            mockResources,
            getRestaurantDetailsUseCase,
            savedStateHandle,
            setRestaurantChosenToEatUseCase,
            getLikedRestaurantsUseCase,
            setLikedRestaurantUseCase,
            getWorkmatesEatAtRestaurantUseCase,
            getLoggedUserUseCase,
            scheduleRestaurantNotificationUseCase,
            cancelRestaurantNotificationUseCase
        );
    }

    @Test
    public void onChooseRestaurant_shouldToggleChosenState_andInvokeUseCases() {
        // Given
        String restaurantId = "restaurantId";
        String restaurantName = "Restaurant Name";
        DetailsRestaurantViewState detail = new DetailsRestaurantViewState(
            restaurantId,
            "imageUrl",
            restaurantName,
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );

        // When
        detailsViewModel.onChooseRestaurant(detail);


        // Then
        verify(setRestaurantChosenToEatUseCase).invoke(restaurantId, restaurantName);
        verify(scheduleRestaurantNotificationUseCase).invoke(
            "Restaurant Name",
            "address",
            ""
        );
    }

    @Test
    public void onChooseRestaurant_whenRestaurantNotChosen_shouldInvokeUseCases() {
        // Given
        DetailsRestaurantViewState detail = mock(DetailsRestaurantViewState.class);
        when(detail.getId()).thenReturn(restaurantId);
        detailsViewModel.onChooseRestaurant(detail);

        // When
        detailsViewModel.onChooseRestaurant(detail);

        // Then
        verify(setRestaurantChosenToEatUseCase).invoke(null, null);
        verify(cancelRestaurantNotificationUseCase).invoke();
    }

    @Test
    public void onToggleLikeRestaurant_shouldTriggerUseCase_whenRestaurantIsLiked() {
        // Given
        detailsViewModel.isRestaurantLiked.setValue(false);
        detailsViewModel.detailsRestaurantViewState = new DetailsRestaurantViewState(
            "1",
            "imageUrl",
            "name",
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );

        // When
        detailsViewModel.onToggleLikeRestaurant();

        // Then
        verify(setLikedRestaurantUseCase, times(1)).invoke(eq(true), anyString());
    }


    @Test
    public void onToggleLikeRestaurant_shouldNotTriggerUseCase_whenRestaurantIsNotLiked() {
        // Given
        detailsViewModel.isRestaurantLiked.setValue(true);
        detailsViewModel.detailsRestaurantViewState = new DetailsRestaurantViewState(
            "1",
            "imageUrl",
            "name",
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );

        // When
        detailsViewModel.onToggleLikeRestaurant();

        // Then
        verify(setLikedRestaurantUseCase, times(1)).invoke(eq(false), anyString());
    }

    @Test
    public void onChooseRestaurant_shouldSetRestaurantChosenAndScheduleNotification_whenRestaurantNotPreviouslyChosen() {
        // Given
        detailsViewModel.isRestaurantChosenLiveData.setValue(false);
        DetailsRestaurantViewState detail = new DetailsRestaurantViewState(
            "1",
            "imageUrl",
            "name",
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );

        // When
        detailsViewModel.onChooseRestaurant(detail);

        // Then
        verify(setRestaurantChosenToEatUseCase, times(1)).invoke("1", "name");
        assertEquals(detail, detailsViewModel.detailsRestaurantViewState);
    }

    @Test
    public void onChooseRestaurant_shouldResetRestaurantChosenAndCancelNotification_whenRestaurantPreviouslyChosen() {
        // Given
        detailsViewModel.isRestaurantChosenLiveData.setValue(true);
        DetailsRestaurantViewState detail = new DetailsRestaurantViewState(
            "1",
            "imageUrl",
            "name",
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );

        // When
        detailsViewModel.onChooseRestaurant(detail);

        // Then
        verify(setRestaurantChosenToEatUseCase, times(1)).invoke(null, null);
        verify(cancelRestaurantNotificationUseCase, times(1)).invoke();
        assertNull(detailsViewModel.detailsRestaurantViewState);
    }

    @Test
    public void getWorkmateListLiveData_shouldReturnCorrectLiveData() {
        // Given
        LiveData<List<DetailsWorkmateViewState>> expectedLiveData = detailsViewModel.workmatesViewStateLiveData;

        // When
        LiveData<List<DetailsWorkmateViewState>> resultLiveData = detailsViewModel.getWorkmateListLiveData();

        // Then
        assertEquals(expectedLiveData, resultLiveData);
    }

    @Test
    public void getDetailViewStateLiveData_shouldReturnCorrectLiveData() {
        // Given
        LiveData<DetailsRestaurantViewState> expectedLiveData = detailsViewModel.restaurantViewStateLiveData;

        // When
        LiveData<DetailsRestaurantViewState> resultLiveData = detailsViewModel.getDetailViewStateLiveData();

        // Then
        assertEquals(expectedLiveData, resultLiveData);
    }

    @Test
    public void updateIsRestaurantLiked_shouldUpdateLiveDataValue() {
        // Given
        boolean isLiked = true;

        // When
        detailsViewModel.updateIsRestaurantLiked(isLiked);

        // Then
        assertEquals(isLiked, detailsViewModel.isRestaurantLiked.getValue());
    }

    @Test
    public void getIsRestaurantChosenLiveData_shouldReturnCorrectLiveData() {
        // Given
        LiveData<Boolean> expectedLiveData = detailsViewModel.isRestaurantChosenLiveData;

        // When
        LiveData<Boolean> resultLiveData = detailsViewModel.getIsRestaurantChosenLiveData();

        // Then
        assertEquals(expectedLiveData, resultLiveData);
    }

    @Test
    public void onChooseRestaurant_shouldTriggerUseCase_whenRestaurantIsChosen() {
        // Given
        DetailsRestaurantViewState detail = new DetailsRestaurantViewState(
            "1",
            "imageUrl",
            "name",
            2.5f,
            "address",
            "phoneNumber",
            "website"
        );
        detailsViewModel.isRestaurantChosenLiveData.setValue(false);

        // When
        detailsViewModel.onChooseRestaurant(detail);

        // Then
        verify(setRestaurantChosenToEatUseCase, times(1)).invoke(anyString(), anyString());
        verify(scheduleRestaurantNotificationUseCase, times(1)).invoke(anyString(), anyString(), anyString());
    }

}


