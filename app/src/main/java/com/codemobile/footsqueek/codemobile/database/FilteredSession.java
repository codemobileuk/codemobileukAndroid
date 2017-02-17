package com.codemobile.footsqueek.codemobile.database;

/**
 * Created by greg on 16/02/2017.
 */

public class FilteredSession {

    private String sessionId;
    private boolean isFiltered;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isFiltered() {
        return isFiltered;
    }

    public void setFiltered(boolean filtered) {
        isFiltered = filtered;
    }
}
