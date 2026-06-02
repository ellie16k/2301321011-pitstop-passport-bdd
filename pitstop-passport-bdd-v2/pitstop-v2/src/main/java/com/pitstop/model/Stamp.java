package com.pitstop.model;

import java.time.LocalDate;

public class Stamp {
    private int id;
    private int passportId;
    private Pitstop pitstop;
    private LocalDate dateAcquired;
    private String userPhotoURL;

    public Stamp(int id, int passportId, Pitstop pitstop, String userPhotoURL) {
        this.id = id;
        this.passportId = passportId;
        this.pitstop = pitstop;
        this.dateAcquired = LocalDate.now();
        this.userPhotoURL = userPhotoURL;
    }

    public int getId() { return id; }
    public int getPassportId() { return passportId; }
    public Pitstop getPitstop() { return pitstop; }
    public LocalDate getDateAcquired() { return dateAcquired; }
    public String getUserPhotoURL() { return userPhotoURL; }
}
