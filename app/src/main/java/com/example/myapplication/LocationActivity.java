package com.example.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.myapplication.everyonelitepal.Node1;
import com.example.myapplication.everyonelitepal.Node10;
import com.example.myapplication.everyonelitepal.Node11;
import com.example.myapplication.everyonelitepal.Node12;
import com.example.myapplication.everyonelitepal.Node13;
import com.example.myapplication.everyonelitepal.Node14;
import com.example.myapplication.everyonelitepal.Node15;
import com.example.myapplication.everyonelitepal.Node16;
import com.example.myapplication.everyonelitepal.Node17;
import com.example.myapplication.everyonelitepal.Node18;
import com.example.myapplication.everyonelitepal.Node19;
import com.example.myapplication.everyonelitepal.Node2;
import com.example.myapplication.everyonelitepal.Node20;
import com.example.myapplication.everyonelitepal.Node3;
import com.example.myapplication.everyonelitepal.Node4;
import com.example.myapplication.everyonelitepal.Node5;
import com.example.myapplication.everyonelitepal.Node6;
import com.example.myapplication.everyonelitepal.Node7;
import com.example.myapplication.everyonelitepal.Node8;
import com.example.myapplication.everyonelitepal.Node9;
import com.example.myapplication.everyonelitepal.NodeMother;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.GetTime.dateToStamp;
import static com.example.myapplication.GetTime.format;

public class LocationActivity extends BaseActivity {

    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    public static double latitude = 0.0;    //获取纬度信息
    public static double longitude = 0.0;    //获取经度信息
    public static double accuracy = 0.0;
    public static double direction = 0.0;
    public static String time = null;
    public byte[] localizeBuffer;
    public byte[] warningBuffer;
    protected MapStatusUpdate mapStatusUpdate;
    public LatLng position;
    public LatLng targetPosition;
    public boolean[] MarkerClicked;
    public MarkerOptions options;

    BitmapDescriptor targetIcon = BitmapDescriptorFactory.fromResource(R.drawable.target_icon_neo);

    Map<Integer, NodeMother> mapShow = new HashMap<>();
    Map<Integer, BitmapDescriptor> mapNodePics = new HashMap<>();
    Map<Integer, BitmapDescriptor> mapLostNodePics = new HashMap<Integer, BitmapDescriptor>();
    Map<Integer, BitmapDescriptor> mapIndirectNodePics = new HashMap<Integer, BitmapDescriptor>();
    Map<Integer, BitmapDescriptor> mapWarningNodePics = new HashMap<Integer, BitmapDescriptor>();
    BitmapDescriptor node1 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_01);
    BitmapDescriptor node2 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_02);
    BitmapDescriptor node3 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_03);
    BitmapDescriptor node4 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_04);
    BitmapDescriptor node5 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_05);
    BitmapDescriptor node6 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_06);
    BitmapDescriptor node7 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_07);
    BitmapDescriptor node8 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_08);
    BitmapDescriptor node9 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_09);
    BitmapDescriptor node10 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_10);
    BitmapDescriptor node11 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_11);
    BitmapDescriptor node12 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_12);
    BitmapDescriptor node13 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_13);
    BitmapDescriptor node14 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_14);
    BitmapDescriptor node15 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_15);
    BitmapDescriptor node16 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_16);
    BitmapDescriptor node17 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_17);
    BitmapDescriptor node18 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_18);
    BitmapDescriptor node19 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_19);
    BitmapDescriptor node20 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_20);
    BitmapDescriptor lostNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_01);
    BitmapDescriptor lostNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_02);
    BitmapDescriptor lostNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_03);
    BitmapDescriptor lostNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_04);
    BitmapDescriptor lostNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_05);
    BitmapDescriptor lostNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_06);
    BitmapDescriptor lostNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_07);
    BitmapDescriptor lostNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_08);
    BitmapDescriptor lostNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_09);
    BitmapDescriptor lostNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_10);
    BitmapDescriptor lostNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_11);
    BitmapDescriptor lostNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_12);
    BitmapDescriptor lostNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_13);
    BitmapDescriptor lostNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_14);
    BitmapDescriptor lostNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_15);
    BitmapDescriptor lostNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_16);
    BitmapDescriptor lostNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_17);
    BitmapDescriptor lostNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_18);
    BitmapDescriptor lostNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_19);
    BitmapDescriptor lostNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_20);
    BitmapDescriptor indirectNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_01);
    BitmapDescriptor indirectNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_02);
    BitmapDescriptor indirectNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_03);
    BitmapDescriptor indirectNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_04);
    BitmapDescriptor indirectNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_05);
    BitmapDescriptor indirectNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_06);
    BitmapDescriptor indirectNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_07);
    BitmapDescriptor indirectNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_08);
    BitmapDescriptor indirectNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_09);
    BitmapDescriptor indirectNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_10);
    BitmapDescriptor indirectNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_11);
    BitmapDescriptor indirectNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_12);
    BitmapDescriptor indirectNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_13);
    BitmapDescriptor indirectNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_14);
    BitmapDescriptor indirectNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_15);
    BitmapDescriptor indirectNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_16);
    BitmapDescriptor indirectNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_17);
    BitmapDescriptor indirectNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_18);
    BitmapDescriptor indirectNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_19);
    BitmapDescriptor indirectNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_20);
    BitmapDescriptor warningNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_01);
    BitmapDescriptor warningNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_02);
    BitmapDescriptor warningNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_03);
    BitmapDescriptor warningNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_04);
    BitmapDescriptor warningNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_05);
    BitmapDescriptor warningNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_06);
    BitmapDescriptor warningNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_07);
    BitmapDescriptor warningNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_08);
    BitmapDescriptor warningNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_09);
    BitmapDescriptor warningNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_10);
    BitmapDescriptor warningNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_11);
    BitmapDescriptor warningNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_12);
    BitmapDescriptor warningNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_13);
    BitmapDescriptor warningNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_14);
    BitmapDescriptor warningNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_15);
    BitmapDescriptor warningNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_16);
    BitmapDescriptor warningNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_17);
    BitmapDescriptor warningNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_18);
    BitmapDescriptor warningNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_19);
    BitmapDescriptor warningNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_20);
    Node1 mNode1 = new Node1();
    Node2 mNode2 = new Node2();
    Node3 mNode3 = new Node3();
    Node4 mNode4 = new Node4();
    Node5 mNode5 = new Node5();
    Node6 mNode6 = new Node6();
    Node7 mNode7 = new Node7();
    Node8 mNode8 = new Node8();
    Node9 mNode9 = new Node9();
    Node10 mNode10 = new Node10();
    Node11 mNode11 = new Node11();
    Node12 mNode12 = new Node12();
    Node13 mNode13 = new Node13();
    Node14 mNode14 = new Node14();
    Node15 mNode15 = new Node15();
    Node16 mNode16 = new Node16();
    Node17 mNode17 = new Node17();
    Node18 mNode18 = new Node18();
    Node19 mNode19 = new Node19();
    Node20 mNode20 = new Node20();

    public class MyLocationListener extends BDAbstractLocationListener {
        //在这个方法中接收定位结果
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null){
//                return;
//            }
            MyLocationData locData = new MyLocationData.Builder().build();
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位成功
                Log.i("baidu_location_result", "offline location success");
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                locData = new MyLocationData.Builder()
                        .latitude(lat).longitude(lon)
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .build();

            } else if (location.getLocType() == BDLocation.TypeOffLineLocationFail) {
                // 离线定位失败
                Log.i("baidu_location_result", "offline location fail");
                Toast.makeText(LocationActivity.this,"请打开网络与GPS或移动至空旷区域",Toast.LENGTH_LONG).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude()).longitude(location.getLongitude())
                        .build();
            } else if (location.getLocType() == BDLocation.TypeGpsLocation) {
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude()).longitude(location.getLongitude())
                        .build();
            }else {
                Log.i("baidu_location_result", "location type = " + location.getLocType());
            }

            baiduMap.setMyLocationData(locData);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getRadius();
            direction = location.getDirection();
            time = location.getTime();
//            location.getSatelliteNumber();
//            location.getSpeed();

            Cursor c = LitePal.findBySQL("select * from SelfLocationDatabase");
            int number = c.getCount();

            SelfLocationDatabase selfLocationDatabase = new SelfLocationDatabase();
                List<SelfLocationDatabase> selfLocationDatas = LitePal.order("time desc").limit(1).find(SelfLocationDatabase.class);
                if(number == 0){
                    selfLocationDatabase.setLatitude(latitude);
                    selfLocationDatabase.setLongitude(longitude);
                    try {
                        selfLocationDatabase.setTime(Integer.parseInt(dateToStamp(format)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    selfLocationDatabase.setHeartRate(Integer.parseInt(cutted));
                    selfLocationDatabase.setWarnType(warnTypes);
                    selfLocationDatabase.setAccuracy(accuracy);
                    selfLocationDatabase.setDirection(direction);
                    selfLocationDatabase.save();
                }else {
                    if(selfLocationDatas.get(0).getLatitude() == latitude &&
                            selfLocationDatas.get(0).getLongitude() == longitude){
                        SelfLocationDatabase newData = new SelfLocationDatabase();
                        try {
                            newData.setTime(Integer.parseInt(dateToStamp(format)));
                            newData.setHeartRate(Integer.parseInt(cutted));
                            newData.setWarnType(warnTypes);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        newData.update(selfLocationDatas.get(0).getId());
                    } else if(location.getLocType() == BDLocation.TypeOffLineLocationFail) {

                    } else {
                        selfLocationDatabase.setLatitude(latitude);
                        selfLocationDatabase.setLongitude(longitude);
                        try {
                            selfLocationDatabase.setTime(Integer.parseInt(dateToStamp(format)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        selfLocationDatabase.setHeartRate(Integer.parseInt(cutted));
                        selfLocationDatabase.setWarnType(warnTypes);
                        selfLocationDatabase.setAccuracy(accuracy);
                        selfLocationDatabase.setDirection(direction);
                        selfLocationDatabase.save();
                    }

                }

        }
    }

    public int createRandom (){
        Random random = new Random();//默认构造方法
        int ran = random.nextInt(50);
        ran = ran * 100;
        return ran;
    }

    public void MapCreate(){
        mapShow.put(1, mNode1);
        mapShow.put(2, mNode2);
        mapShow.put(3, mNode3);
        mapShow.put(4, mNode4);
        mapShow.put(5, mNode5);
        mapShow.put(6, mNode6);
        mapShow.put(7, mNode7);
        mapShow.put(8, mNode8);
        mapShow.put(9, mNode9);
        mapShow.put(10, mNode10);
        mapShow.put(11, mNode11);
        mapShow.put(12, mNode12);
        mapShow.put(13, mNode13);
        mapShow.put(14, mNode14);
        mapShow.put(15, mNode15);
        mapShow.put(16, mNode16);
        mapShow.put(17, mNode17);
        mapShow.put(18, mNode18);
        mapShow.put(19, mNode19);
        mapShow.put(20, mNode20);
        mapNodePics.put(1,node1);
        mapNodePics.put(2,node2);
        mapNodePics.put(3,node3);
        mapNodePics.put(4,node4);
        mapNodePics.put(5,node5);
        mapNodePics.put(6,node6);
        mapNodePics.put(7,node7);
        mapNodePics.put(8,node8);
        mapNodePics.put(9,node9);
        mapNodePics.put(10,node10);
        mapNodePics.put(11,node11);
        mapNodePics.put(12,node12);
        mapNodePics.put(13,node13);
        mapNodePics.put(14,node14);
        mapNodePics.put(15,node15);
        mapNodePics.put(16,node16);
        mapNodePics.put(17,node17);
        mapNodePics.put(18,node18);
        mapNodePics.put(19,node19);
        mapNodePics.put(20,node20);
        mapLostNodePics.put(1,lostNode1);
        mapLostNodePics.put(2,lostNode2);
        mapLostNodePics.put(3,lostNode3);
        mapLostNodePics.put(4,lostNode4);
        mapLostNodePics.put(5,lostNode5);
        mapLostNodePics.put(6,lostNode6);
        mapLostNodePics.put(7,lostNode7);
        mapLostNodePics.put(8,lostNode8);
        mapLostNodePics.put(9,lostNode9);
        mapLostNodePics.put(10,lostNode10);
        mapLostNodePics.put(11,lostNode11);
        mapLostNodePics.put(12,lostNode12);
        mapLostNodePics.put(13,lostNode13);
        mapLostNodePics.put(14,lostNode14);
        mapLostNodePics.put(15,lostNode15);
        mapLostNodePics.put(16,lostNode16);
        mapLostNodePics.put(17,lostNode17);
        mapLostNodePics.put(18,lostNode18);
        mapLostNodePics.put(19,lostNode19);
        mapLostNodePics.put(20,lostNode20);
        mapIndirectNodePics.put(1,indirectNode1);
        mapIndirectNodePics.put(2,indirectNode2);
        mapIndirectNodePics.put(3,indirectNode3);
        mapIndirectNodePics.put(4,indirectNode4);
        mapIndirectNodePics.put(5,indirectNode5);
        mapIndirectNodePics.put(6,indirectNode6);
        mapIndirectNodePics.put(7,indirectNode7);
        mapIndirectNodePics.put(8,indirectNode8);
        mapIndirectNodePics.put(9,indirectNode9);
        mapIndirectNodePics.put(10,indirectNode10);
        mapIndirectNodePics.put(11,indirectNode11);
        mapIndirectNodePics.put(12,indirectNode12);
        mapIndirectNodePics.put(13,indirectNode13);
        mapIndirectNodePics.put(14,indirectNode14);
        mapIndirectNodePics.put(15,indirectNode15);
        mapIndirectNodePics.put(16,indirectNode16);
        mapIndirectNodePics.put(17,indirectNode17);
        mapIndirectNodePics.put(18,indirectNode18);
        mapIndirectNodePics.put(19,indirectNode19);
        mapIndirectNodePics.put(20,indirectNode20);
        mapWarningNodePics.put(1,warningNode1);
        mapWarningNodePics.put(2,warningNode2);
        mapWarningNodePics.put(3,warningNode3);
        mapWarningNodePics.put(4,warningNode4);
        mapWarningNodePics.put(5,warningNode5);
        mapWarningNodePics.put(6,warningNode6);
        mapWarningNodePics.put(7,warningNode7);
        mapWarningNodePics.put(8,warningNode8);
        mapWarningNodePics.put(9,warningNode9);
        mapWarningNodePics.put(10,warningNode10);
        mapWarningNodePics.put(11,warningNode11);
        mapWarningNodePics.put(12,warningNode12);
        mapWarningNodePics.put(13,warningNode13);
        mapWarningNodePics.put(14,warningNode14);
        mapWarningNodePics.put(15,warningNode15);
        mapWarningNodePics.put(16,warningNode16);
        mapWarningNodePics.put(17,warningNode17);
        mapWarningNodePics.put(18,warningNode18);
        mapWarningNodePics.put(19,warningNode19);
        mapWarningNodePics.put(20,warningNode20);
    }

    public void init() {
        //SDKInitializer.initialize(this);
        MapCreate();
        MarkerClicked = new  boolean[20] ;
        for(int i = 0;i<20;i++){
            MarkerClicked[i] = true;
        }
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();

        baiduMap.setMyLocationEnabled(true);//开启定位图层
        MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.COMPASS;
        boolean enableDirection = true;
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);

        //设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17);
        baiduMap.setMapStatus(mapStatusUpdate);

        MyLocationConfiguration config = new MyLocationConfiguration(mode,enableDirection,customMarker);
        baiduMap.setMyLocationConfiguration(config);
        mMapView.getChildAt(2).setPadding(0,0,47,185);//这是控制缩放控件的位置
        timerSycLoc.schedule(LocationSycTask, 0, 10000);//定时同步信息
        timer.schedule(task,0,10000);//同步其他点

        mLocationClient.start();//开始定位

        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("适配android 8限制后台定位功能", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(LocationActivity.this);
            Intent nfIntent = new Intent(LocationActivity.this, LocationActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(LocationActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        baiduMap.setOnMapLongClickListener(listener);

    }


    //地图长击事件
    BaiduMap.OnMapLongClickListener listener = new BaiduMap.OnMapLongClickListener() {
        public  void onMapLongClick(LatLng newPoint){
            if(newPoint != null){
                targetPosition = newPoint;
            }
        }
    };

    Timer timer = new Timer();
    final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //BE_WARNED = false;
            int allNum = 0;
            int lostNum = 0;
            baiduMap.clear();
            GetTime.getCurrentTime();
            options = new MarkerOptions();
            if(EXTEND_LEGACY){
                for (Integer nodeNum : mapShow.keySet()) {
                    if(nodeNum != SelfNumber){
                        String Node = "Node" + nodeNum;
                        Cursor c = LitePal.findBySQL("select * from " + Node);
                        int number = c.getCount();

                        if(number != 0){
                            allNum++;
                            List<? extends NodeMother> NodeInfors =
                                    LitePal.order("time desc").limit(1).find(mapShow.get(nodeNum).getClass());
                            position = new LatLng(NodeInfors.get(0).getLatitude(),
                                    NodeInfors.get(0).getLongitude());
                            options.position(position);
                            try {
                                if(NodeInfors.get(0).getTime() < Integer.parseInt(dateToStamp(format))-40){
                                    options.icon(mapLostNodePics.get(nodeNum));
                                    lostNum++;
                                }
                                else if(NodeInfors.get(0).getWarnType() != 0){
                                    options.icon(mapWarningNodePics.get(nodeNum));
                                    //if(GET_WARNED){
                                        Intent intent = new Intent("com.example.myapplication.WARNING_BROADCAST");
                                        intent.setPackage("com.example.myapplication");
                                        intent.putExtra("dangerNode", nodeNum);
                                        mLocalBroadcastManager.sendBroadcast(intent);
                                        BE_WARNED = true;
                                    //}
                                } else if(NodeInfors.get(0).isDirect())
                                    options.icon(mapNodePics.get(nodeNum));
                                else if(!NodeInfors.get(0).isDirect())
                                    options.icon(mapIndirectNodePics.get(nodeNum));
                                baiduMap.addOverlay(options);
                                LOAD_COMPLETED = true;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "现在在显示" + nodeNum);
                        }else
                            Log.d(TAG, "跳过了一张空的表");
                    }else
                        Log.d(TAG, "跳过自身");
                }
            }

            TotalNumber = allNum;
            UnconnectableNumber = lostNum;
            NeighborNumber = allNum - lostNum;
            if((TotalNumber > 1 && NeighborNumber <= 1) || NeighborNumber == 0 && TotalNumber != 0){
                Intent intent = new Intent("com.example.myapplication.LOST_BROADCAST");
                intent.setPackage("com.example.myapplication");
                mLocalBroadcastManager.sendBroadcast(intent);
                warnTypes = EDGE_WARNING;
                BE_WARNED = true;
            }else//if(warnTypes == EDGE_WARNING)
            {
                warnTypes = 0;
                BE_WARNED = false;
            }
            if(!BE_WARNED ){
                Intent intent = new Intent("com.example.myapplication.PEACE_BROADCAST");
                intent.setPackage("com.example.myapplication");
                mLocalBroadcastManager.sendBroadcast(intent);
            }
            if(!points.isEmpty()){
                OverlayOptions mOverlayOptions = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(points)
                        .dottedLine(true)
                        ;
                //在地图上绘制折线
                baiduMap.addOverlay(mOverlayOptions);
                Log.d(TAG, points.get(0).toString());
            }

        }

    };

    public Timer timerSycLoc = new Timer();
    public TimerTask LocationSycTask = new TimerTask() {
        @Override
        public void run() {
            if (CONNECT_STATUS) {
                String localizeData = "#" + SelfNumber + "," +LocationActivity.latitude + "," + LocationActivity.longitude + "," + "\n";
                localizeData = localizeData.replace("\\n","\n");
                localizeBuffer = localizeData.getBytes();
                try {
                    mmOutStream = mmSocket.getOutputStream();
                    mmOutStream.write(localizeBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(warnTypes != 0){
                new Thread(new Thread()).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //int num = warnTypes;
                            Thread.sleep(3000);//延时1s
                            //do something
                            // storage: && num != 0
                            if (CONNECT_STATUS ) {
                                System.out.println("warn " + warnTypes);
                                String WarningMessage = "!" + SelfNumber + "," + "sos" + warnTypes + "," + "\n";
                                WarningMessage = WarningMessage.replace("\\n","\n");
                                warningBuffer = WarningMessage.getBytes();
                                try {
                                    mmOutStream = mmSocket.getOutputStream();
                                    mmOutStream.write(warningBuffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }


        }
    };

//   public void markOnTarget(){
//        if(targetPosition != null && setTarget){
//            OverlayOptions option = new MarkerOptions()
//                    .position(targetPosition)
//                    .icon(targetIcon);
//            //在地图上添加Marker，并显示
//            baiduMap.addOverlay(option);
//        }
//    }

    private void initLocation(){

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //LocationMode. Device_Sensors：仅使用设备；  //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗;

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(10000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        //mMapView.onResume();
        super.onResume();
        mLocationClient.disableLocInForeground(true);
    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        //mMapView.onPause();
        super.onPause();
        mLocationClient.enableLocInForeground(1, notification);
        mLocationClient.start();

    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();//停止定位
        timer.cancel();
        timerSycLoc.cancel();
        super.onDestroy();
    }
}
