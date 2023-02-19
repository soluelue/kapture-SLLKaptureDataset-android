package com.sll.estimation.model;

public class LatLng {
    private long timestamp;
    private double latitude;
    private double longitude;
    private double altitude;

    public LatLng(long timestamp, double latitude, double longitude, double altitude){
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return timestamp +
                "," + latitude +
                "," + longitude +
                "," + altitude;
    }
}
