package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;

/**
 * Created by greg on 19/01/2017.
 */

public class Location extends RealmObject {

    private String locationName;
    private double longitude;
    private double latitude;
    private String description;

    public Location(){

    }


    public Location(String locationName, double longitude, double latitude, String description) {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
