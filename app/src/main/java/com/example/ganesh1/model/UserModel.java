package com.example.ganesh1.model;

public class UserModel {

    private String createdTimestamp;
    private String username;
    private String email;
    private String phone;
    private String userId;
    private String profileImage;
    private String fcmToken;


    public UserModel(String username, String email, String phone, String userId, String profileImage) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
        this.createdTimestamp = createdTimestamp;
        this.profileImage = profileImage;
    }

    public UserModel(String username, String email, String username1) {

        this.username = username;
        this.email = email;

    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUserId() { return userId; }
    public String getProfileImage() { return profileImage; }
}
