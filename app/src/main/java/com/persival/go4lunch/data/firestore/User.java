package com.persival.go4lunch.data.firestore;

import java.util.Objects;

public class User {
    private String uId;
    private String name;
    private String emailAddress;
    private String avatarPictureUrl;

    // Empty constructor needed for Firestore deserialization
    public User() {}

    public User(String uId, String name, String emailAddress, String avatarPictureUrl) {
        this.uId = uId;
        this.name = name;
        this.emailAddress = emailAddress;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId, name, emailAddress, avatarPictureUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(uId, user.uId) && Objects.equals(name, user.name) && Objects.equals(emailAddress, user.emailAddress) && Objects.equals(avatarPictureUrl, user.avatarPictureUrl);
    }

    @Override
    public String toString() {
        return "User{" +
            "uId='" + uId + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            '}';
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    public void setAvatarPictureUrl(String avatarPictureUrl) {
        this.avatarPictureUrl = avatarPictureUrl;
    }

}


