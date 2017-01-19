package com.codemobile.footsqueek.codemobile.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by greg on 19/01/2017.
 */

public class Speaker extends RealmObject {

    @PrimaryKey private String id;
    private String firstname;
    private String surname;
    private String twitter;
    private String organisation;
    private String profile;
    private String photoUrl;
    private String fullname;

    public Speaker(){

    }

    public Speaker(String id, String firstname, String surname, String twitter, String organisation, String profile, String photoUrl, String fullname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.twitter = twitter;
        this.organisation = organisation;
        this.profile = profile;
        this.photoUrl = photoUrl;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
