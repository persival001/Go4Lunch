package com.persival.go4lunch.ui.authentication;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.facebook.FacebookSdk;

/**
 * Base Activity class that allow to manage all the common code for the activities
 * @param <T> Should be the type of the viewBinding of your activity see more <a href="https://developer.android.com/topic/libraries/view-binding"> here </a>
 */
abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    public abstract T getViewBinding();
    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
    }

    /**
     * Initialise the binding object and the layout of the activity
     */
    private void initBinding(){
        binding = getViewBinding();
        View view = binding.getRoot();
        setContentView(view);
    }

}