package com.persival.go4lunch;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
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
import com.persival.go4lunch.ui.main.details.DetailsRestaurantViewState;
import com.persival.go4lunch.ui.main.details.DetailsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DetailsViewModelTest {

    private final MutableLiveData<DetailsRestaurantViewState> detailsRestaurantViewStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsEntity> placeDetailsEntityLiveData = new MutableLiveData<>();
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private DetailsViewModel viewModel;
    @Mock
    private Resources resources;
    @Mock
    private SavedStateHandle savedStateHandle;
    @Mock
    private GetRestaurantDetailsUseCase getRestaurantDetailsUseCase;
    @Mock
    private GetLikedRestaurantsUseCase getLikedRestaurantsUseCase;
    @Mock
    private SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase;
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

    @Before
    public void setup() {
        String restaurantId = "1";
        List<String> photos = new ArrayList<>();

        DetailsRestaurantViewState restaurantDetails = new DetailsRestaurantViewState("1", "https://resto.com", "resto", 3.4f, "rue du resto", "1234", "https://resto.com");
        detailsRestaurantViewStateLiveData.setValue(restaurantDetails);

        PlaceDetailsEntity placeDetailsEntity = new PlaceDetailsEntity("1", photos, "resto", 3.4f, "rue du resto", "1234", "https://resto.com");
        placeDetailsEntityLiveData.setValue(placeDetailsEntity);

        LoggedUserEntity loggedUserEntity = new LoggedUserEntity("1", "Emilie", "https://image.com", "001");

        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);
        when(getLikedRestaurantsUseCase.invoke()).thenReturn(new MutableLiveData<>(new ArrayList<>()));
        when(savedStateHandle.get(anyString())).thenReturn(restaurantId);
        when(getRestaurantDetailsUseCase.invoke(anyString())).thenReturn(placeDetailsEntityLiveData);


        viewModel = new DetailsViewModel(
            resources,
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


}
