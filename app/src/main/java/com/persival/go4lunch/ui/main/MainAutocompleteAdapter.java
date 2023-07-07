package com.persival.go4lunch.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.databinding.MainItemAutocompleteBinding;

public class MainAutocompleteAdapter extends ListAdapter<MainViewStateAutocomplete, MainAutocompleteAdapter.MainAutocompleteViewHolder> {

    private final OnAutocompleteClickedListener listener;

    public MainAutocompleteAdapter(OnAutocompleteClickedListener listener) {
        super(new DiffUtil.ItemCallback<MainViewStateAutocomplete>() {
            @Override
            public boolean areItemsTheSame(@NonNull MainViewStateAutocomplete oldItem, @NonNull MainViewStateAutocomplete newItem) {
                return oldItem.getPlaceId().equals(newItem.getPlaceId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MainViewStateAutocomplete oldItem, @NonNull MainViewStateAutocomplete newItem) {
                return oldItem.equals(newItem);
            }
        });

        this.listener = listener;
    }

    @NonNull
    @Override
    public MainAutocompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainAutocompleteViewHolder(
            MainItemAutocompleteBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MainAutocompleteViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class MainAutocompleteViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final MainItemAutocompleteBinding binding;

        public MainAutocompleteViewHolder(@NonNull MainItemAutocompleteBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(MainViewStateAutocomplete item, OnAutocompleteClickedListener listener) {
            binding.mainItemAutocompleteTextview.setText(item.getName());
            binding.getRoot().setOnClickListener(v -> listener.onAutocompleteClicked(item));
        }
    }
}
