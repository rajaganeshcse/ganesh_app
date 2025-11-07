package com.example.ganesh1.model;

public class Message {
    private String message;
    private String senderId;
    private long timestamp;

    public Message() {}

    public Message(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessage() { return message; }
    public String getSenderId() { return senderId; }
    public long getTimestamp() { return timestamp; }
}
