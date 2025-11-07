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
        this.userId=userId;
    }

    public UserModel()
    {}

    public UserModel(String s, String s1, String username) {
    }


    public String getEmail() { return email; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getUserId() {

        return userId;
    }
}

