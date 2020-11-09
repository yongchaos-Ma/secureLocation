package com.example.myapplication;

import org.litepal.crud.LitePalSupport;

public class SelfLocationDatabase extends LitePalSupport {
    private int id;
    private double latitude;
    private double longitude;
    private Integer time;
    private int WarnType;
    private int HeartRate;

    public double getDirection() {
        return Direction;
    }

    public void setDirection(double direction) {
        Direction = direction;
    }

    public double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(double accuracy) {
        Accuracy = accuracy;
    }

    private double Direction;
    private double Accuracy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public int getWarnType() {
        return WarnType;
    }

    public void setWarnType(int warnType) {
        WarnType = warnType;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        HeartRate = heartRate;
    }
}
