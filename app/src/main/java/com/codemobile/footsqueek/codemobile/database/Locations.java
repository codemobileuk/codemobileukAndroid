package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by greg on 19/01/2017.
 */

public class Locations extends RealmObject {

    @PrimaryKey private String locationName;
    private double longitude;
    private double latitude;
    private String description;
    private String image_url;
    private String type;


    public Locations(){

    }


    public Locations(String locationName, double longitude, double latitude, String description, String image_url, String type) {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.image_url = image_url;
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
