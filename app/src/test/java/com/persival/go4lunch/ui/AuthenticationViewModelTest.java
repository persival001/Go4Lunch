package com.persival.go4lunch.ui;

import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.persival.go4lunch.domain.workmate.CreateWorkmateUseCase;
import com.persival.go4lunch.ui.authentication.AuthenticationViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthenticationViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AuthenticationViewModel authenticationViewModel;
    @Mock
    private CreateWorkmateUseCase mockCreateWorkmateUseCase;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authenticationViewModel = new AuthenticationViewModel(mockCreateWorkmateUseCase);
    }

    @Test
    public void createNewWorkmate_CallsUseCase() {
        // When
        authenticationViewModel.createNewWorkmate();

        // Then
        verify(mockCreateWorkmateUseCase).invoke();
    }
}

