package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class ChildLocation {
    String latitude,longitude,username;

    public ChildLocation(String latitude, String longitude, String username) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
    }

    public ChildLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ChildLocation() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "Latitude: "+latitude
                + "Longitude : "+longitude;
    }
}
