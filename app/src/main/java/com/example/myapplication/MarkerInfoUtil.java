package com.example.myapplication;

import java.io.Serializable;

/**
 * 地图标注信息实体类
 * @author jing__jie
 *
 */
public class MarkerInfoUtil implements Serializable{
    //private static final long serialVersionUID = 8633299996744734593L;

    private double latitude;//纬度
    private double longitude;//经度
    private int name;//名字
    //构造方法
    public MarkerInfoUtil() {}
    public MarkerInfoUtil(double latitude, double longitude, int name) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    //toString方法
    @Override
    public String toString() {
        return "MarkerInfoUtil [latitude=" + latitude + ", longitude=" + longitude + ", name=" + name + "]";
    }
    //getter setter
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
    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }
}
