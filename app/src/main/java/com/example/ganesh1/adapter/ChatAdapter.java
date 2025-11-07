package com.example.ganesh1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ganesh1.R;
import com.example.ganesh1.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Message> messageList;
    final int MSG_TYPE_RIGHT = 1;
    final int MSG_TYPE_LEFT = 2;

    public ChatAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);
            return new RightViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
            return new LeftViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = messageList.get(position);
        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).msgText.setText(msg.getMessage());
        } else {
            ((LeftViewHolder) holder).msgText.setText(msg.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class RightViewHolder extends RecyclerView.ViewHolder {
        TextView msgText;

        RightViewHolder(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.right_msg_text);
        }
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView msgText;

        LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.left_msg_text);
        }
    }
}
