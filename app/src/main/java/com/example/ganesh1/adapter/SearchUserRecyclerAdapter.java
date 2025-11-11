package com.example.ganesh1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ganesh1.R;
import com.example.ganesh1.search_user;
import com.example.ganesh1.model.UserModel;
import com.example.ganesh1.search_user;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserModel model) {

        // ✅ Set text fields
        holder.username.setText(model.getUsername());
        holder.phone.setText(model.getPhone());

        // ✅ Load profile image using Glide
        if (model.getProfileImage() != null && !model.getProfileImage().isEmpty()) {
            Glide.with(context)
                    .load(model.getProfileImage())
                    .placeholder(R.drawable.person_icon)
                    .into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.person_icon);
        }

        // ✅ Handle click on user item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, search_user.class);
            intent.putExtra("uid", model.getUserId());
            intent.putExtra("username", model.getUsername());
            intent.putExtra("phone", model.getPhone());
            intent.putExtra("profileImage", model.getProfileImage());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserViewHolder(v);
    }

    // ✅ ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView username, phone;
        ImageView profileImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_text);
            phone = itemView.findViewById(R.id.phone_text);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}
