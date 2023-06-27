package com.persival.go4lunch.ui.main.restaurants;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.persival.go4lunch.R;

import java.util.Objects;

public class RestaurantsAdapter extends ListAdapter<RestaurantsViewState, RestaurantsAdapter.ViewHolder> {

    private final OnRestaurantClickedListener listener;

    protected RestaurantsAdapter(OnRestaurantClickedListener listener) {
        super(new ListRestaurantsItemCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView restaurantName;
        private final TextView restaurantAddress;
        private final TextView restaurantOpeningTime;
        private final TextView restaurantDistance;
        private final TextView restaurantParticipants;
        private final RatingBar restaurantStars;
        private final ImageView restaurantPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            restaurantOpeningTime = itemView.findViewById(R.id.restaurant_availability);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            restaurantParticipants = itemView.findViewById(R.id.workmate_icon);
            restaurantStars = itemView.findViewById(R.id.rating_bar);
            restaurantPicture = itemView.findViewById(R.id.restaurant_picture);
        }

        public void bind(RestaurantsViewState item, OnRestaurantClickedListener listener) {

            itemView.setOnClickListener(v -> listener.onRestaurantClicked(item.getId()));
            Glide.with(itemView)
                .load(item.getPictureUrl())
                .placeholder(R.drawable.logoresto)
                .error(R.drawable.no_restaurant_picture)
                .into(restaurantPicture);

            restaurantName.setText(item.getName());
            restaurantAddress.setText(item.getTypeOfCuisineAndAddress());
            if (item.getOpeningTime()) {
                restaurantOpeningTime.setText(R.string.open);
                restaurantOpeningTime.setTextColor(itemView.getResources()
                    .getColor(com.google.android.libraries.places.R.color.quantum_black_text));
            } else {
                restaurantOpeningTime.setText(R.string.closed);
                restaurantOpeningTime.setTextColor(itemView.getResources()
                    .getColor(com.google.android.libraries.places.R.color.quantum_googred));
                restaurantOpeningTime.setTypeface(null, Typeface.BOLD);
            }
            restaurantDistance.setText(item.getDistance());

            if (item.getParticipants().equals("")) {
                restaurantParticipants.setVisibility(View.GONE);
            } else {
                restaurantParticipants.setVisibility(View.VISIBLE);
                restaurantParticipants.setText(item.getParticipants());
            }

            if (item.getRating() == 0F) {
                restaurantStars.setVisibility(View.GONE);
            } else {
                restaurantStars.setVisibility(View.VISIBLE);
                restaurantStars.setRating(item.getRating());
            }

        }
    }

    private static class ListRestaurantsItemCallback extends DiffUtil.ItemCallback<RestaurantsViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantsViewState oldItem, @NonNull RestaurantsViewState newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantsViewState oldItem, @NonNull RestaurantsViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
