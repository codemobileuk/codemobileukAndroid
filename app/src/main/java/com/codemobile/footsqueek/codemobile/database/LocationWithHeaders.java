package com.codemobile.footsqueek.codemobile.database;



/**
 * Created by greg on 17/02/2017.
 */

public class LocationWithHeaders {

    public Location location;
    public int rowType;
    public String title;

    public LocationWithHeaders(Location location, int rowType, String title) {
        this.location = location;
        this.rowType = rowType;
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
