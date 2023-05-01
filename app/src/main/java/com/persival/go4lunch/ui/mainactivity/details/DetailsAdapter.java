package com.persival.go4lunch.ui.mainactivity.details;

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

public class DetailsAdapter extends ListAdapter<DetailsViewState, DetailsAdapter.ViewHolder> {

    public DetailsAdapter() {
        super(new DetailsAdapterDiffCallback());
    }

    @NonNull
    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsAdapter.ViewHolder(
            LayoutInflater.
                from(parent.getContext()).
                inflate(
                    R.layout.item_details,
                    parent,
                    false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static class DetailsAdapterDiffCallback extends DiffUtil.ItemCallback<DetailsViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull DetailsViewState oldItem, @NonNull DetailsViewState newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DetailsViewState oldItem, @NonNull DetailsViewState newItem) {
            return oldItem.getParticipants().equals(newItem.getParticipants());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView restaurantPicture;
        private final TextView restaurantName;
        private final RatingBar restaurantStars;
        private final TextView restaurantAddress;
        private final TextView restaurantPhoneNumber;
        private final TextView restaurantWebSite;
        private final ImageView avatarPicture;
        private final TextView avatarName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantPicture = itemView.findViewById(R.id.details_picture);
            restaurantName = itemView.findViewById(R.id.details_name);
            restaurantStars = itemView.findViewById(R.id.details_rating_bar);
            restaurantAddress = itemView.findViewById(R.id.details_address);
            restaurantPhoneNumber = itemView.findViewById(R.id.call_button);
            restaurantWebSite = itemView.findViewById(R.id.website_button);
            avatarName = itemView.findViewById(R.id.avatar_name);
            avatarPicture = itemView.findViewById(R.id.avatar_picture);
        }

        public void bind(DetailsViewState item) {
            Glide.with(itemView)
                .load(item.getAvatarUrl())
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.baseline_error_24)
                .into(avatarPicture);

            Glide.with(itemView)
                .load(item.getPictureUrl())
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.baseline_error_24)
                .into(restaurantPicture);

            restaurantName.setText(item.getName());
            restaurantAddress.setText(item.getTypeOfCuisineAndAddress());
            restaurantStars.setRating(item.getRating());
        }
    }
}
