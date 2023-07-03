package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;
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
public class GetWorkmatesEatAtRestaurantUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase;

    @Before
    public void setUp() {
        getWorkmatesEatAtRestaurantUseCase = new GetWorkmatesEatAtRestaurantUseCase(userRepository);
    }

    @Test
    public void getWorkmatesEatAtRestaurant_withValidData() {
        // Given
        MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();
        List<UserEatAtRestaurantEntity> workmates = new ArrayList<>();
        workmates.add(new UserEatAtRestaurantEntity(
            "1",
            "https://image.com",
            "Emilie",
            "restaurant1",
            "001",
            new Date())
        );
        workmates.add(new UserEatAtRestaurantEntity(
            "2",
            "https://image.com",
            "Emilie",
            "restaurant2",
            "001",
            new Date())
        );
        workmates.add(new UserEatAtRestaurantEntity(
            "3",
            "https://image.com",
            "Emilie",
            null,
            "001",
            new Date())
        );
        workmatesEatAtRestaurantLiveData.setValue(workmates);

        when(userRepository.getWorkmatesEatAtRestaurantLiveData()).thenReturn(workmatesEatAtRestaurantLiveData);

        // When
        LiveData<List<UserEatAtRestaurantEntity>> result = getWorkmatesEatAtRestaurantUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), workmates);
    }

    @Test
    public void getWorkmatesEatAtRestaurant_withNullData() {
        // Given
        MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = new MutableLiveData<>();
        workmatesEatAtRestaurantLiveData.setValue(null);

        when(userRepository.getWorkmatesEatAtRestaurantLiveData()).thenReturn(workmatesEatAtRestaurantLiveData);

        // When
        LiveData<List<UserEatAtRestaurantEntity>> result = getWorkmatesEatAtRestaurantUseCase.invoke();

        // Then
        assertNull(TestUtil.getValueForTesting(result));
    }
}
