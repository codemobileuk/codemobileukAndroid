package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by greg on 28/03/2017.
 */

public class DataBaseVersion extends RealmObject {

    @PrimaryKey private String id;
    private String dbVersion;

    public DataBaseVersion() {

    }

    public DataBaseVersion(String id, String dbVersion) {
        this.id = id;
        this.dbVersion = dbVersion;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }
}
