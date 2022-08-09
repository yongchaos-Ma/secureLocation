package com.example.myapplication;

import static com.example.myapplication.GetTime.dateToStamp;
import static com.example.myapplication.GetTime.format;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class LocationActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    private final String TAG = "LocationActivity";
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    public static String time = null;
    private byte[] localizeBuffer;
    private byte[] warningBuffer;
    private byte[] recordBuffer;
    private byte[] loraSettingBuffer;
    private byte[] btSettingBuffer;
    protected MapStatusUpdate mapStatusUpdate;
    public LatLng position;
    private String recordResult = null;
    //private String recordMessage = null;
    private final HashMap<Integer, String> messageTable = new HashMap<>();
    private List<InfoWindow> infoWindowsList;
    private Boolean SETTING_CHANGED = false;
    private Boolean NAME_CHANGED = false;
    private String loraSetting = null;
    private String bluetoothSetting = null;
    //private final List<ArcInfo> arc = new ArrayList<>();
    public List<LatLng> fencePoints = new ArrayList<>();

    public boolean[] MarkerClicked;
    public MarkerOptions options;

    // 浏览路线节点相关
    private RouteLine mRouteLine = null;
    private final boolean mUseDefaultIcon = false;
    private OverlayManager mRouteOverlay = null;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可

    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch = null;
    private WalkingRouteResult mWalkingRouteResult = null;
    private boolean hasShowDialog = false;

    private final BitmapDescriptor targetIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_target);
    private final BitmapDescriptor footIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo_yellow);
    private static final BitmapDescriptor startIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_start);
    private final BitmapDescriptor textureBlue = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_blue_arrow);
    private final BitmapDescriptor littleIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo_little);
    private final BitmapDescriptor littleIconRed = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo_little_red);
    //    private final BitmapDescriptor textureGreen = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_green_arrow);
//    private final BitmapDescriptor textureNoFocus = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_nofocus);
//    private final BitmapDescriptor textureRed = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_red_arrow);
//    private final BitmapDescriptor textureYellow = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_yellow_arrow);

    private final Map<Integer, NodeMother> mapShow = new HashMap<>();
    private final Map<Integer, BitmapDescriptor> mapNodePics = new HashMap<>();
    private final Map<Integer, BitmapDescriptor> mapLostNodePics = new HashMap<>();
    private final Map<Integer, BitmapDescriptor> mapIndirectNodePics = new HashMap<>();
    private final Map<Integer, BitmapDescriptor> mapWarningNodePics = new HashMap<>();
    private final Map<Integer, MarkerOptions> mapMarkerObjects = new HashMap<>();
    private final Map<Integer, Marker> markerObjectsAsMarker = new HashMap<>();
    private final Map<Integer, Integer> messageNumAndReceivedTime = new HashMap<>();
    private final Map<Integer, DistanceAndLatlng> distanceMap = new TreeMap<>();
    private final Map<Integer,LatLng> numToDistanceMap = new HashMap<>();
    private final BitmapDescriptor node1 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_01);
    private final BitmapDescriptor node2 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_02);
    private final BitmapDescriptor node3 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_03);
    private final BitmapDescriptor node4 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_04);
    private final BitmapDescriptor node5 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_05);
    private final BitmapDescriptor node6 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_06);
    private final BitmapDescriptor node7 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_07);
    private final BitmapDescriptor node8 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_08);
    private final BitmapDescriptor node9 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_09);
    private final BitmapDescriptor node10 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_10);
    private final BitmapDescriptor node11 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_11);
    private final BitmapDescriptor node12 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_12);
    private final BitmapDescriptor node13 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_13);
    private final BitmapDescriptor node14 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_14);
    private final BitmapDescriptor node15 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_15);
    private final BitmapDescriptor node16 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_16);
    private final BitmapDescriptor node17 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_17);
    private final BitmapDescriptor node18 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_18);
    private final BitmapDescriptor node19 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_19);
    private final BitmapDescriptor node20 = BitmapDescriptorFactory.fromResource(R.drawable.node_red_20);
    private final BitmapDescriptor lostNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_01);
    private final BitmapDescriptor lostNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_02);
    private final BitmapDescriptor lostNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_03);
    private final BitmapDescriptor lostNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_04);
    private final BitmapDescriptor lostNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_05);
    private final BitmapDescriptor lostNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_06);
    private final BitmapDescriptor lostNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_07);
    private final BitmapDescriptor lostNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_08);
    private final BitmapDescriptor lostNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_09);
    private final BitmapDescriptor lostNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_10);
    private final BitmapDescriptor lostNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_11);
    private final BitmapDescriptor lostNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_12);
    private final BitmapDescriptor lostNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_13);
    private final BitmapDescriptor lostNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_14);
    private final BitmapDescriptor lostNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_15);
    private final BitmapDescriptor lostNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_16);
    private final BitmapDescriptor lostNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_17);
    private final BitmapDescriptor lostNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_18);
    private final BitmapDescriptor lostNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_19);
    private final BitmapDescriptor lostNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_gray_20);
    private final BitmapDescriptor indirectNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_01);
    private final BitmapDescriptor indirectNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_02);
    private final BitmapDescriptor indirectNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_03);
    private final BitmapDescriptor indirectNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_04);
    private final BitmapDescriptor indirectNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_05);
    private final BitmapDescriptor indirectNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_06);
    private final BitmapDescriptor indirectNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_07);
    private final BitmapDescriptor indirectNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_08);
    private final BitmapDescriptor indirectNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_09);
    private final BitmapDescriptor indirectNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_10);
    private final BitmapDescriptor indirectNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_11);
    private final BitmapDescriptor indirectNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_12);
    private final BitmapDescriptor indirectNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_13);
    private final BitmapDescriptor indirectNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_14);
    private final BitmapDescriptor indirectNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_15);
    private final BitmapDescriptor indirectNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_16);
    private final BitmapDescriptor indirectNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_17);
    private final BitmapDescriptor indirectNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_18);
    private final BitmapDescriptor indirectNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_19);
    private final BitmapDescriptor indirectNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_blue_20);
    private final BitmapDescriptor warningNode1 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_01);
    private final BitmapDescriptor warningNode2 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_02);
    private final BitmapDescriptor warningNode3 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_03);
    private final BitmapDescriptor warningNode4 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_04);
    private final BitmapDescriptor warningNode5 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_05);
    private final BitmapDescriptor warningNode6 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_06);
    private final BitmapDescriptor warningNode7 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_07);
    private final BitmapDescriptor warningNode8 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_08);
    private final BitmapDescriptor warningNode9 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_09);
    private final BitmapDescriptor warningNode10 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_10);
    private final BitmapDescriptor warningNode11 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_11);
    private final BitmapDescriptor warningNode12 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_12);
    private final BitmapDescriptor warningNode13 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_13);
    private final BitmapDescriptor warningNode14 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_14);
    private final BitmapDescriptor warningNode15 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_15);
    private final BitmapDescriptor warningNode16 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_16);
    private final BitmapDescriptor warningNode17 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_17);
    private final BitmapDescriptor warningNode18 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_18);
    private final BitmapDescriptor warningNode19 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_19);
    private final BitmapDescriptor warningNode20 = BitmapDescriptorFactory.fromResource(R.drawable.node_orange_20);
    private final Node1 mNode1 = new Node1();
    private final Node2 mNode2 = new Node2();
    private final Node3 mNode3 = new Node3();
    private final Node4 mNode4 = new Node4();
    private final Node5 mNode5 = new Node5();
    private final Node6 mNode6 = new Node6();
    private final Node7 mNode7 = new Node7();
    private final Node8 mNode8 = new Node8();
    private final Node9 mNode9 = new Node9();
    private final Node10 mNode10 = new Node10();
    private final Node11 mNode11 = new Node11();
    private final Node12 mNode12 = new Node12();
    private final Node13 mNode13 = new Node13();
    private final Node14 mNode14 = new Node14();
    private final Node15 mNode15 = new Node15();
    private final Node16 mNode16 = new Node16();
    private final Node17 mNode17 = new Node17();
    private final Node18 mNode18 = new Node18();
    private final Node19 mNode19 = new Node19();
    private final Node20 mNode20 = new Node20();
    public MarkerOptions node1Options = new MarkerOptions();
    public MarkerOptions node2Options = new MarkerOptions();
    public MarkerOptions node3Options = new MarkerOptions();
    public MarkerOptions node4Options = new MarkerOptions();
    public MarkerOptions node5Options = new MarkerOptions();
    public MarkerOptions node6Options = new MarkerOptions();
    public MarkerOptions node7Options = new MarkerOptions();
    public MarkerOptions node8Options = new MarkerOptions();
    public MarkerOptions node9Options = new MarkerOptions();
    public MarkerOptions node10Options = new MarkerOptions();
    public MarkerOptions node11Options = new MarkerOptions();
    public MarkerOptions node12Options = new MarkerOptions();
    public MarkerOptions node13Options = new MarkerOptions();
    public MarkerOptions node14Options = new MarkerOptions();
    public MarkerOptions node15Options = new MarkerOptions();
    public MarkerOptions node16Options = new MarkerOptions();
    public MarkerOptions node17Options = new MarkerOptions();
    public MarkerOptions node18Options = new MarkerOptions();
    public MarkerOptions node19Options = new MarkerOptions();
    public MarkerOptions node20Options = new MarkerOptions();
    //public Marker node10Options = new MarkerOptions();
//    public Marker node1MarkerObject;
//    public Marker node2MarkerObject;
//    public Marker node3MarkerObject;
//    public Marker node4MarkerObject;
//    public Marker node5MarkerObject;
//    public Marker node6MarkerObject;
//    public Marker node7MarkerObject;
//    public Marker node8MarkerObject;
//    public Marker node9MarkerObject;
//    public Marker node10MarkerObject;
//    public Marker node11MarkerObject;
//    public Marker node12MarkerObject;
//    public Marker node13MarkerObject;
//    public Marker node14MarkerObject;
//    public Marker node15MarkerObject;
//    public Marker node16MarkerObject;
//    public Marker node17MarkerObject;
//    public Marker node18MarkerObject;
//    public Marker node19MarkerObject;
//    public Marker node20MarkerObject;

    private int count = 1;
    private final OverlayOptions targetMarker = new MarkerOptions();
    private final OverlayOptions footMarker = new MarkerOptions();
    private final OverlayOptions startMarker = new MarkerOptions();
    private final OverlayOptions polyLine = new PolylineOptions();
    private final LatLng selfLatLng = new LatLng(0.0,0.0);
    private boolean isVirating = false;
    private final List<LatLng> polyPoints = new ArrayList<>();
    private Polygon mPolygon;
    boolean In_OR_OUT = true;



    private int GPSAffirmedTimes = 0;
    private int NetworkAffirmedTimes = 0;
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
                NetworkAffirmedTimes++;
                Log.d(TAG, "网络定位定位成功，次数："+NetworkAffirmedTimes);
                Log.d(TAG, "网络定位结果：("+ locData.latitude+","+locData.longitude+")");

            } else if (location.getLocType() == BDLocation.TypeGpsLocation) {
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude()).longitude(location.getLongitude())
                        .build();
                GPSAffirmedTimes++;
                Log.d(TAG, "GPS定位定位成功，次数："+GPSAffirmedTimes);
                Log.d(TAG, "GPS定位结果：("+ locData.latitude+","+locData.longitude+")");

            }else {
                Log.d("baidu_location_result", "location type = " + location.getLocType());
            }

            baiduMap.setMyLocationData(locData);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            accuracy = location.getRadius();
            direction = location.getDirection();
            //Toast.makeText(LocationActivity.this,"direction: " + direction,Toast.LENGTH_SHORT).show();
//            time = location.getTime();
//            location.getSatelliteNumber();
//            location.getSpeed();

            new Thread(() -> {
                //mSelfDatabaseBinder.SelfDatabaseOperation(latitude,longitude,accuracy,direction,cutted,warnTypes);

                {
//                        if(Looper.getMainLooper() == Looper.myLooper())
//                            Log.d("SelfStorage", "Service is in the main thread.");
//                        else Log.d("SelfStorage", "Service is not in the main thread!");

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
            }).start();
        }
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
        mapMarkerObjects.put(1,node1Options);
        mapMarkerObjects.put(2,node2Options);
        mapMarkerObjects.put(3,node3Options);
        mapMarkerObjects.put(4,node4Options);
        mapMarkerObjects.put(5,node5Options);
        mapMarkerObjects.put(6,node6Options);
        mapMarkerObjects.put(7,node7Options);
        mapMarkerObjects.put(8,node8Options);
        mapMarkerObjects.put(9,node9Options);
        mapMarkerObjects.put(10,node10Options);
        mapMarkerObjects.put(11,node11Options);
        mapMarkerObjects.put(12,node12Options);
        mapMarkerObjects.put(13,node13Options);
        mapMarkerObjects.put(14,node14Options);
        mapMarkerObjects.put(15,node15Options);
        mapMarkerObjects.put(16,node16Options);
        mapMarkerObjects.put(17,node17Options);
        mapMarkerObjects.put(18,node18Options);
        mapMarkerObjects.put(19,node19Options);
        mapMarkerObjects.put(20,node20Options);

//        markerObjectsAsMarker.put(1,node1MarkerObject);
//        markerObjectsAsMarker.put(2,node2MarkerObject);
//        markerObjectsAsMarker.put(3,node3MarkerObject);
//        markerObjectsAsMarker.put(4,node4MarkerObject);
//        markerObjectsAsMarker.put(5,node5MarkerObject);
//        markerObjectsAsMarker.put(6,node6MarkerObject);
//        markerObjectsAsMarker.put(7,node7MarkerObject);
//        markerObjectsAsMarker.put(8,node8MarkerObject);
//        markerObjectsAsMarker.put(9,node9MarkerObject);
//        markerObjectsAsMarker.put(10,node10MarkerObject);
//        markerObjectsAsMarker.put(11,node11MarkerObject);
//        markerObjectsAsMarker.put(12,node12MarkerObject);
//        markerObjectsAsMarker.put(13,node13MarkerObject);
//        markerObjectsAsMarker.put(14,node14MarkerObject);
//        markerObjectsAsMarker.put(15,node15MarkerObject);
//        markerObjectsAsMarker.put(16,node16MarkerObject);
//        markerObjectsAsMarker.put(17,node17MarkerObject);
//        markerObjectsAsMarker.put(18,node18MarkerObject);
//        markerObjectsAsMarker.put(19,node19MarkerObject);
//        markerObjectsAsMarker.put(20,node20MarkerObject);
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
        //mMapView.getChildAt(2).setPadding(0,0,47,185);//这是控制缩放控件的位置

        timerSycLoc.schedule(LocationSycTask, 0, 10000);//定时同步信息
        timer.schedule(task,0,10000);//同步其他点

        mLocationClient.start();//开始定位

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

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
        //地图长击事件

        //markOnTarget();
    }

    BaiduMap.OnMapLongClickListener listener = new BaiduMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng newPoint) {
            if (newPoint != null) {
                newDestination = newPoint;
                Toast.makeText(LocationActivity.this, "Long click: " + newDestination,
                        Toast.LENGTH_SHORT).show();

            }
        }
    };

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess() {

// 重置浏览节点的路线数据
        mRouteLine = null;
        // 设置起终点信息 起点参数
        //PlanNode startNode = PlanNode.withCityNameAndPlaceName("西安","西安邮电大学长安校区家属院");
        LatLng self = new LatLng(latitude, longitude);
        PlanNode startNode = PlanNode.withLocation(self);
        Log.d(TAG, "searchButtonProcess: " + startNode.getLocation());
        // 终点参数
        PlanNode endNode = PlanNode.withLocation(newDestination);
//        PlanNode endNode = PlanNode.withCityNameAndPlaceName("西安","赛格国际购物中心");
        Log.d(TAG, "searchButtonProcess: " + endNode.getLocation());

        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(startNode) // 起点
                .to(endNode)); // 终点

    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        private MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (mUseDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (mUseDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    /**
     * 步行路线结果回调
     *
     * @param result  步行路线结果
     */
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if( mRouteOverlay != null ){
            //baiduMap.clear();
            distanceMap.clear();
            numToDistanceMap.clear();
            markerTimer(0);
        }

        if (null == result) {
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            Log.d(TAG, "onGetWalkingRouteResult: " + result.getSuggestAddrInfo().getSuggestStartNode());
            Log.d(TAG, "onGetWalkingRouteResult: " + result.getSuggestAddrInfo().getSuggestEndNode());
            return;
        }

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {

            if (result.getRouteLines().size() > 1) {
                mWalkingRouteResult = result;
                if (!hasShowDialog) {
                    SelectRouteDialog selectRouteDialog = new SelectRouteDialog(this,
                            result.getRouteLines(), RouteLineAdapter.Type.WALKING_ROUTE);
                    selectRouteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShowDialog = false;
                        }
                    });
                    selectRouteDialog.setOnItemInDlgClickLinster(new SelectRouteDialog.OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            mRouteLine = mWalkingRouteResult.getRouteLines().get(position);
                            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            overlay.setData(mWalkingRouteResult.getRouteLines().get(position));
                            overlay.zoomToSpan();
                            mRouteOverlay = overlay;
                            mRouteOverlay.addToMap();
                        }

                    });
                    selectRouteDialog.show();
                    hasShowDialog = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                mRouteLine = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap);
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.zoomToSpan();
                mRouteOverlay = overlay;
                mRouteOverlay.addToMap();

                List route = mRouteLine.getAllStep();
//              mRouteLine.getAllStep().get(i).toString();
//              Polyline polyline = (Polyline) mRouteLine;
                for(int k = 0 ; k <= mRouteLine.getAllStep().size() -1 ; k++){

                    RouteStep stepPart = (RouteStep) route.get(k);
                    LatLng start =  stepPart.getWayPoints().get(0);
                    LatLng end = stepPart.getWayPoints().get(stepPart.getWayPoints().size() -1 );

                    OverlayOptions startOverlay = new MarkerOptions()
                            .position(start)
                            .icon(littleIcon);
                    //在地图上添加Marker，并显示
                    baiduMap.addOverlay(startOverlay);

                    OverlayOptions endOverlay = new MarkerOptions()
                            .position(end)
                            .alpha(0.7f)
                            .icon(littleIconRed);
                    //在地图上添加Marker，并显示
                    baiduMap.addOverlay(endOverlay);

                    Log.d(TAG, "onRoutePrintOut: " + k + " start: " + start + " end: "+end);

                    LatLng self = new LatLng(latitude, longitude);
                    DistanceAndLatlng nodeStart = new DistanceAndLatlng();
                    nodeStart.setNodeDistance(DistanceUtil.getDistance(self, start));
                    nodeStart.setNodeLatlng(start);

                    DistanceAndLatlng nodeEnd = new DistanceAndLatlng();
                    nodeEnd.setNodeDistance(DistanceUtil.getDistance(self, end));
                    nodeEnd.setNodeLatlng(end);

                    distanceMap.put(2*k, nodeStart);
                    distanceMap.put((2*k)+1, nodeEnd);
                    //Log.d(TAG, "inDistanceMap: start: " + distanceMap.get(start) + "; end: " + distanceMap.get(end));

                }


            } else {
                Log.d("route result", "结果数<0");
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    private void markerTimer(int timeNow){

        for (Integer nodeNum : mapShow.keySet()){ //进入for循环，按顺序遍历（nodeNum为指引）

            if(nodeNum != SelfNumber){

                //语音消息过期判断
                if(messageNumAndReceivedTime.containsKey(nodeNum)){
                    try {
                        timeNow = Integer.parseInt(dateToStamp(format));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(timeNow - messageNumAndReceivedTime.get(nodeNum)  > 40){
                        Log.d(TAG, "clearMessage:" + messageNumAndReceivedTime.get(nodeNum));
                        MarkerOptions emptyMarker = new MarkerOptions();
                        messageNumAndReceivedTime.remove(nodeNum);
                        messageTable.remove(nodeNum);
                        mapMarkerObjects.remove(nodeNum);
                        mapMarkerObjects.put(nodeNum, emptyMarker);
//                                Marker marker = null;
//                                mapMarkerObjects.put(nodeNum, marker);
                    }
                }

                String Node = "Node" + nodeNum;//拼出表名
                Cursor c = LitePal.findBySQL("select * from " + Node);
                int number = c.getCount();

                if(number != 0){//确定所获取的表内的数据不是0条
                    TotalNumber++;
                    List<? extends NodeMother> NodeInfors =
                            LitePal.order("time desc")
                                    .limit(1)
                                    .find(mapShow.get(nodeNum).getClass());
                    //寻找目标表，以时间排序，获取第一行数据（最新数据）

                    if(markerObjectsAsMarker.containsKey(nodeNum) ){
                        Objects.requireNonNull(markerObjectsAsMarker.get(nodeNum)).remove();
                    }

                    position = new LatLng(NodeInfors.get(0).getLatitude(),
                            NodeInfors.get(0).getLongitude());
                    Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).position(position);
                    //mapMarkers.get(nodeNum).setPosition(position);
                    //options.position(position);
                    try {
                        if(NodeInfors.get(0).getTime() < Integer.parseInt(dateToStamp(format))-40){
                            //options.icon(mapLostNodePics.get(nodeNum));
                            Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapLostNodePics.get(nodeNum));
                            //mapMarkers.get(nodeNum).setIcon(mapLostNodePics.get(nodeNum));
                            UnconnectableNumber++;
                        }
                        else if(NodeInfors.get(0).getWarnType() != 0){
                            //options.icon(mapWarningNodePics.get(nodeNum));
                            Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapWarningNodePics.get(nodeNum));
                            //mapMarkers.get(nodeNum).setIcon(mapWarningNodePics.get(nodeNum));
                            //if(GET_WARNED){
//                                        Intent intent = new Intent("com.example.myapplication.WARNING_BROADCAST");
//                                        intent.setPackage("com.example.myapplication");
//                                        intent.putExtra("dangerNode", nodeNum);
                            //mLocalBroadcastManager.sendBroadcast(intent);
                            EventBus.getDefault().post(new MessageEvent(nodeNum.toString()));
                            BE_WARNED = true;
                            //}
                        } else if(NodeInfors.get(0).isDirect())
                            //options.icon(mapNodePics.get(nodeNum));
                            Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapNodePics.get(nodeNum));
                            //mapMarkers.get(nodeNum).setIcon(mapNodePics.get(nodeNum));
                        else if(!NodeInfors.get(0).isDirect())
                            //options.icon(mapIndirectNodePics.get(nodeNum));
                            Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapIndirectNodePics.get(nodeNum));
                        //mapMarkers.get(nodeNum).setIcon(mapIndirectNodePics.get(nodeNum));


                        //baiduMap.addOverlay(options);
                        //响应点击的OnInfoWindowClickListener
                        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick() {
                                Toast.makeText(LocationActivity.this, "Click on InfoWindow", Toast.LENGTH_LONG).show();
                                new SpeakModule().speechSync(LocationActivity.this, messageTable.get(nodeNum));
                            }
                        };
                        //判断是否添加语音消息框
                        if(messageTable.containsKey(nodeNum)){
                            TextView tv = new TextView(LocationActivity.this);
                            tv.setText(messageTable.get(nodeNum));
                            tv.setPadding(10,5,10,5);
                            tv.setBackgroundResource(R.drawable.popup);
                            //用来构造InfoWindow
                            BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromView(tv);
                            //构造InfoWindow
                            //point 描述的位置点
                            //-100 InfoWindow相对于point在y轴的偏移量
                            LatLng point = position;
                            InfoWindow mInfoWindow = new InfoWindow(mBitmap, point, -100, listener);
                            mapMarkerObjects.get(nodeNum).infoWindow(mInfoWindow);
                            //Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).showInfoWindow(mInfoWindow);
                        }
                        //Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).setVisible(true);

                        Marker marker =(Marker) (baiduMap.addOverlay(mapMarkerObjects.get(nodeNum)));
                        markerObjectsAsMarker.put(nodeNum, marker);
                        //Log.d(TAG, "Marker:" + markerObjectsAsMarker.get(nodeNum));


                        LOAD_COMPLETED = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "现在在显示" + nodeNum );
                }else
                    Log.d(TAG, "跳过了一张空的表");

            }else
                Log.d(TAG, "跳过自身");
        }


    }


    Timer timer = new Timer();
    final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            BE_WARNED = false;
            TotalNumber = 0;
            UnconnectableNumber = 0;
            int timeNow = 0;
            //baiduMap.clear();
            GetTime.getCurrentTime();
            options = new MarkerOptions();
            if(EXTEND_LEGACY){

                for (Integer nodeNum : mapShow.keySet()){ //进入for循环，按顺序遍历（nodeNum为指引）

                    if(nodeNum != SelfNumber){

                        //语音消息过期判断
                        if(messageNumAndReceivedTime.containsKey(nodeNum)){
                            try {
                                timeNow = Integer.parseInt(dateToStamp(format));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(timeNow - messageNumAndReceivedTime.get(nodeNum)  > 40){
                                Log.d(TAG, "clearMessage:" + messageNumAndReceivedTime.get(nodeNum));
                                MarkerOptions emptyMarker = new MarkerOptions();
                                messageNumAndReceivedTime.remove(nodeNum);
                                messageTable.remove(nodeNum);
                                mapMarkerObjects.remove(nodeNum);
                                mapMarkerObjects.put(nodeNum, emptyMarker);
//                                Marker marker = null;
//                                mapMarkerObjects.put(nodeNum, marker);
                            }
                        }

                        String Node = "Node" + nodeNum;//拼出表名
                        Cursor c = LitePal.findBySQL("select * from " + Node);
                        int number = c.getCount();

                        if(number != 0){//确定所获取的表内的数据不是0条
                            TotalNumber++;
                            List<? extends NodeMother> NodeInfors =
                                    LitePal.order("time desc")
                                            .limit(1)
                                            .find(mapShow.get(nodeNum).getClass());
                            //寻找目标表，以时间排序，获取第一行数据（最新数据）

                            if(markerObjectsAsMarker.containsKey(nodeNum) ){
                                Objects.requireNonNull(markerObjectsAsMarker.get(nodeNum)).remove();
                            }

                            position = new LatLng(NodeInfors.get(0).getLatitude(),
                                    NodeInfors.get(0).getLongitude());
                            Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).position(position);
                            //mapMarkers.get(nodeNum).setPosition(position);
                            //options.position(position);
                            try {
                                if(NodeInfors.get(0).getTime() < Integer.parseInt(dateToStamp(format))-40){
                                    //options.icon(mapLostNodePics.get(nodeNum));
                                    Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapLostNodePics.get(nodeNum));
                                    //mapMarkers.get(nodeNum).setIcon(mapLostNodePics.get(nodeNum));
                                    UnconnectableNumber++;
                                }
                                else if(NodeInfors.get(0).getWarnType() != 0){
                                    //options.icon(mapWarningNodePics.get(nodeNum));
                                    Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapWarningNodePics.get(nodeNum));
                                    //mapMarkers.get(nodeNum).setIcon(mapWarningNodePics.get(nodeNum));
                                    //if(GET_WARNED){
//                                        Intent intent = new Intent("com.example.myapplication.WARNING_BROADCAST");
//                                        intent.setPackage("com.example.myapplication");
//                                        intent.putExtra("dangerNode", nodeNum);
                                    //mLocalBroadcastManager.sendBroadcast(intent);
                                    EventBus.getDefault().post(new MessageEvent(nodeNum.toString()));
                                    BE_WARNED = true;
                                    //}
                                } else if(NodeInfors.get(0).isDirect())
                                    //options.icon(mapNodePics.get(nodeNum));
                                    Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapNodePics.get(nodeNum));
                                    //mapMarkers.get(nodeNum).setIcon(mapNodePics.get(nodeNum));
                                else if(!NodeInfors.get(0).isDirect())
                                    //options.icon(mapIndirectNodePics.get(nodeNum));
                                    Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).icon(mapIndirectNodePics.get(nodeNum));
                                //mapMarkers.get(nodeNum).setIcon(mapIndirectNodePics.get(nodeNum));


                                //baiduMap.addOverlay(options);
                                //响应点击的OnInfoWindowClickListener
                                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick() {
                                        Toast.makeText(LocationActivity.this, "Click on InfoWindow", Toast.LENGTH_LONG).show();
                                        new SpeakModule().speechSync(LocationActivity.this, messageTable.get(nodeNum));
                                    }
                                };
                                //判断是否添加语音消息框
                                if(messageTable.containsKey(nodeNum)){
                                    TextView tv = new TextView(LocationActivity.this);
                                    tv.setText(messageTable.get(nodeNum));
                                    tv.setPadding(10,5,10,5);
                                    tv.setBackgroundResource(R.drawable.popup);
                                    //用来构造InfoWindow
                                    BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromView(tv);
                                    //构造InfoWindow
                                    //point 描述的位置点
                                    //-100 InfoWindow相对于point在y轴的偏移量
                                    LatLng point = position;
                                    InfoWindow mInfoWindow = new InfoWindow(mBitmap, point, -100, listener);
                                    mapMarkerObjects.get(nodeNum).infoWindow(mInfoWindow);
                                    //Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).showInfoWindow(mInfoWindow);
                                }
                                //Objects.requireNonNull(mapMarkerObjects.get(nodeNum)).setVisible(true);

                                Marker marker =(Marker) (baiduMap.addOverlay(mapMarkerObjects.get(nodeNum)));
                                markerObjectsAsMarker.put(nodeNum, marker);
                                //Log.d(TAG, "Marker:" + markerObjectsAsMarker.get(nodeNum));


                                LOAD_COMPLETED = true;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "现在在显示" + nodeNum );
                        }else
                            Log.d(TAG, "跳过了一张空的表");

                    }else
                        Log.d(TAG, "跳过自身");
                }
            }

            NeighborNumber = TotalNumber - UnconnectableNumber;

            if((TotalNumber > 1 && NeighborNumber <= 1) || NeighborNumber == 0 && TotalNumber != 0){
                Intent intent = new Intent("com.example.myapplication.LOST_BROADCAST");
                intent.setPackage("com.example.myapplication");
                mLocalBroadcastManager.sendBroadcast(intent);
                //warnTypes = EDGE_WARNING;
                BE_WARNED = true;
            }else if(warnTypes == EDGE_WARNING)
            {
                //warnTypes = 0;
                BE_WARNED = false;
            }
            if(!BE_WARNED ){
//                Intent intent = new Intent("com.example.myapplication.PEACE_BROADCAST");
//                intent.setPackage("com.example.myapplication");
//                mLocalBroadcastManager.sendBroadcast(intent);
                EventBus.getDefault().post(new PeaceMessageEvent(true));
            }

            new Thread(() -> fenceAddProg()).start();


        }

    };



    public class MessageEvent {

        public final String message;
        //= "Eventbus test";

        public MessageEvent(String message) {
            this.message = message;
        }

    }

    public class PeaceMessageEvent {

        public final Boolean peaceIndex;
        //= "Eventbus test";

        public PeaceMessageEvent(Boolean peaceIndex) {
            this.peaceIndex = peaceIndex;
        }

    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe()
    public void onRecordMessageEvent(BaseActivity.RecordMessageEvent event) {
        Log.d(TAG, "onRecordMessageEvent: " + event.message);
        if(event.message != null)
            recordResult = event.message;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordMessageEvent(HeadAnalysis.RecordMessageEvent event) {
        int number = Integer.parseInt( event.recordMessage[0]);
        String mandarin = StringTrans.hexGBK2String(event.recordMessage[1]);
        Log.d(TAG, "onRecordMessageEvent: " + mandarin);

        messageTable.remove(number);
        messageTable.put(number,mandarin);
        try {
            messageNumAndReceivedTime.put(number,Integer.parseInt(dateToStamp(format)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //响应点击的OnInfoWindowClickListener
        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                //Toast.makeText(LocationActivity.this, "Click on InfoWindow", Toast.LENGTH_LONG).show();
                new SpeakModule().speechSync(LocationActivity.this, mandarin);
            }
        };
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangedEvent(LoraSetting.SettingChangedEvent event) {
        String readyToSend = null;
        String newSpeed = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("speed_preference", "4800");
        String newPower = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("power_preference", "22");
        String newChannel = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("channel_preference", "1");
        String newCode = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("code_preference", "0");
//        String newName = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString("rename_preference", "DEVICE "+SelfNumber);
        Log.d(TAG, "onRecordMessageEvent: " + event.isChanged);
        if(event.isChanged){

            loraSetting = "$" +newSpeed+ "," +newPower+ "," +newChannel+ "," +newCode+ "," + "\n";
            loraSetting = loraSetting.replace("\\n","\n");
            //bluetoothSetting = "*" + newName + "\n";
            //bluetoothSetting = bluetoothSetting.replace("\\n","\n");
            SETTING_CHANGED = true;
            Log.d(TAG, "onSettingChangedEvent: " + "Speed:"+ newSpeed + "Power"+ newPower + "Channel:"+ newChannel + "Code" + newCode);
            //Log.d(TAG, "onSettingChangedEvent: Name:" + newName);
            //settingChangedAlert(newName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNameChangedEvent(LoraSetting.NameChangedEvent event) {
        String newName = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("rename_preference", "DEVICE "+SelfNumber);
        Log.d(TAG, "onNameChangedEvent: " + event.nameIsChanged);
        if(event.nameIsChanged){
            bluetoothSetting = "*" + newName + "\n";
            bluetoothSetting = bluetoothSetting.replace("\\n","\n");
            NAME_CHANGED = true;
            Log.d(TAG, "onSettingChangedEvent: Name:" + newName);
            settingChangedAlert(newName);
        }
    }
    private void settingChangedAlert(String NodeNumber){
        //测试按键
        AlertDialog.Builder loadBuilder = new AlertDialog.Builder(LocationActivity.this);
        loadBuilder.setTitle("应用参数已修改，请重新连接设备，设备名：" + NodeNumber)
                .setIcon(R.drawable.ic_bell_o)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        loadBuilder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRouteSearchEvent(BaseActivity.routeSearchEvent event) {
        if(event.callUp){
            searchButtonProcess();
        }
    }


    /**
     * @param lat_a 纬度1
     * @param lng_a 经度1
     * @param lat_b 纬度2
     * @param lng_b 经度2
     * @return
     */
    private double getAngle1(double lat_a, double lng_a, double lat_b, double lng_b) {

        double y = Math.sin(lng_b - lng_a) * Math.cos(lat_b);
        double x = Math.cos(lat_a) * Math.sin(lat_b) - Math.sin(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        if (brng < 0)
            brng = brng + 360;
        return brng;
    }
    private void fenceAddProg(){

//        for(int i=0; i < numToDistanceMap.size(); i++){
//            Double distance = distanceMap.get( numToDistanceMap.get(i) );
//        }
        if (!(mPolygon == null)){
            mPolygon.remove();
        }

        if(setTarget) {
            LatLng newSelfPos = new LatLng(latitude, longitude);
            for (int i = 0; i < distanceMap.size(); i++) {
                Log.d(TAG, "fenceAddProg: distance:" + distanceMap.get(i).getNodeDistance()
                        + " latlng" + distanceMap.get(i).getNodeLatlng());
                double newDistance = DistanceUtil.getDistance(newSelfPos, distanceMap.get(i).getNodeLatlng());
                DistanceAndLatlng newDisAndLN = new DistanceAndLatlng();
                newDisAndLN.setNodeDistance(newDistance);
                newDisAndLN.setNodeLatlng(distanceMap.get(i).getNodeLatlng());
                distanceMap.remove(i);
                distanceMap.put(i, newDisAndLN);
            }
            LatLng newDestination = Objects.requireNonNull(distanceMap.get(1)).getNodeLatlng();
            LatLng newBegin = Objects.requireNonNull(distanceMap.get(0)).getNodeLatlng();

            polyPoints.clear();
            if (count == 1) {
                fencePoints.add(newBegin);
                fencePoints.add(newDestination);
                count++;
            }else if(count >1){
//                double distance = LineAndFence.pointToLine(selfLatLng, targetPosition, immediateLatLng);
//                //在地图上添加Marker，并显示
//                if(!selfLatLng.equals(new LatLng(latitude, longitude)))
//                    baiduMap.addOverlay(startMarker);
//                baiduMap.addOverlay(targetMarker);
//                baiduMap.addOverlay(polyLine);
//                Log.d(TAG, "run: Added " + count+" polyline from " + selfLatLng + " to " + targetPosition);
            LatLng foot = LineAndFence.getFoot(newBegin, newDestination, newSelfPos);
            ((MarkerOptions) footMarker).position(foot).icon(footIcon);
            //构造CircleOptions对象
            CircleOptions mCircleOptions = new CircleOptions().center(foot)
                    .radius(40)
                    .fillColor(0x4D0000FF)
                    .stroke(new Stroke(5, 0xAA00ff00))
                    .visible(false); //边框宽和边框颜色.fillColor(0xAA0000FF) //填充颜色


            //mCircleOptions.getStroke();
            //在地图上显示圆
            baiduMap.addOverlay(footMarker);
            if (count < 3)
                baiduMap.addOverlay(mCircleOptions);

            if (setTarget && count >= 2) {
                for (Integer nodeNum : mapShow.keySet()) {
                    if (nodeNum != SelfNumber) {
                        String Node = "Node" + nodeNum;
                        Cursor c = LitePal.findBySQL("select * from " + Node);
                        int number = c.getCount();
                        if (number != 0) {
                            List<? extends NodeMother> NodeInfors =
                                    LitePal.order("time desc").limit(1).find(mapShow.get(nodeNum).getClass());
                            LatLng target = new LatLng(NodeInfors.get(0).getLatitude(),
                                    NodeInfors.get(0).getLongitude());

                            LatLng startPoint = ArcInfo.setStartPoint(foot, target);
                            //Log.d(TAG, "startPoint: " + startPoint);
                            LatLng endPoint = ArcInfo.setEndPoint(foot, target);
                            //Log.d(TAG, "endPoint: " + endPoint);
                            double lat = (foot.latitude + target.latitude) / 2;
                            double lon = (foot.longitude + target.longitude) / 2;

                            LatLng middlePoint = new LatLng(lat, lon);
                            //Log.d(TAG, "middlePoint: " + middlePoint);
                            /*
                             * center 构成圆的中心点
                             * radius 圆的半径
                             * point  待判断点
                             */
                            if (!SpatialRelationUtil.isCircleContainsPoint(foot, 40, target)) {
                                polyPoints.add(startPoint);
                                polyPoints.add(middlePoint);
                                polyPoints.add(endPoint);
                            }
                        }
                    }
                    //else
                    //Log.d(TAG, "跳过自身");
                }
                LatLng targetStartPoint = ArcInfo.setStartPoint(foot, newBegin);
                //Log.d(TAG, "startPoint: " + targetStartPoint);
                LatLng targetEndPoint = ArcInfo.setEndPoint(foot, newDestination);
                //Log.d(TAG, "endPoint: " + targetEndPoint);
                double lat = (foot.latitude + newDestination.latitude) / 2;
                double lon = (foot.longitude + newDestination.longitude) / 2;
                lat = (foot.latitude + lat) / 2;
                lon = (foot.longitude + lon) / 2;
                LatLng targetMiddlePoint = new LatLng(lat, lon);
                //Log.d(TAG, "middlePoint: " + targetMiddlePoint);
                if (!SpatialRelationUtil.isCircleContainsPoint(foot, 40, targetMiddlePoint)) {
                    polyPoints.add(targetStartPoint);
                    polyPoints.add(targetEndPoint);
                    polyPoints.add(targetMiddlePoint);
                }

                LinkedHashSet<LatLng> hashSet = new LinkedHashSet<>(polyPoints);
                ArrayList<LatLng> listWithoutDuplicates = new ArrayList<>(hashSet);
                HashMap<Integer, ArrayList<Object>> mapAll = new HashMap<>();
                for (int i = 0; i < listWithoutDuplicates.size(); i++) {
                    //第一个放经纬度 第二个放角度
                    ArrayList<Object> objList = new ArrayList<>();
                    objList.add(listWithoutDuplicates.get(i));
                    objList.add(getAngle1(foot.latitude, foot.longitude,
                            listWithoutDuplicates.get(i).latitude, listWithoutDuplicates.get(i).longitude));
                    mapAll.put(i, objList);
                }

                ArrayList<Object> temp = new ArrayList<>();
                int size = mapAll.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - 1 - i; j++) {
                        if (Double.parseDouble(Objects.requireNonNull(mapAll.get(j)).get(1).toString()) >
                                Double.parseDouble(Objects.requireNonNull(mapAll.get(j + 1)).get(1).toString()))  //交换两数位置
                        {
                            temp = mapAll.get(j);
                            mapAll.put(j, mapAll.get(j + 1));
                            mapAll.put(j + 1, temp);
                        }
                    }
                }


                listWithoutDuplicates.clear();
                for (Integer integer : mapAll.keySet()) {
                    if (Objects.requireNonNull(mapAll.get(integer)).get(0) instanceof LatLng) {
                        listWithoutDuplicates.add((LatLng) Objects.requireNonNull(mapAll.get(integer)).get(0));
                    }
                }
                listWithoutDuplicates.add(newDestination);

                //构造PolygonOptions
                PolygonOptions mPolygonOptions = new PolygonOptions()
                        .points(listWithoutDuplicates)
                        .fillColor(0xAAFFFFAA) //填充颜色
                        .stroke(new Stroke(5, 0xAA00FF00)); //边框宽度和颜色

                //在地图上显示多边形
                mPolygon = (Polygon) baiduMap.addOverlay(mPolygonOptions);

                //判断点pt是否在位置点列表mPoints构成的多边形内。
                In_OR_OUT = SpatialRelationUtil.isPolygonContainsPoint(listWithoutDuplicates, newSelfPos);
            }


            if (!In_OR_OUT) {
                //开启震动
                isVirating = true;
                VirateUtil.vibrate(LocationActivity.this, new long[]{100, 200, 300, 300}, -1);
                Looper.prepare();
                Toast.makeText(LocationActivity.this, "请回到正确方向！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                if (isVirating) {
                    isVirating = false;
                    VirateUtil.virateCancle(LocationActivity.this);
                    Looper.prepare();
                    Toast.makeText(LocationActivity.this, "你正在正确方向上", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
            count++;
        }
        }



//        if(mRouteOverlay != null && setTarget){
//            polyPoints.clear();
//            if(count == 1 ){
//                listener = null;
//                selfLatLng = new LatLng(latitude,longitude);
//                fencePoints.add(selfLatLng);
//                fencePoints.add(targetPosition);
////                ((PolylineOptions) polyLine).width(15).customTexture(textureBlue).points(fencePoints);
////                //在地图上添加Marker，并显示
////                baiduMap.addOverlay(targetMarker);
////                baiduMap.addOverlay(polyLine);
//                Log.d(TAG, "run: Added polyline from " + selfLatLng + "to " + targetPosition);
//                count++;
//            }
//            else if(count>1) {
//                LatLng immediateLatLng = new LatLng(latitude, longitude);
////                double distance = LineAndFence.pointToLine(selfLatLng, targetPosition, immediateLatLng);
////                //在地图上添加Marker，并显示
////                if(!selfLatLng.equals(new LatLng(latitude, longitude)))
////                    baiduMap.addOverlay(startMarker);
////                baiduMap.addOverlay(targetMarker);
////                baiduMap.addOverlay(polyLine);
////                Log.d(TAG, "run: Added " + count+" polyline from " + selfLatLng + " to " + targetPosition);
//                LatLng foot = LineAndFence.getFoot(selfLatLng, targetPosition, immediateLatLng);
//                ((MarkerOptions) footMarker).position(foot).icon(footIcon);
//                //构造CircleOptions对象
//                CircleOptions mCircleOptions = new CircleOptions().center(foot)
//                        .radius(40)
//                        .fillColor(0x4D0000FF)
//                        .stroke(new Stroke(5, 0xAA00ff00))
//                        .visible(false); //边框宽和边框颜色.fillColor(0xAA0000FF) //填充颜色
//
//
//                //mCircleOptions.getStroke();
//                //在地图上显示圆
//                baiduMap.addOverlay(footMarker);
//                if(count < 3)
//                    baiduMap.addOverlay(mCircleOptions);
//
//                if(setTarget && count > 2){
//                    for (Integer nodeNum : mapShow.keySet()) {
//                        if(nodeNum != SelfNumber){
//                            String Node = "Node" + nodeNum;
//                            Cursor c = LitePal.findBySQL("select * from " + Node);
//                            int number = c.getCount();
//                            if(number != 0){
//                                List<? extends NodeMother> NodeInfors =
//                                        LitePal.order("time desc").limit(1).find(mapShow.get(nodeNum).getClass());
//                                LatLng target = new LatLng(NodeInfors.get(0).getLatitude(),
//                                        NodeInfors.get(0).getLongitude());
//
//                                LatLng startPoint = ArcInfo.setStartPoint(foot,target);
//                                //Log.d(TAG, "startPoint: " + startPoint);
//                                LatLng endPoint = ArcInfo.setEndPoint(foot,target);
//                                //Log.d(TAG, "endPoint: " + endPoint);
//                                double lat = (foot.latitude + target.latitude)/2;
//                                double lon = (foot.longitude + target.longitude)/2;
//
//                                LatLng middlePoint = new LatLng(lat, lon);
//                                //Log.d(TAG, "middlePoint: " + middlePoint);
//                                /*
//                                 * center 构成圆的中心点
//                                 * radius 圆的半径
//                                 * point  待判断点
//                                 */
//                                if(!SpatialRelationUtil.isCircleContainsPoint(foot,40,target)){
//                                    polyPoints.add(startPoint);
//                                    polyPoints.add(middlePoint);
//                                    polyPoints.add(endPoint);
//                                }
//                            }
//                        }
//                        //else
//                            //Log.d(TAG, "跳过自身");
//                    }
//                    LatLng targetStartPoint = ArcInfo.setStartPoint(foot,targetPosition);
//                    //Log.d(TAG, "startPoint: " + targetStartPoint);
//                    LatLng targetEndPoint = ArcInfo.setEndPoint(foot,targetPosition);
//                    //Log.d(TAG, "endPoint: " + targetEndPoint);
//                    double lat = (foot.latitude + targetPosition.latitude)/2;
//                    double lon = (foot.longitude + targetPosition.longitude)/2;
//                    LatLng targetMiddlePoint = new LatLng(lat, lon);
//                    //Log.d(TAG, "middlePoint: " + targetMiddlePoint);
//                    if(!SpatialRelationUtil.isCircleContainsPoint(foot,40,targetMiddlePoint)){
//                        polyPoints.add(targetStartPoint);
//                        polyPoints.add(targetEndPoint);
//                        polyPoints.add(targetMiddlePoint);
//                    }
//
//                    LinkedHashSet<LatLng> hashSet = new LinkedHashSet<>(polyPoints);
//                    ArrayList<LatLng> listWithoutDuplicates = new ArrayList<>(hashSet);
//                    HashMap<Integer, ArrayList<Object>> mapAll = new HashMap<>();
//                    for (int i = 0; i < listWithoutDuplicates.size(); i++) {
//                        //第一个放经纬度 第二个放角度
//                        ArrayList<Object> objList = new ArrayList<>();
//                        objList.add(listWithoutDuplicates.get(i));
//                        objList.add(getAngle1(foot.latitude, foot.longitude,
//                                listWithoutDuplicates.get(i).latitude, listWithoutDuplicates.get(i).longitude));
//                        mapAll.put(i, objList);
//                    }
//
//                    ArrayList<Object> temp = new ArrayList<>();
//                    int size = mapAll.size();
//                    for (int i = 0; i < size - 1; i++) {
//                        for (int j = 0; j < size - 1 - i; j++) {
//                            if (Double.parseDouble(Objects.requireNonNull(mapAll.get(j)).get(1).toString()) >
//                                    Double.parseDouble(Objects.requireNonNull(mapAll.get(j + 1)).get(1).toString()))  //交换两数位置
//                            {
//                                temp = mapAll.get(j);
//                                mapAll.put(j, mapAll.get(j + 1));
//                                mapAll.put(j + 1, temp);
//                            }
//                        }
//                    }
//
//                    listWithoutDuplicates.clear();
//                    for (Integer integer : mapAll.keySet()) {
//                        if (Objects.requireNonNull(mapAll.get(integer)).get(0) instanceof LatLng) {
//                            listWithoutDuplicates.add((LatLng) Objects.requireNonNull(mapAll.get(integer)).get(0));
//                        }
//                    }
//
//                    //构造PolygonOptions
//                    PolygonOptions mPolygonOptions = new PolygonOptions()
//                            .points(listWithoutDuplicates)
//                            .fillColor(0xAAFFFF00) //填充颜色
//                            .stroke(new Stroke(5, 0xAA00FF00)); //边框宽度和颜色
//
//                    //在地图上显示多边形
//                    baiduMap.addOverlay(mPolygonOptions);
//                    //判断点pt是否在位置点列表mPoints构成的多边形内。
//                    In_OR_OUT = SpatialRelationUtil.isPolygonContainsPoint(listWithoutDuplicates,selfLatLng);
//                }
//
//
//                if(!In_OR_OUT){
//                    //开启震动
//                    isVirating = true;
//                    VirateUtil.vibrate(LocationActivity.this,new long[]{100, 200, 300, 300},-1);
//                    Looper.prepare();
//                    Toast.makeText(LocationActivity.this,"请回到正确方向！",Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }else {
//                    if (isVirating) {
//                        isVirating = false;
//                        VirateUtil.virateCancle(LocationActivity.this);
//                        Looper.prepare();
//                        Toast.makeText(LocationActivity.this,"你正在正确方向上",Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                }
//                count++;
//            }
//        }
    }

    public Timer timerSycLoc = new Timer();
    public TimerTask LocationSycTask = new TimerTask() {
        String lastRecordResult = null;
        int recordCount = 0;
        @Override
        public void run() {
            //String receivedInfo = getIntent().getStringExtra("recordMessage");//接收BaseActivity传递过来的数据
            if (CONNECT_STATUS) {
                String localizeData = "#" + SelfNumber + "," + latitude + "," + longitude + "," + "\n";
                Log.d(TAG, "run: localizeData is:" + localizeData);
                localizeData = localizeData.replace("\\n","\n");
                localizeBuffer = localizeData.getBytes();
                try {

                    mmOutStream = mmSocket.getOutputStream();
                    mmOutStream.write(localizeBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            if((warnTypes != 0 || hasBeenBackToNormal )&& warnTypes != EDGE_WARNING ){
                //new Thread(new Thread()).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //int num = warnTypes;
                            Thread.sleep(3000);//延时3s
                            // storage: && num != 0
                            if (CONNECT_STATUS ) {
                                Log.d(TAG, "warn " + warnTypes);
                                String WarningMessage = "!"+ "," + "sos" + warnTypes + "," + "\n";
                                Log.d("SOS TYPE :", WarningMessage);
                                WarningMessage = WarningMessage.replace("\\n","\n");
                                warningBuffer = WarningMessage.getBytes();
                                if(hasBeenBackToNormal)
                                    hasBeenBackToNormal = false;
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

            if((recordResult != null )&& recordResult.equals(lastRecordResult)){
                recordCount++;
            }else{
                recordCount = 0;
            }
            if(recordCount >= 4){
                recordResult = null;
                recordCount = 0;
            }

            if(recordResult != null){
                //new Thread(new Thread()).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500);//延时4s
                            if (CONNECT_STATUS ) {
                                Log.d(TAG, "prepare message " + recordResult);
                                String RecordMessage = "%"+ SelfNumber +"," + recordResult + "," + "\n";
                                RecordMessage = RecordMessage.replace("\\n","\n");
                                recordBuffer = RecordMessage.getBytes();
                                try {
                                    mmOutStream = mmSocket.getOutputStream();
                                    mmOutStream.write(recordBuffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                lastRecordResult = recordResult;

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            if(SETTING_CHANGED){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6000);//延时6s
                            if (CONNECT_STATUS ) {
                                loraSettingBuffer = loraSetting.getBytes();
                                try {
                                    mmOutStream = mmSocket.getOutputStream();
                                    mmOutStream.write(loraSettingBuffer);
                                    SETTING_CHANGED = false;
                                    //bluetoothSetting = null;
                                    loraSetting = null;
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
            if(NAME_CHANGED){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6500);//延时6.5s
                            if (CONNECT_STATUS ) {
                                btSettingBuffer = bluetoothSetting.getBytes();
                                try {
                                    mmOutStream = mmSocket.getOutputStream();
                                    mmOutStream.write(btSettingBuffer);
                                    NAME_CHANGED = false;
                                    bluetoothSetting = null;
                                    //loraSetting = null;
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

        option.setLocationNotify(false);
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
        // 释放检索对象
        if (mSearch != null) {
            mSearch.destroy();
        }
    }

}
