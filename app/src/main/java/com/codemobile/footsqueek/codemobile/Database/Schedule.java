package com.codemobile.footsqueek.codemobile.database;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by gregv on 07/01/2017.
 */

public class Schedule extends RealmObject {

    private Date timeStart;
    private Date timeEnd;
    private String speaker;
    private String title;
    private String desc;
    private boolean doubleRow = false;
    private int id;

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

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void addNewRow(){

    }

    public boolean checkIfRowNeedsUpdate(Schedule newRow){
        return false;
    }
}
