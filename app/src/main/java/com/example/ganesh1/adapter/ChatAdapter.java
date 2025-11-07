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

    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    ArrayList<Message> messageList;

    public ChatAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        // ✅ Determine if message was sent by current user or received
        Message message = messageList.get(position);
        if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENDER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right,parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_SENDER) {
            ((SenderViewHolder) holder).senderMessage.setText(message.getMessage());
        } else {
            ((ReceiverViewHolder) holder).receiverMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ✅ ViewHolder for sender
    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.left_chat_textview);
        }
    }

    // ✅ ViewHolder for receiver
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
