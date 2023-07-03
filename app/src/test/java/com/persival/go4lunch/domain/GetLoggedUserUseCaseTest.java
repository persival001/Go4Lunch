package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetLoggedUserUseCaseTest {

    @Mock
    LoggedUserRepository loggedUserRepository;

    GetLoggedUserUseCase getLoggedUserUseCase;

    @Before
    public void setUp() {
        getLoggedUserUseCase = new GetLoggedUserUseCase(loggedUserRepository);
    }

    @Test
    public void getLoggedUser_withSuccess() {
        // Given
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity("id", "name", "url", "email");
        when(loggedUserRepository.getLoggedUser()).thenReturn(loggedUserEntity);

        // When
        LoggedUserEntity result = getLoggedUserUseCase.invoke();

        // Then
        assertEquals(result, loggedUserEntity);
    }

    @Test
    public void getLoggedUser_withNull() {
        // Given
        when(loggedUserRepository.getLoggedUser()).thenReturn(null);

        // When
        LoggedUserEntity result = getLoggedUserUseCase.invoke();

        // Then
        assertNull(result);
    }
}
