package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsGpsActivatedUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    GpsPermissionRepository gpsPermissionRepository;

    IsGpsActivatedUseCase isGpsActivatedUseCase;

    @Before
    public void setUp() {
        isGpsActivatedUseCase = new IsGpsActivatedUseCase(gpsPermissionRepository);
    }

    @Test
    public void isGpsActivated_true() {
        // Given
        LiveData<Boolean> gpsActivatedLiveData = new MutableLiveData<>(true);
        when(gpsPermissionRepository.isGpsActivated()).thenReturn(gpsActivatedLiveData);

        // When
        LiveData<Boolean> result = isGpsActivatedUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), true);
    }

    @Test
    public void isGpsActivated_false() {
        // Given
        LiveData<Boolean> gpsActivatedLiveData = new MutableLiveData<>(false);
        when(gpsPermissionRepository.isGpsActivated()).thenReturn(gpsActivatedLiveData);

        // When
        LiveData<Boolean> result = isGpsActivatedUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), false);
    }
}
