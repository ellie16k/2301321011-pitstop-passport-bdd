package com.pitstop.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private int totalStamps;
    private DigitalPassport passport;

    public User(int id, String username, String email, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.totalStamps = 0;
        this.passport = new DigitalPassport(id, this);
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public int getTotalStamps() { return totalStamps; }
    public DigitalPassport getPassport() { return passport; }

    public void setTotalStamps(int totalStamps) { this.totalStamps = totalStamps; }
    public void setPassport(DigitalPassport passport) { this.passport = passport; }
}
