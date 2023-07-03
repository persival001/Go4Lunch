package com.persival.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.details.GetLikedRestaurantsUseCase;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GooglePlacesRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    @Mock
    GetLoggedUserUseCase getLoggedUserUseCase;

    private GetLikedRestaurantsUseCase getLikedRestaurantsUseCase;

    private LoggedUserEntity loggedUserEntity;
    private MutableLiveData<List<String>> liveData;

    @Before
    public void setUp() {
        getLikedRestaurantsUseCase = new GetLikedRestaurantsUseCase(userRepository, getLoggedUserUseCase);

        loggedUserEntity = new LoggedUserEntity(
            "1",
            "Emilie",
            "https://image.com",
            "001"
        );

        liveData = new MutableLiveData<>();

        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);
    }

    @Test
    public void getLikedRestaurantsUseCase_withSuccess() {
        // Given
        List<String> likedRestaurants = Arrays.asList("Restaurant1", "Restaurant2");
        liveData.setValue(likedRestaurants);

        // When
        when(userRepository.getLikedRestaurantsForUser(loggedUserEntity.getId())).thenReturn(liveData);

        // Then
        assertEquals(getLikedRestaurantsUseCase.invoke().getValue(), likedRestaurants);
    }

    @Test
    public void getLikedRestaurantsUseCase_withEmptyString() {
        // Given
        List<String> likedRestaurants = Collections.singletonList("");
        liveData.setValue(likedRestaurants);

        // When
        when(userRepository.getLikedRestaurantsForUser(loggedUserEntity.getId())).thenReturn(liveData);

        // Then
        assertEquals(getLikedRestaurantsUseCase.invoke().getValue(), likedRestaurants);
    }
}
