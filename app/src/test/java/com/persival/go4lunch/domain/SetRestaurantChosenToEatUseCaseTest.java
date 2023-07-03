package com.persival.go4lunch.domain;

import com.persival.go4lunch.domain.details.SetRestaurantChosenToEatUseCase;
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
public class SetRestaurantChosenToEatUseCaseTest {
    @Mock
    UserRepository mockUserRepository;

    @Mock
    GetLoggedUserUseCase mockGetLoggedUserUseCase;

    private SetRestaurantChosenToEatUseCase useCase;

    @Before
    public void setUp() {
        useCase = new SetRestaurantChosenToEatUseCase(mockUserRepository, mockGetLoggedUserUseCase);
    }

    @Test
    public void testInvoke_withCurrentUser() {
        // Given
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
            "1",
            "Emilie",
            "https://image.com",
            "001"
        );

        String testRestaurantId = "TestRestaurantId";
        String testRestaurantName = "TestRestaurantName";
        Mockito.when(mockGetLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        // When
        useCase.invoke(testRestaurantId, testRestaurantName);

        // Then
        Mockito.verify(mockUserRepository).updateWorkmateInformation(loggedUserEntity, testRestaurantId, testRestaurantName);
    }

    @Test
    public void testInvoke_withoutCurrentUser() {
        // Given
        Mockito.when(mockGetLoggedUserUseCase.invoke()).thenReturn(null);

        // When
        useCase.invoke("TestRestaurantId", "TestRestaurantName");

        // Then
        Mockito.verify(
            mockUserRepository,
            Mockito.never()).updateWorkmateInformation(
            Mockito.any(LoggedUserEntity.class),
            Mockito.anyString(), Mockito.anyString()
        );
    }
}

