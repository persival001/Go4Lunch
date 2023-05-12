package com.persival.go4lunch.data.model;

public class User {
    private String uid;
    private String name;
    private String avatarPictureUrl;

    public User() {
    }

    public User(String uid, String name, String avatarPictureUrl) {
        this.uid = uid;
        this.name = name;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    public void setAvatarPictureUrl(String avatarPictureUrl) {
        this.avatarPictureUrl = avatarPictureUrl;
    }


}

