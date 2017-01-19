package com.codemobile.footsqueek.codemobile.database;

import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gregv on 07/01/2017.
 */

public class Session extends RealmObject {

    @PrimaryKey private String id;
    private String title;
    private String desc;
    private String speakerId;
    private Date timeStart;
    private Date timeEnd;
    private String locationName;
    private String locationDesc;
    private boolean doubleRow = false;

    public Session(){

    }

    public Session(String id, String title, String desc, String speakerId, Date timeStart, Date timeEnd, String locationName, String locationDesc, boolean doubleRow) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.speakerId = speakerId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.locationName = locationName;
        this.locationDesc = locationDesc;
        this.doubleRow = doubleRow;
    }

    public boolean isDoubleRow() {
        return doubleRow;
    }

    public void setDoubleRow(boolean doubleRow) {
        this.doubleRow = doubleRow;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void deleteAll(){

    }

    public void deleteRow(int id){

    }

    public void editRow(int id){

    }

    public void editMultipleRows(int[] listOfId){
        for (int id: listOfId) {
            editRow(id);
        }

    }

    public static void addNewRow(Session session){

        RealmUtility.addNewRow(session);

       /* Realm realm = AppDelegate.getRealmInstance();

        try{
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(sesswsion);
            realm.commitTransaction();

        } catch (Exception e) {
            Log.e("RealmError", "error" + e);
            realm.cancelTransaction();

        }*/

    }

    public boolean checkIfRowNeedsUpdate(Session newRow){
        return false;
    }
}
