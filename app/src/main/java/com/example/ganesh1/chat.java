package com.example.ganesh1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.example.ganesh1.adapter.ChatAdapter;
import com.example.ganesh1.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chat extends AppCompatActivity {

    EditText messageInput;
    ImageButton sendBtn;
    RecyclerView recyclerView;

    String senderId, receiverId, chatRoomId;

    ArrayList<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.chat_message_input);
        sendBtn = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);

        senderId = FirebaseAuth.getInstance().getUid();
        receiverId = getIntent().getStringExtra("receiverId");

        // ✅ Unique chat room between two users
        chatRoomId = senderId.compareTo(receiverId) < 0
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(chatAdapter);

        listenMessages();
        sendBtn.setOnClickListener(v -> sendMessage());
    }

    // ✅ Send Message
    private void sendMessage() {
        String msg = messageInput.getText().toString().trim();
        if (msg.isEmpty()) return;

        CollectionReference chatRef = db.collection("chats")
                .document(chatRoomId)
                .collection("messages");

        Map<String, Object> map = new HashMap<>();
        map.put("message", msg);
        map.put("senderId", senderId);
        map.put("timestamp", System.currentTimeMillis());

        chatRef.add(map);
        messageInput.setText("");
    }

    // ✅ Live Listening to Both Sender + Receiver
    private void listenMessages() {
        db.collection("chats")
                .document(chatRoomId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {

                    if (error != null || value == null) return;

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            Message msg = dc.getDocument().toObject(Message.class);
                            messageList.add(msg);
                            chatAdapter.notifyItemInserted(messageList.size() - 1);

                            recyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }
}
