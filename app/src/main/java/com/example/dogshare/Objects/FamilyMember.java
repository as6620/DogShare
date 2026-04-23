package com.example.dogshare.Objects;

public class FamilyMember extends User {
    private boolean isParent;

    public FamilyMember() {
        super();
    }

    public FamilyMember(String uid, String userName, String userType, String groupId, boolean isParent) {
        super(uid, userName, userType, groupId);
        this.isParent = isParent;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }
}