package com.example.dogshare.Objects;

public class User {

    private String userId;
    private String userName;
    private String role;    // child / parent / dogwalker
    private String groupId;

    public User() {
    }

    public User(String userId, String userName, String role, String groupId) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
