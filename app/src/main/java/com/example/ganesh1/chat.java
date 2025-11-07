package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView Username;
    RecyclerView recyclerView;

    String senderId, receiverId, receiverName, chatRoomId;

    ArrayList<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Views
        messageInput = findViewById(R.id.chat_message_input);
        sendBtn = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);
        Username = findViewById(R.id.other_username);

        // ✅ Check Firebase Auth
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Get sender (current user) and receiver info
        senderId = FirebaseAuth.getInstance().getUid();
        receiverId = getIntent().getStringExtra("uid"); // same key as in adapter intent
        receiverName = getIntent().getStringExtra("username");

        if (receiverId == null) {
            Toast.makeText(this, "Receiver ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Username.setText(receiverName != null ? receiverName : "Chat");

        // ✅ Unique chat room for both users
        chatRoomId = senderId.compareTo(receiverId) < 0
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

        // ✅ Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(chatAdapter);

        // ✅ Listen and send messages
        listenMessages();
        sendBtn.setOnClickListener(v -> sendMessage());
    }

    // ✅ Send Message to Firestore
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

        chatRef.add(map)
                .addOnSuccessListener(aVoid -> messageInput.setText(""))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Message send failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // ✅ Listen for real-time messages
    private void listenMessages() {
        db.collection("chats")
                .document(chatRoomId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        return;
                    }

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
