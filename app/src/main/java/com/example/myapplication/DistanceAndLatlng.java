package com.example.myapplication;

import com.baidu.mapapi.model.LatLng;

public class DistanceAndLatlng {

    private LatLng nodeLatlng;
    private double nodeDistance;

    public LatLng getNodeLatlng() {
        return nodeLatlng;
    }

    public void setNodeLatlng(LatLng nodeLatlng) {
        this.nodeLatlng = nodeLatlng;
    }

    public double getNodeDistance() {
        return nodeDistance;
    }

    public void setNodeDistance(double nodeDistance) {
        this.nodeDistance = nodeDistance;
    }

}
