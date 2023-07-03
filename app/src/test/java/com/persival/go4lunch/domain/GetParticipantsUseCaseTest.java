package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.restaurant.GetParticipantsUseCase;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetParticipantsUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    GetParticipantsUseCase getParticipantsUseCase;

    @Before
    public void setUp() {
        getParticipantsUseCase = new GetParticipantsUseCase(userRepository);
    }

    @Test
    public void getParticipants_withWorkmates() {
        // Given
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
            "restaurant1",
            "001",
            new Date())
        );

        MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesLiveData = new MutableLiveData<>();
        workmatesLiveData.setValue(workmates);

        HashMap<String, Integer> expectedResults = new HashMap<>();
        expectedResults.put("restaurant1", 2);
        expectedResults.put("restaurant2", 1);

        when(userRepository.getWorkmatesEatAtRestaurantLiveData()).thenReturn(workmatesLiveData);

        // When
        LiveData<HashMap<String, Integer>> result = getParticipantsUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), expectedResults);
    }

    @Test
    public void getParticipants_withNoWorkmates() {
        // Given
        MutableLiveData<List<UserEatAtRestaurantEntity>> workmatesLiveData = new MutableLiveData<>();
        workmatesLiveData.setValue(Collections.emptyList());

        HashMap<String, Integer> expectedResults = new HashMap<>();

        when(userRepository.getWorkmatesEatAtRestaurantLiveData()).thenReturn(workmatesLiveData);

        // When
        LiveData<HashMap<String, Integer>> result = getParticipantsUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), expectedResults);
    }
}

