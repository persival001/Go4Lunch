package com.persival.go4lunch.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.user.GetUserNameChangedUseCase;
import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetUserNameChangedUseCaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    LoggedUserRepository loggedUserRepository;

    GetUserNameChangedUseCase getUserNameChangedUseCase;

    @Before
    public void setUp() {
        getUserNameChangedUseCase = new GetUserNameChangedUseCase(loggedUserRepository);
    }

    @Test
    public void getUserNameChangedLiveData_withValidData() {
        // Given
        MutableLiveData<LoggedUserEntity> userNameChangedLiveData = new MutableLiveData<>();
        LoggedUserEntity userEntity = new LoggedUserEntity("1", "John Doe", "image_url", "001");
        userNameChangedLiveData.setValue(userEntity);

        when(loggedUserRepository.getUserNameChangedLiveData()).thenReturn(userNameChangedLiveData);

        // When
        LiveData<LoggedUserEntity> result = getUserNameChangedUseCase.invoke();

        // Then
        assertEquals(TestUtil.getValueForTesting(result), userEntity);
    }

    @Test
    public void getUserNameChangedLiveData_withNullData() {
        // Given
        MutableLiveData<LoggedUserEntity> userNameChangedLiveData = new MutableLiveData<>();
        userNameChangedLiveData.setValue(null);

        when(loggedUserRepository.getUserNameChangedLiveData()).thenReturn(userNameChangedLiveData);

        // When
        LiveData<LoggedUserEntity> result = getUserNameChangedUseCase.invoke();

        // Then
        assertNull(TestUtil.getValueForTesting(result));
    }
}
