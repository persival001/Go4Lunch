package com.persival.go4lunch.domain;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.workmate.DeleteAccountUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAccountUseCaseTest {
    @Mock
    LoggedUserRepository loggedUserRepository;

    DeleteAccountUseCase deleteAccountUseCase;

    @Before
    public void setUp() {
        deleteAccountUseCase = new DeleteAccountUseCase(loggedUserRepository);
    }

    @Test
    public void deleteAccount_withSuccess() {
        // When
        deleteAccountUseCase.invoke();

        // Then
        verify(loggedUserRepository).deleteAccount();
    }

    @Test(expected = RuntimeException.class)
    public void deleteAccount_withFailure() {
        // Given
        doThrow(new RuntimeException()).when(loggedUserRepository).deleteAccount();

        // When
        deleteAccountUseCase.invoke();

        // Then
        // Should throw a RuntimeException
    }
}