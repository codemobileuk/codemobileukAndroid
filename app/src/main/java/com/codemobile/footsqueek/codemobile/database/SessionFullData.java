package com.codemobile.footsqueek.codemobile.database;

/**
 * Created by greg on 07/02/2017.
 */

public class SessionFullData {

    public Session session;
    public int rowType;
    public String time;
    public String name;
    public int rowColour;

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    public SessionFullData(Session session, int rowType, String time, String name, int rowColour) {
        this.session = session;
        this.rowType = rowType;
        this.time = time;
        this.name = name;
        this.rowColour = rowColour;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowColour() {
        return rowColour;
    }

    public void setRowColour(int rowColour) {
        this.rowColour = rowColour;
    }
}
