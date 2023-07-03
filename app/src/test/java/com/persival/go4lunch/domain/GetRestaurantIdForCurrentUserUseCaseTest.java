package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.restaurant.GetRestaurantIdForCurrentUserUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetRestaurantIdForCurrentUserUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase;

    @Before
    public void setUp() {
        getRestaurantIdForCurrentUserUseCase = new GetRestaurantIdForCurrentUserUseCase(userRepository);
    }

    @Test
    public void getRestaurantId_withValidId() {
        // Given
        MutableLiveData<String> restaurantIdLiveData = new MutableLiveData<>();
        restaurantIdLiveData.setValue("restaurant1");

        when(userRepository.getRestaurantIdForCurrentUser()).thenReturn(restaurantIdLiveData);

        // When
        LiveData<String> result = getRestaurantIdForCurrentUserUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), "restaurant1");
    }

    @Test
    public void getRestaurantId_withNullId() {
        // Given
        MutableLiveData<String> restaurantIdLiveData = new MutableLiveData<>();
        restaurantIdLiveData.setValue(null);

        when(userRepository.getRestaurantIdForCurrentUser()).thenReturn(restaurantIdLiveData);

        // When
        LiveData<String> result = getRestaurantIdForCurrentUserUseCase.invoke();

        // Then
        assertNull(TestUtil.getValueForTesting(result));
    }
}
