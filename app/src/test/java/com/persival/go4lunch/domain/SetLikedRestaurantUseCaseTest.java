package com.persival.go4lunch.domain;

import com.persival.go4lunch.domain.details.SetLikedRestaurantUseCase;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SetLikedRestaurantUseCaseTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private GetLoggedUserUseCase mockGetLoggedUserUseCase;

    private SetLikedRestaurantUseCase useCase;

    @Before
    public void setup() {
        useCase = new SetLikedRestaurantUseCase(mockUserRepository, mockGetLoggedUserUseCase);
    }

    @Test
    public void testInvoke_success() {
        // Given
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
            "1",
            "Emilie",
            "https://image.com",
            "001"
        );

        boolean testIsAdded = true;
        String testRestaurantId = "TestRestaurantId";
        Mockito.when(mockGetLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        // When
        useCase.invoke(testIsAdded, testRestaurantId);

        // Then
        Mockito.verify(mockGetLoggedUserUseCase).invoke();
        Mockito.verify(mockUserRepository).updateLikedRestaurants(loggedUserEntity, testIsAdded, testRestaurantId);
    }

    @Test
    public void testInvoke_withNullUser() {
        // Given
        Mockito.when(mockGetLoggedUserUseCase.invoke()).thenReturn(null);

        // When
        useCase.invoke(true, "TestRestaurantId");

        // Then
        Mockito.verify(mockGetLoggedUserUseCase).invoke();
        Mockito.verify(mockUserRepository, Mockito.never()).updateLikedRestaurants(null, true, "TestRestaurantId");
    }

}

