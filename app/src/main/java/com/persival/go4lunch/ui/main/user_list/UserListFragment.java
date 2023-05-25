package com.persival.go4lunch.ui.main.user_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.databinding.FragmentUserListBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserListFragment extends Fragment {

    private FragmentUserListBinding binding;

    public static UserListFragment newInstance() {
        return new UserListFragment();
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentUserListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UserListViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);

        RecyclerView recyclerView = binding.userListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        UserListAdapter adapter = new UserListAdapter();
        recyclerView.setAdapter(adapter);

        //viewModel.populateUserListLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}