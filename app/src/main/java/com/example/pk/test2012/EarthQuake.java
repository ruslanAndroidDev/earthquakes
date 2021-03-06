package com.example.pk.test2012;

/**
 * Created by pk on 21.12.2016.
 */
public class EarthQuake {
    private String location;
    private double magnitude;
    private long time;
    private double longitude;
    private double latitude;


    public EarthQuake(String location, double magnitude, long time,double longitude,double latitude) {
        this.location = location;
        this.magnitude = magnitude;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return longitude;
    }

    public double getLongitude() {
        return latitude;
    }
}
