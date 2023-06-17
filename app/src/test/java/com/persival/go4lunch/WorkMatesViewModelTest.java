package com.persival.go4lunch;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;
import com.persival.go4lunch.ui.main.workmates.WorkmatesViewModel;
import com.persival.go4lunch.ui.main.workmates.WorkmatesViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkMatesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase;

    @Mock
    private Resources resources;

    @Mock
    private GetLoggedUserUseCase getLoggedUserUseCase;

    @Mock
    private Observer<List<WorkmatesViewState>> observer;

    private WorkmatesViewModel viewModel;

    private LoggedUserEntity loggedUserEntity;


    @Before
    public void setUp() {
        loggedUserEntity = new LoggedUserEntity("id", "name", "email", "url");
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        LiveData<List<WorkmateEatAtRestaurantEntity>> liveData = new MutableLiveData<>();
        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(liveData);

        viewModel = new WorkmatesViewModel(getWorkmatesEatAtRestaurantUseCase, resources, getLoggedUserUseCase);
    }

    @Test
    public void testGetWorkmatesSuccess() {
        WorkmateEatAtRestaurantEntity entity1 = new WorkmateEatAtRestaurantEntity("2", "https://image.com", "Emilie", "Restaurant 1", "001", new Date());
        WorkmateEatAtRestaurantEntity entity2 = new WorkmateEatAtRestaurantEntity("3", "https://image.com", "Gino", "Restaurant 2", "002", new Date());

        // Prepare mock data
        List<WorkmateEatAtRestaurantEntity> entities = Arrays.asList(entity1, entity2);
        LiveData<List<WorkmateEatAtRestaurantEntity>> liveData = new MutableLiveData<>(entities);

        // Prepare expected result
        WorkmatesViewState viewState1 = new WorkmatesViewState("2", "https://image.com", "Emilie is eating at Restaurant 1");
        WorkmatesViewState viewState2 = new WorkmatesViewState("3", "https://image.com", "Gino is eating at Restaurant 2");
        List<WorkmatesViewState> expected = Arrays.asList(viewState1, viewState2);

        // Configure mocks
        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(liveData);
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);
        when(resources.getString(R.string.is_eating_at, "Emilie", "Restaurant 1")).thenReturn("Emilie is eating at Restaurant 1");
        when(resources.getString(R.string.is_eating_at, "Gino", "Restaurant 2")).thenReturn("Gino is eating at Restaurant 2");

        // Create ViewModel
        viewModel = new WorkmatesViewModel(getWorkmatesEatAtRestaurantUseCase, resources, getLoggedUserUseCase);

        // Observe the LiveData
        viewModel.getViewStateLiveData().observeForever(observer);

        // Verify the observer received the correct data
        verify(observer).onChanged(expected);
    }


    @Test
    public void testGetWorkmatesFailure() {
        // Arrange
        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(new MutableLiveData<>(null));

        // Act
        WorkmatesViewModel viewModel = new WorkmatesViewModel(getWorkmatesEatAtRestaurantUseCase, resources, getLoggedUserUseCase);
        LiveData<List<WorkmatesViewState>> liveData = viewModel.getViewStateLiveData();

        // Assert
        liveData.observeForever(observer);
        verify(observer).onChanged(null);
        liveData.removeObserver(observer);
    }

}