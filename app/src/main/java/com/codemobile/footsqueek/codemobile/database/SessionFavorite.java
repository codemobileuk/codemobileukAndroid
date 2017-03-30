package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by greg on 30/03/2017.
 */

public class SessionFavorite extends RealmObject {

    @PrimaryKey String sessionId;
    String id;
    Boolean isFavorite;

    public SessionFavorite(){

    }

    public SessionFavorite(String sessionId, String id, Boolean isFavorite) {
        this.sessionId = sessionId;
        this.id = id;
        this.isFavorite = isFavorite;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
