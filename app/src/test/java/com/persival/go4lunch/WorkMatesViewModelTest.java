package com.persival.go4lunch;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;
import com.persival.go4lunch.ui.main.workmates.WorkmatesViewModel;
import com.persival.go4lunch.ui.main.workmates.WorkmatesViewState;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
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

    private MutableLiveData<List<UserEatAtRestaurantEntity>> liveData;


    @Before
    public void setUp() {
        loggedUserEntity = mock(LoggedUserEntity.class);
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        liveData = new MutableLiveData<>();
        when(getWorkmatesEatAtRestaurantUseCase.invoke()).thenReturn(liveData);

        viewModel = new WorkmatesViewModel(getWorkmatesEatAtRestaurantUseCase, resources, getLoggedUserUseCase);
    }

    @Test
    public void testGetWorkmatesSuccess() {
        UserEatAtRestaurantEntity entity1 = new UserEatAtRestaurantEntity("2", "https://image.com", "Emilie", "Restaurant 1", "001", new Date());
        UserEatAtRestaurantEntity entity2 = new UserEatAtRestaurantEntity("3", "https://image.com", "Gino", "Restaurant 2", "002", new Date());
        List<UserEatAtRestaurantEntity> listOfWorkmates = new ArrayList<>();
        listOfWorkmates.add(entity1);
        listOfWorkmates.add(entity2);
        liveData.setValue(listOfWorkmates);

        // When
        List<WorkmatesViewState> result = TestUtil.getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(2, result.size());

        verify(getWorkmatesEatAtRestaurantUseCase).invoke();
        verify(getLoggedUserUseCase).invoke();

        verifyNoMoreInteractions(getWorkmatesEatAtRestaurantUseCase);
        verifyNoMoreInteractions(getLoggedUserUseCase);
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