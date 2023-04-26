package com.persival.go4lunch.model;

import java.util.Objects;

public class Restaurant {
    private final long id;
    private final String name;
    private final String address;
    private final String photoUrl;
    private final String openingHours;
    private final String distance;
    private final String workmates;

    public Restaurant(
        final long id,
        final String name,
        final String address,
        final String photoUrl,
        final String openingHours,
        final String distance,
        final String workmates
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoUrl = photoUrl;
        this.openingHours = openingHours;
        this.distance = distance;
        this.workmates = workmates;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getDistance() {
        return distance;
    }

    public String getWorkmates() {
        return workmates;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, photoUrl, openingHours, distance, workmates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Restaurant that = (Restaurant) o;
        return id == that.id
            && Objects.equals(name, that.name)
            && Objects.equals(address, that.address)
            && Objects.equals(photoUrl, that.photoUrl)
            && Objects.equals(openingHours, that.openingHours)
            && Objects.equals(distance, that.distance)
            && Objects.equals(workmates, that.workmates);
    }
}
