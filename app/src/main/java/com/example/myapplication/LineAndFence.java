 package com.example.myapplication;

 import com.baidu.mapapi.model.LatLng;
 import com.baidu.mapapi.utils.DistanceUtil;

public class LineAndFence {
    public static double pointToLine(LatLng x1, LatLng x2, LatLng x0) {
        double space = 0;
        double a, b, c;
        a = DistanceUtil. getDistance(x1, x2);// 线段的长度
        b = DistanceUtil. getDistance(x1, x0);// (x1,y1)到点的距离
        c = DistanceUtil. getDistance(x2, x0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
        }
        if (a <= 0.000001) {
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离(利用三角形面积公式求高)
        return space;

    }

// 计算两点之间的距离
    private double lineSpace(double x1, double y1, double x2, double y2) {
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return lineLength;

    }

    public static LatLng getFoot(LatLng p1,LatLng p2,LatLng p3){

        double dx=p1.latitude-p2.latitude;
        double dy=p1.longitude-p2.longitude;

        double u=(p3.latitude-p1.latitude)*dx+(p3.longitude-p1.longitude)*dy;
        u/=dx*dx+dy*dy;

        LatLng foot=new LatLng((p1.latitude+u*dx),(p1.longitude+u*dy));

        return foot;
    }

    /// <summary>
    /// 计算移动后的经纬度
    /// </summary>
    /// <param name="lon">经度</param>
    /// <param name="lat">纬度</param>
    /// <param name="a">方位角（弧度）</param>
    /// <param name="dst">移动距离</param>
    /// <returns></returns>
    /// 1点对应顺势针第一个点，2点对应顺时针第二个点
    public static LatLng LongLatOffset1(double lat, double lon, double a, double dst)
    {
        a = a + (Math.PI / 2.0);
        double arc = 6371.393 * 1000;
        lon = lon + (dst * Math.sin(a) / (arc * Math.cos(lat) * 2 * Math.PI / 360));
        lat = lat + (dst * Math.cos(a) / (arc * 2 * Math.PI / 360));

        return new LatLng(lat, lon);
    }

    public static LatLng LongLatOffset2(double lat, double lon, double a, double dst)
    {
        a = a + (Math.PI / 2.0);
        double arc = 6371.393 * 1000;
        lon = lon -(dst * Math.sin(a) / (arc * Math.cos(lat) * 2 * Math.PI / 360)) ;
        lat = lat -(dst * Math.cos(a) / (arc * 2 * Math.PI / 360)) ;

        return new LatLng(lat, lon) ;
    }

    /**
     * 计算方位角
     *
     * @param a latlng1
     * @param b latlng2
     * @return
     */
    public static double azimuthAngle(LatLng a, LatLng b) {
        double dx, dy, angle = 0;
        double x1 = a.latitude;
        double x2 = b.latitude;
        double y1 = a.longitude;
        double y2 = b.longitude;

        dx = x2 - x1;
        dy = y2 - y1;
        if (x2 == x1) {
            angle = Math.PI / 2.0;
            if (y2 == y1) {
                angle = 0.0;
            } else if (y2 < y1) {
                angle = 3.0 * Math.PI / 2.0;
            }
        } else if ((x2 > x1) && (y2 > y1)) {
            angle = Math.atan(dx / dy);
        } else if ((x2 > x1) && (y2 < y1)) {
            angle = Math.PI / 2 + Math.atan(-dy / dx);
        } else if ((x2 < x1) && (y2 < y1)) {
            angle = Math.PI + Math.atan(dx / dy);
        } else if ((x2 < x1) && (y2 > y1)) {
            angle = 3.0 * Math.PI / 2.0 + Math.atan(dy / -dx);
        }

        return (angle * 180 / Math.PI);
        //return angle ;
    }




}




