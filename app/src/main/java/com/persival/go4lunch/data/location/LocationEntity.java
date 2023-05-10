package com.persival.go4lunch.data.location;

import androidx.annotation.NonNull;

import java.util.Objects;

public class LocationEntity {
    private final double latitude;
    private final double longitude;

    public LocationEntity(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity that = (LocationEntity) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "LocationEntity{" +
            "latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
