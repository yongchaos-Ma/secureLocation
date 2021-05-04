package com.example.myapplication;

import android.graphics.Color;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class ArcInfo{

    private LatLng startPoint;
    private LatLng middlePoint;
    private LatLng endPoint;
//    private double distance;
//    private OverlayOptions mArcOptions = new ArcOptions();

    public static LatLng setStartPoint(LatLng foot , LatLng middlePoint) {
        double angle = LineAndFence.azimuthAngle(foot, middlePoint);

        return LineAndFence.LongLatOffset1(foot.latitude, foot.longitude, angle, 80);
    }

    public LatLng getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(LatLng middlePoint) {
        this.middlePoint = middlePoint;
    }

    public static LatLng setEndPoint(LatLng foot , LatLng middlePoint) {
        double angle = LineAndFence.azimuthAngle(foot, middlePoint);
        return LineAndFence.LongLatOffset2(foot.latitude, foot.longitude, angle, 80);
    }

    private static double generateDistance(LatLng centerPoint, LatLng middlePoint){
        return 0.5 * DistanceUtil. getDistance(centerPoint, middlePoint);
    }

    public static OverlayOptions generateArc(LatLng startPoint , LatLng middlePoint , LatLng endPoint){
        //构造ArcOptions对象
        OverlayOptions mArcOptions = new ArcOptions()
                .color(Color.MAGENTA)
                .width(10)
                .points(startPoint, middlePoint, endPoint);

        return mArcOptions;
    }

}