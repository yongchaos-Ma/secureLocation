package com.example.myapplication.everyonelitepal;

public class Node16 extends NodeMother {
    private int id;
    private double latitude;
    private double longitude;
    private Integer time;
    private Integer WarnType;
    private Integer HeartRate;
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

    public Integer getWarnType() {
        return WarnType;
    }

    public void setWarnType(int warnType) {
        this.WarnType = warnType;
    }

    public Integer getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        this.HeartRate = heartRate;
    }
}

