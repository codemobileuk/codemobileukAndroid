package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by greg on 14/02/2017.
 */

public class Tag extends RealmObject {

   @PrimaryKey private String sessionTagId; //a combination of sessionId and tagID
    private String tagId;
    private String tag;
    private String sessionId;

    public Tag() {

    }

    public Tag(String sessionTagId, String tagId, String tag, String sessionId) {
        this.sessionTagId = sessionTagId;
        this.tagId = tagId;
        this.tag = tag;
        this.sessionId = sessionId;
    }

    public String getSessionTagId() {
        return sessionTagId;
    }

    public void setSessionTagId(String sessionTagId) {
        this.sessionTagId = sessionTagId;
    }

    public String getId() {
        return tagId;
    }

    public void setId(String tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
