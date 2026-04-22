package com.example.dogshare.Objects;

public class DogWalker extends User {
    private String city;
    private String address;
    private int age;
    private String phoneNum;
    private String experienceWithDogs;
    private String profilePhotoUrl;

    public DogWalker() {
        super();
    }

    public DogWalker(String userId, String userName, String role, String groupId,
                     String city, String address, int age, String phoneNum,
                     String experienceWithDogs, String profilePhotoUrl) {

        super(userId, userName, role, groupId);

        this.city = city;
        this.address = address;
        this.age = age;
        this.phoneNum = phoneNum;
        this.experienceWithDogs = experienceWithDogs;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getExperienceWithDogs() {
        return experienceWithDogs;
    }

    public void setExperienceWithDogs(String experienceWithDogs) {
        this.experienceWithDogs = experienceWithDogs;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}