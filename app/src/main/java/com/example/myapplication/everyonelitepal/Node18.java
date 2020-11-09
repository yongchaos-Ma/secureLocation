package com.example.myapplication.everyonelitepal;

import org.litepal.crud.LitePalSupport;

public class Node18 extends NodeMother {
    private int id;
    private double latitude;
    private double longitude;
    private Integer time;
    private int WarnType;
    private int HeartRate;
    private boolean direct;

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

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

