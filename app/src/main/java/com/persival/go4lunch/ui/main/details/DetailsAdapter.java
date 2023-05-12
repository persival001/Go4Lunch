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

public class DetailsAdapter extends ListAdapter<DetailsUserViewState, DetailsAdapter.ViewHolder> {

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

    private static class DetailsAdapterDiffCallback extends DiffUtil.ItemCallback<DetailsUserViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull DetailsUserViewState oldItem, @NonNull DetailsUserViewState newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DetailsUserViewState oldItem, @NonNull DetailsUserViewState newItem) {
            return oldItem.getUid().equals(newItem.getUid());
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

        public void bind(DetailsUserViewState item) {
            Glide.with(itemView)
                .load(item.getAvatarPictureUrl())
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.baseline_error_24)
                .into(avatarPicture);

            avatarName.setText(item.getName());
        }
    }
}
