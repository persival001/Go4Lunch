package com.persival.go4lunch.domain;

import static org.mockito.Mockito.verify;

import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;
import com.persival.go4lunch.domain.permissions.RefreshGpsActivationUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RefreshGpsActivationUseCaseTest {
    @Mock
    GpsPermissionRepository gpsPermissionRepository;

    RefreshGpsActivationUseCase refreshGpsActivationUseCase;

    @Before
    public void setUp() {
        refreshGpsActivationUseCase = new RefreshGpsActivationUseCase(gpsPermissionRepository);
    }

    @Test
    public void refreshGpsActivation_invokeRepositoryMethod() {
        // When
        refreshGpsActivationUseCase.invoke();

        // Then
        verify(gpsPermissionRepository).refreshGpsActivation();
    }
}
