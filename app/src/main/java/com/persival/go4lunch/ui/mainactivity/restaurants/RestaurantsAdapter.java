package com.persival.go4lunch.ui.mainactivity.restaurants;

import android.util.Log;
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
        private final ImageView restaurantStars;
        private final ImageView restaurantPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            restaurantOpeningTime = itemView.findViewById(R.id.restaurant_availability);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            restaurantParticipants = itemView.findViewById(R.id.workmate_icon);
            restaurantStars = itemView.findViewById(R.id.rate_icon);
            restaurantPicture = itemView.findViewById(R.id.restaurant_picture);
        }

        public void bind(RestaurantsViewState item, OnRestaurantClickedListener listener) {
            Log.d("RestaurantsAdapter", "Binding item: " + item.getName());
            itemView.setOnClickListener(v -> listener.onRestaurantClicked(item.getId()));
            Glide.with(itemView)
                .load(item.getPictureUrl())
                .placeholder(R.drawable.logoresto) // Image par défaut en cas d'échec du chargement
                .error(R.drawable.baseline_error_24) // Image à afficher en cas d'erreur
                .into(restaurantPicture);

            restaurantName.setText(item.getName());
            restaurantAddress.setText(item.getTypeOfCuisineAndAddress());
            restaurantOpeningTime.setText(item.getOpeningTime());
            restaurantDistance.setText(item.getDistance());
            restaurantParticipants.setText(item.getParticipants());
            //restaurantStars.setImageResource(item.getStars());
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
