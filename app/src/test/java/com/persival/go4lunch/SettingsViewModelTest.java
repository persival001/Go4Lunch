package com.persival.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.SetNewUserNameUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.DeleteAccountUseCase;
import com.persival.go4lunch.ui.main.settings.SettingsViewModel;
import com.persival.go4lunch.ui.main.settings.SettingsViewState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SettingsViewModelTest {

    @Mock
    private GetLoggedUserUseCase getLoggedUserUseCase;
    @Mock
    private SetNewUserNameUseCase setNewUserNameUseCase;
    @Mock
    private DeleteAccountUseCase deleteAccountUseCase;

    private SettingsViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        viewModel = new SettingsViewModel(getLoggedUserUseCase, setNewUserNameUseCase, getLoggedUserUseCase1, deleteAccountUseCase);
    }

    @Test
    public void testGetLoggedUserLiveData() {
        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
            "id",
            "name",
            "email",
            "avatarUrl"
        );
        when(getLoggedUserUseCase.invoke()).thenReturn(loggedUserEntity);

        SettingsViewState expectedViewState = new SettingsViewState(
            loggedUserEntity.getId(),
            loggedUserEntity.getName(),
            loggedUserEntity.getEmailAddress(),
            loggedUserEntity.getAvatarPictureUrl()
        );

        viewModel = new SettingsViewModel(getLoggedUserUseCase, setNewUserNameUseCase, getLoggedUserUseCase1, deleteAccountUseCase);

        assertEquals(expectedViewState, viewModel.getLoggedUserLiveData().getValue());
    }

    @Test
    public void testSetNewUserName() {
        String newUserName = "NewUser";

        viewModel.setNewUserName(newUserName);

        verify(setNewUserNameUseCase).invoke(newUserName);
    }

    @Test
    public void testDeleteAccount() {
        viewModel.deleteAccount();

        verify(deleteAccountUseCase).invoke();
    }
}

