package com.persival.go4lunch.ui.mainactivity.restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.persival.go4lunch.R;

public class RestaurantsAdapter extends ListAdapter<RestaurantsViewState, RestaurantsAdapter.ViewHolder> {

    protected RestaurantsAdapter(@NonNull DiffUtil.ItemCallback<RestaurantsViewState> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView pictureUrl;
        private final TextView nameTextView;
        private final ImageView deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             = itemView.findViewById(R.id.restaurant_picture);
            nameTextView = itemView.findViewById(R.id.neighbours_item_tv_name);
            deleteImageView = itemView.findViewById(R.id.neighbours_item_iv_delete);
        }

        public void bind(RestaurantsViewState item) {
            Glide.with(avatarImageView)
                .load(item.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImageView);
            nameTextView.setText(item.getName());
        }
    }

    private static class ListRestaurantsItemCallback extends DiffUtil.ItemCallback<RestaurantsViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantsViewState oldItem, @NonNull RestaurantsViewState newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantsViewState oldItem, @NonNull RestaurantsViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
