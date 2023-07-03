package com.persival.go4lunch.domain;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.CreateWorkmateUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.UserEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class CreateWorkmateUseCaseTest {
    @Mock
    UserRepository userRepository;

    @Mock
    GetLoggedUserUseCase getLoggedUserUseCase;

    CreateWorkmateUseCase createWorkmateUseCase;

    @Before
    public void setUp() {
        createWorkmateUseCase = new CreateWorkmateUseCase(userRepository, getLoggedUserUseCase);
    }

    @Test
    public void createWorkmate_withSuccess() {
        // Given
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
            "1",
            "Emilie",
            "https://image.com",
            "001"
        );
        UserEntity newUser = new UserEntity(
            loggedUserEntity.getId(),
            loggedUserEntity.getName(),
            loggedUserEntity.getPictureUrl(),
            new ArrayList<>()
        );

        // When
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        // Invoke
        createWorkmateUseCase.invoke();

        // Then
        verify(userRepository).createUser(newUser);
        verify(userRepository).updateWorkmateInformation(loggedUserEntity, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void createWorkmate_withoutLoggedInUser() {
        // Given
        when(getLoggedUserUseCase.invoke()).thenReturn(null);

        // Invoke
        createWorkmateUseCase.invoke();

        // Then
        // Should throw an IllegalStateException
    }
}
