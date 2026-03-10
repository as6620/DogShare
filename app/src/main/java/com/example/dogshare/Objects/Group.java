package com.example.dogshare.Objects;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String groupCode;
    private List<String> userIds;
    private List<String> dogIds;

    public Group() {
        userIds = new ArrayList<>();
        dogIds = new ArrayList<>();
    }

    public Group(String groupId, String groupCode) {
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.userIds = new ArrayList<>();
        this.dogIds = new ArrayList<>();
    }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }

    public List<String> getUserIds() { return userIds; }
    public void setUserIds(List<String> userIds) { this.userIds = userIds; }

    public List<String> getDogIds() { return dogIds; }
    public void setDogIds(List<String> dogIds) { this.dogIds = dogIds; }
}
