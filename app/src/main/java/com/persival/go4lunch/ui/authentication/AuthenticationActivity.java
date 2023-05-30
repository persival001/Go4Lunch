package com.persival.go4lunch.ui.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ui.main.MainActivity;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthenticationActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> signInActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> handleResponseAfterSignIn(result.getResultCode(), result.getData())
    );

    private AuthenticationViewModel viewModel;

    public static Intent navigate(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        } else {
            // Authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

            // Launch the authentication activity
            Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo_auth)
                .setIsSmartLockEnabled(false, true)
                .build();

            signInActivityResultLauncher.launch(signInIntent);
        }
    }

    private void startMainActivity() {
        Intent startMainActivity = new Intent(this, MainActivity.class);
        this.startActivity(startMainActivity);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                //viewModel.setFirestoreUser();
                startMainActivity();
            }
        } else {
            handleSignInError(IdpResponse.fromResultIntent(data));
        }
    }

    private void handleSignInError(@Nullable IdpResponse response) {
        if (response == null) {
            showToastMessage(R.string.error_authentication_canceled);
            return;
        }

        if (response.getError() != null) {
            int errorCode = response.getError().getErrorCode();

            if (errorCode == ErrorCodes.NO_NETWORK) {
                showToastMessage(R.string.error_no_internet);
            } else if (errorCode == ErrorCodes.UNKNOWN_ERROR) {
                showToastMessage(R.string.error_unknown_error);
            }
        }
    }

    private void showToastMessage(@StringRes int messageId) {
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        signInActivityResultLauncher.unregister();
    }
}