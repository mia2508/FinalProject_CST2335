package com.example.vipha.finalproject_cst2335;
/**
 * This is a model class of Trip
 * **/
import android.support.annotation.NonNull;

public class Trip {
    private String destination;
    private String startTime;
    private String latitude;
    private String longitude;
    private String gpsSpeed;
    private String adjustedScheduleTime;

    public Trip() {
        this("", "", "", "", "", "");
    }

    public Trip(String destination, String startTime, String latitude, String longitude,
                String gpsSpeed, String adjustedScheduleTime) {
        this.destination = destination;
        this.startTime = startTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsSpeed = gpsSpeed;
        this.adjustedScheduleTime = adjustedScheduleTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(String gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public String getAdjustedScheduleTime() {
        return adjustedScheduleTime;
    }

    public void setAdjustedScheduleTime(String adjustedScheduleTime) {
        this.adjustedScheduleTime = adjustedScheduleTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Destination: " +
                destination +
                System.lineSeparator() +
                "Start Time: " +
                startTime +
                System.lineSeparator() +
                "latitude: " +
                latitude +
                System.lineSeparator() +
                "Longitude: " +
                longitude +
                System.lineSeparator() +
                "Gps Speed:" +
                gpsSpeed +
                System.lineSeparator() +
                "Aadjusted Schedule Time: " +
                adjustedScheduleTime;
    }
}