package com.persival.go4lunch.domain.user;

public interface LoggedUserRepository {

    void setNewUserName(String newUserName);

    void deleteAccount();
}