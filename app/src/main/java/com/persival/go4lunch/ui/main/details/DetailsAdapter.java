package com.persival.go4lunch.ui.main.details;

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
import com.persival.go4lunch.R;

public class DetailsAdapter extends ListAdapter<DetailsWorkmateViewState, DetailsAdapter.ViewHolder> {

    public DetailsAdapter() {
        super(new DetailsAdapterDiffCallback());
    }

    @NonNull
    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsAdapter.ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static class DetailsAdapterDiffCallback extends DiffUtil.ItemCallback<DetailsWorkmateViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull DetailsWorkmateViewState oldItem, @NonNull DetailsWorkmateViewState newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DetailsWorkmateViewState oldItem, @NonNull DetailsWorkmateViewState newItem) {
            return oldItem.equals(newItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatarPicture;
        private final TextView avatarName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarName = itemView.findViewById(R.id.avatar_name);
            avatarPicture = itemView.findViewById(R.id.avatar_picture);
        }

        public void bind(DetailsWorkmateViewState item) {
            Glide.with(itemView)
                .load(item.getWorkmatePictureUrl())
                .circleCrop()
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.ic_anon_user_48dp)
                .into(avatarPicture);

            avatarName.setText(item.getWorkmateName());
        }
    }
}
