package com.example.dogshare.Objects;

public class Dog {

    private String dogId;
    private String groupId;
    private String dogName;
    private int dogAge;
    private String dogBreed;
    private Boolean dogGender; // male = true / female = false
    private Boolean shareDogToOthers;
    private String dogPhotoUrl;

    public Dog() {
    }

    public Dog(String dogId, String groupId, String dogName, int dogAge,
               String dogBreed, Boolean dogGender,
               Boolean shareDogToOthers, String dogPhotoUrl) {

        this.dogId = dogId;
        this.groupId = groupId;
        this.dogName = dogName;
        this.dogAge = dogAge;
        this.dogBreed = dogBreed;
        this.dogGender = dogGender;
        this.shareDogToOthers = shareDogToOthers;
        this.dogPhotoUrl = dogPhotoUrl;
    }

    public String getDogId() { return dogId; }
    public void setDogId(String dogId) { this.dogId = dogId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getDogName() { return dogName; }
    public void setDogName(String dogName) { this.dogName = dogName; }

    public int getDogAge() { return dogAge; }
    public void setDogAge(int dogAge) { this.dogAge = dogAge; }

    public String getDogBreed() { return dogBreed; }
    public void setDogBreed(String dogBreed) { this.dogBreed = dogBreed; }

    public Boolean getDogGender() { return dogGender; }
    public void setDogGender(Boolean dogGender) { this.dogGender = dogGender; }

    public Boolean getShareDogToOthers() { return shareDogToOthers; }
    public void setShareDogToOthers(Boolean shareDogToOthers) { this.shareDogToOthers = shareDogToOthers; }

    public String getDogPhotoUrl() { return dogPhotoUrl; }
    public void setDogPhotoUrl(String dogPhotoUrl) { this.dogPhotoUrl = dogPhotoUrl; }
}
