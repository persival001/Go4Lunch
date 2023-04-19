package com.persival.go4lunch.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.persival.go4lunch.R;
import com.persival.go4lunch.viewmodel.RestaurantsViewModel;

public class RestaurantsFragment extends Fragment {

    private RestaurantsViewModel restaurantsViewModel;

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}