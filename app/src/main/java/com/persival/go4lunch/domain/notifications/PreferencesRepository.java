package com.persival.go4lunch.domain.notifications;

public interface PreferencesRepository {

    void saveWorkId(String workId);

    String getWorkId();

    void removeWorkId();
}
