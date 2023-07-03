package com.persival.go4lunch.domain;

import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.domain.location.StartLocationRequestUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartLocationRequestUseCaseTest {
    @Mock
    LocationDataRepository mockLocationDataRepository;

    private StartLocationRequestUseCase useCase;

    @Before
    public void setUp() {
        useCase = new StartLocationRequestUseCase(mockLocationDataRepository);
    }

    @Test
    public void testInvoke() {
        // When
        useCase.invoke();

        // Then
        Mockito.verify(mockLocationDataRepository).startLocationRequest();
    }
}
