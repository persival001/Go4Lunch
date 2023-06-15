package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
            loggedUserEntity.getAvatarPictureUrl()
        );

        LiveData<SettingsViewState> liveData = viewModel.getLoggedUserLiveData();
        assertNotNull(liveData);
        assertEquals(expectedViewState, liveData.getValue());
    }

    @Test
    public void testSetNewUserName() {
        String newUserName = "newUserName";
        viewModel.setNewUserName(newUserName);
        verify(setNewUserNameUseCase).invoke(newUserName);
    }

    @Test
    public void testDeleteAccount() {
        viewModel.deleteAccount();
        verify(deleteAccountUseCase).invoke();
    }
}
