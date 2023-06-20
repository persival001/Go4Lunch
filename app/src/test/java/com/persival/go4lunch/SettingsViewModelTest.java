package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.SetNewUserNameUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.DeleteAccountUseCase;
import com.persival.go4lunch.ui.main.settings.SettingsViewModel;
import com.persival.go4lunch.ui.main.settings.SettingsViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private GetLoggedUserUseCase getLoggedUserUseCase;
    @Mock
    private SetNewUserNameUseCase setNewUserNameUseCase;
    @Mock
    private DeleteAccountUseCase deleteAccountUseCase;
    private SettingsViewModel viewModel;
    private LoggedUserEntity loggedUserEntity;

    @Before
    public void setUp() {
        loggedUserEntity = new LoggedUserEntity("id", "name", "email", "url");
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        viewModel = new SettingsViewModel(getLoggedUserUseCase, setNewUserNameUseCase, deleteAccountUseCase);
    }

    @Test
    public void testGetLoggedUserLiveData() {
        SettingsViewState expectedViewState = new SettingsViewState(
            loggedUserEntity.getId(),
            loggedUserEntity.getName(),
            loggedUserEntity.getEmailAddress(),
            loggedUserEntity.getPictureUrl()
        );

        LiveData<SettingsViewState> liveData = viewModel.getLoggedUserLiveData();
        assertNotNull(liveData);
        assertEquals(expectedViewState, liveData.getValue());
    }

    @Test
    public void testGetLoggedUserLiveDataFailure() {
        // Arrange
        when(getLoggedUserUseCase.invoke()).thenReturn(null);

        // Act
        SettingsViewModel viewModel = new SettingsViewModel(getLoggedUserUseCase, setNewUserNameUseCase, deleteAccountUseCase);
        LiveData<SettingsViewState> liveData = viewModel.getLoggedUserLiveData();

        // Assert
        assertNull(liveData.getValue());
    }


    @Test
    public void testSetNewUserName() {
        String newUserName = "newUserName";
        viewModel.setNewUserName(newUserName);
        verify(setNewUserNameUseCase).invoke(newUserName);
    }

    @Test(expected = Exception.class)
    public void testSetNewUserNameFailure() {
        // Arrange
        String newUserName = "newUserName";
        doThrow(new Exception("Mock Exception")).when(setNewUserNameUseCase).invoke(newUserName);

        // Act
        viewModel.setNewUserName(newUserName);
    }

    @Test
    public void testDeleteAccount() {
        viewModel.deleteAccount();
        verify(deleteAccountUseCase).invoke();
    }

    @Test(expected = Exception.class)
    public void testDeleteAccountFailure() {
        // Arrange
        doThrow(new Exception("Mock Exception")).when(deleteAccountUseCase).invoke();

        // Act
        viewModel.deleteAccount();
    }

}
