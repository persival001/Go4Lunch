package com.persival.go4lunch.ui.main.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.FragmentSettingsBinding;
import com.persival.go4lunch.ui.authentication.AuthenticationActivity;
import com.persival.go4lunch.ui.main.maps.MapsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SettingsViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupDoneButtonOnEditText(binding.usernameEditText);

        viewModel.getLoggedUserLiveData().observe(getViewLifecycleOwner(), user -> {
            binding.usernameEditText.setText(user.getAvatarName());
            binding.emailTextView.setText(user.getEmail());
            Glide.with(binding.profileImageButton)
                .load(user.getAvatarPictureUrl())
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.ic_anon_user_48dp)
                .circleCrop()
                .into(binding.profileImageButton);
        });

        binding.deleteButton.setOnClickListener(v -> {
            viewModel.deleteAccount();
            Toast.makeText(getContext()
                , getString(R.string.deleted_account)
                , Toast.LENGTH_SHORT).show();
            startActivity(AuthenticationActivity.navigate(requireContext()));
        });

        binding.updateButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString();

            if (username.isEmpty()) {
                binding.usernameEditText.setError(getString(R.string.username_error));
                binding.usernameEditText.requestFocus();
            } else {
                viewModel.setNewUserName(username);
                Toast.makeText(getContext()
                    , getString(R.string.username_updated)
                    , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.GONE);
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(
            getViewLifecycleOwner(),
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, MapsFragment.newInstance())
                        .commit();
                }
            }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.VISIBLE);
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Listener for the edit text name (when the user click on done button on the keyboard)
    public void setupDoneButtonOnEditText(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Clear focus
                editText.clearFocus();

                // Close keyboard
                InputMethodManager imm = (InputMethodManager) v.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }
            return false;
        });
    }
}
