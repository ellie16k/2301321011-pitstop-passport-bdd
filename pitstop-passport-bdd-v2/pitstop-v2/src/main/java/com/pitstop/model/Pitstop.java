package com.pitstop.model;

public class Pitstop {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String category;
    private String stampDesignURL;

    public Pitstop(int id, String name, double latitude, double longitude, String category) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.stampDesignURL = "stamps/" + id + ".png";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCategory() { return category; }
    public String getStampDesignURL() { return stampDesignURL; }
}
