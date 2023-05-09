package com.persival.go4lunch.data.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Users {

    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String firstName;
    @NonNull
    private final String email;
    @NonNull
    private final String photoUrl;

    public Users(
        @NonNull String id,
        @NonNull String name,
        @NonNull String firstName,
        @NonNull String email,
        @NonNull String photoUrl
    ) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstName, email, photoUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Users users = (Users) o;
        return id.equals(users.id) &&
            name.equals(users.name) &&
            firstName.equals(users.firstName) &&
            email.equals(users.email) &&
            photoUrl.equals(users.photoUrl);
    }

    @Override
    public String toString() {
        return "Users{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", firstName='" + firstName + '\'' +
            ", email='" + email + '\'' +
            ", photoUrl='" + photoUrl + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPhotoUrl() {
        return photoUrl;
    }

}
