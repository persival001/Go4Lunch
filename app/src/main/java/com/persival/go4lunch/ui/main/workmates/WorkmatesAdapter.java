package com.persival.go4lunch.ui.main.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.persival.go4lunch.R;

import java.util.Objects;

public class WorkmatesAdapter extends ListAdapter<WorkmatesViewState, WorkmatesAdapter.ViewHolder> {
    private final OnItemClickListener listener;

    public WorkmatesAdapter(OnItemClickListener listener) {
        super(new UserListAdapterDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView avatarName;
        private final ImageView avatarPicture;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            avatarName = itemView.findViewById(R.id.avatar_name);
            avatarPicture = itemView.findViewById(R.id.avatar_picture);
        }

        public void bind(WorkmatesViewState item, OnItemClickListener listener) {
            Glide.with(itemView)
                .load(item.getWorkmatePictureUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.ic_anon_user_48dp)
                .into(avatarPicture);

            if (item.getText().contains(context.getString(R.string.hasnt_decided_yet_in_string))) {
                avatarName.setTextColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_grey500));
            } else {
                avatarName.setTextColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_black_text));
            }

            avatarName.setText(item.getText());
            if (item.getRestaurantId() != null) {
                itemView.setOnClickListener(v -> listener.onItemClick(item.getRestaurantId()));
            } else {
                itemView.setOnClickListener(null);
            }

        }
    }


    private static class UserListAdapterDiffCallback extends DiffUtil.ItemCallback<WorkmatesViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
