package com.persival.go4lunch.ui.main.workmates;

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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.persival.go4lunch.R;

import java.util.Objects;

public class WorkmatesAdapter extends ListAdapter<WorkmatesViewState, WorkmatesAdapter.ViewHolder> {

    public WorkmatesAdapter() {
        super(new UserListAdapterDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView avatarName;
        private final ImageView avatarPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarName = itemView.findViewById(R.id.avatar_name);
            avatarPicture = itemView.findViewById(R.id.avatar_picture);
        }

        public void bind(WorkmatesViewState item) {
            Glide.with(itemView)
                .load(item.getAvatarPictureUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.ic_anon_user_48dp)
                .into(avatarPicture);


            avatarName.setText(item.getName());
        }
    }

    private static class UserListAdapterDiffCallback extends DiffUtil.ItemCallback<WorkmatesViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return Objects.equals(oldItem.getuId(), newItem.getuId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
