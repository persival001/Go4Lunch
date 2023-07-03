package com.persival.go4lunch.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.user.SetNewUserNameUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SetNewUserNameUseCaseTest {
    @Mock
    UserRepository mockUserRepository;

    @Mock
    LoggedUserRepository mockLoggedUserRepository;

    @Mock
    GetLoggedUserUseCase mockGetLoggedUserUseCase;

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    FirebaseUser mockFirebaseUser;

    private SetNewUserNameUseCase useCase;

    @Before
    public void setUp() {
        useCase = new SetNewUserNameUseCase(mockUserRepository, mockLoggedUserRepository, mockGetLoggedUserUseCase, mockFirebaseAuth);
    }

    @Test
    public void testInvoke_withCurrentUser() {
        // Given
        String testUid = "TestUid";
        String testUserName = "TestUserName";
        Mockito.when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        Mockito.when(mockFirebaseUser.getUid()).thenReturn(testUid);

        // When
        useCase.invoke(testUserName);

        // Then
        Mockito.verify(mockUserRepository).setNewUserName(testUid, testUserName);
        Mockito.verify(mockLoggedUserRepository).setNewUserName(testUserName);
    }

    @Test
    public void testInvoke_withoutCurrentUser() {
        // Given
        Mockito.when(mockFirebaseAuth.getCurrentUser()).thenReturn(null);

        // When
        useCase.invoke("TestUserName");

        // Then
        Mockito.verify(mockUserRepository, Mockito.never()).setNewUserName(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(mockLoggedUserRepository, Mockito.never()).setNewUserName(Mockito.anyString());
    }
}

