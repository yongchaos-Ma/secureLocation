package com.example.myapplication;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.example.myapplication.BlueToothListActivity.EXTRA_DEVICE_ADDRESS;
import static com.example.myapplication.GetTime.dateToStamp;
import static com.example.myapplication.GetTime.format;
import static com.example.myapplication.GetTime.getCurrentTime;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
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
import com.example.myapplication.power_individual_demo.BleCmd06_getData;
import com.example.myapplication.power_individual_demo.BleCmd09_getAllData;
import com.example.myapplication.power_individual_demo.BleCmd20_setTime;
import com.example.myapplication.power_individual_demo.BleNotifyParse;
import com.example.myapplication.power_individual_demo.Config;
import com.example.myapplication.power_individual_demo.DeviceListActivity;
import com.example.myapplication.power_individual_demo.UartService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.iflytek.cloud.SpeechUtility;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class BaseActivity extends AppCompatActivity {

    public MapView mMapView;
    public BaiduMap baiduMap;
    protected MapStatusUpdate mapStatusUpdate;
    private DrawerLayout mDrawerLayout;
    //测试项目坐标
    //public final LatLng xupt = new LatLng(34.1606259200,108.9074823500);
    //GPS信息
    public static double latitude = 0.0;    //获取纬度信息
    public static double longitude = 0.0;    //获取经度信息
    public double accuracy = 0.0;
    public double direction = 0.0;
    //按键
    public static LatLng newDestination;
    private ImageButton menuButton;
    private SubActionButton buttonFir;
    private SubActionButton buttonSec;
    private SubActionButton buttonThr;
    private SubActionButton buttonFou;
    private TextView Tv5;
    private TextView Tv6;
    private TextView Tv7;
    private NavigationView navView;
    private View headview;
    private CircleImageView headImage;
    //public FloatingActionButton fab;
    FloatingActionButton floatingActionButton1;
    FloatingActionButton floatingActionButton2;

    //蓝牙菜单
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public  final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public  BluetoothSocket mmSocket;
    public  OutputStream mmOutStream;
    public  InputStream mmInStream;

    public static boolean CONNECT_STATUS = false;
    public static boolean BAND_CONNECTED = false;
    public static boolean CAN_NOTIFY = false;
    public boolean HR_DETECTED = false;
    public boolean BLE_STOPPED = false;
    public boolean EXTEND_LEGACY = false;
    public static BluetoothAdapter mBtAdapter;

    public List<LatLng> points = new ArrayList<>();
    public static int SelfNumber = 0;
    public int WarnOthers = 1;
    public int EDGE_WARNING = 2;
    public int HR_Warning = 3;
    public static boolean GET_WARNED = false;
    public boolean SELFNUMBER_SETTLED = false;
    public int TotalNumber = 0;
    public int NeighborNumber = 0;
    public int UnconnectableNumber = 0;
    public int warnTypes = 0;
    public static int getWarnedTypes = 0;
    public boolean BE_WARNED = false;
    public boolean hasBeenBackToNormal = true;
    public boolean LOAD_COMPLETED = false;

    public PendingIntent pendingIntent;
    NotificationChannel mChannel;
    public static final int NotificationId1 = 1;
    public static final int NotificationId2 = 2;
    public static final int NotificationId3 = 3;
    public static final int NotificationId4 = 4;

    public static String readStr ;

    public boolean setTarget = false;
    public static Handler handler = null;

    Config hr_config;
    int time_flag = 0;
    final int intf_ble_uart = 1;
    int intf = intf_ble_uart;
    private static final int REQUEST_SELECT_DEVICE_BAND = 101;
    private static final int REQUEST_ENABLE_BT_BAND = 102;
    public static final String TAG = "nRFUART";
    private UartService mUartService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBandBtAdapter = null;
    BleNotifyParse bleNotify = new BleNotifyParse();
    private final byte[] tx_data = new byte[512];
    private int tx_data_len = 0;
    private int tx_data_front = 0;
    private int tx_data_rear = 0;
    public int danger_HR = 0;
    String cutted  = "0";
    int ScanPeriod = 10000;
    public int trackSlot = 0;
    final int[] p = {1};
    final int[] q = {1};

    public LocalBroadcastManager mLocalBroadcastManager;
    //private WarningBroadcastReceiver mWarningBroadcastReceiver;
    private LostBroadcastReceiver mLostBroadcastReceiver;
    private SelfWarningBroadcastReceiver mSWBroadcastReceiver;

    private final String recordMessage = null;

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LitePal.initialize(this);
        SpeechUtility.createUtility(this,  "appid=74285e8e" );

        makeStatusBarTransparent();
        setContentView(R.layout.for_fun);
        Tv5 = findViewById(R.id.Tv_node_num_num);
        Tv7 = findViewById(R.id.Tv_lost_num_num);
        Tv6 = findViewById(R.id.Tv_hr_num_num);

        mDrawerLayout = findViewById(R.id.fun_layout);
        navView = findViewById(R.id.nav_view_new);
        headview = navView.inflateHeaderView(R.layout.nav_header);
        headImage = headview.findViewById(R.id.icon_image);
        menuButton = findViewById(R.id.drawer_open);

        mMapView = findViewById(R.id.bmapView);
        //获得地图控制器
        baiduMap = mMapView.getMap();
        //MKOfflineMap mOffline = new MKOfflineMap();
        /*隐藏比例按钮，默认是显示的
            mMapView.showScaleControl(false);
          隐藏缩放按钮，默认是显示的
            mMapView.showZoomControls(false);
         */
        //设置中心点.MapStatusUpdate放入setMapStatus方法中用来描述即将的变化
        mMapView.getChildAt(2).setPadding(0,0,0,610);//这是控制缩放控件的位置
        mMapView.getChildAt(1).setPadding(0,0,0,165);//这是控制图标控件的位置
        //设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17);
        baiduMap.setMapStatus(mapStatusUpdate);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);


        handler=new Handler();

        hr_config = new Config(BaseActivity.this);

        mBandBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBandBtAdapter == null || mBtAdapter == null) {
            Toast.makeText(this, "蓝牙是不可用的",
                    Toast.LENGTH_LONG).show();
        }

        if(hr_config.isValid())
        {
            if (!mBandBtAdapter.isEnabled())
            { mBandBtAdapter.enable(); }
        }
        service_init();
        if(hr_config.isValid())
        {
            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(hr_config.getAddr());
            Log.d(TAG, "... onActivityResultdevice.address==" + mDevice
                    + "mserviceValue" + mUartService);
        }

        init();
        BroadcastInitial();

        UiInitial();
        requestMyPermissions();
        LitePal.getDatabase();
        EventBus.getDefault().register(this);
        //mSharedPreferences = getSharedPreferences(LoraSetting.PREFER_NAME, Activity.MODE_PRIVATE);

        timerFlash.schedule(timerFlashTask,0,8000);//刷新网络
    }
//    private void requestMyPermissions() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有蓝牙权限");
//        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//            //没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.INTERNET}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有网络权限");
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有定位权限");
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有精准定位权限");
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有录音权限");
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有改变网络状态权限");
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED)
//        {//没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
//        } else {
//            Log.d(TAG, "requestMyPermissions: 有读取手机状态权限");
//        }
//    }

    private void requestMyPermissions() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 100);
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.INTERNET}, 100);
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 100);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            Log.d(TAG, "requestMyPermissions: 权限已全部获取");
        }
    }

    private void BroadcastInitial(){
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("com.example.myapplication.LOST_BROADCAST");
        intentFilter.addAction("com.example.myapplication.SELF_WARN_BROADCAST");
        intentFilter.addAction("com.example.myapplication.WARNING_BROADCAST");
        intentFilter.addAction("com.example.myapplication.PEACE_BROADCAST");
        mLostBroadcastReceiver = new LostBroadcastReceiver();
        mSWBroadcastReceiver = new SelfWarningBroadcastReceiver();
        //mWarningBroadcastReceiver = new WarningBroadcastReceiver();
        PeaceBroadcastReceiver peaceBroadcastReceiver = new PeaceBroadcastReceiver();

        mLocalBroadcastManager.registerReceiver(mLostBroadcastReceiver, intentFilter);
        mLocalBroadcastManager.registerReceiver(mSWBroadcastReceiver, intentFilter);
        //mLocalBroadcastManager.registerReceiver(mWarningBroadcastReceiver, intentFilter);
        mLocalBroadcastManager.registerReceiver(peaceBroadcastReceiver, intentFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void UiInitial(){
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: headImage");
                setNumber();
                headImage.setClickable(false);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onItemPlusSelected(item);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        menuButton.setOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));

        ImageButton baseButton = findViewById(R.id.plus_setting);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(160, 160, 17);
        //params.setMargins(5,5,5,5);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(BaseActivity.this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(BaseActivity.this);
        itemIcon1.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.alarm));
        buttonFir = itemBuilder
                .setContentView(itemIcon1)
                .setLayoutParams(params)
                //.setBackgroundDrawable(ContextCompat.getDrawable(BaseActivity.this,R.drawable.small_circle_button) )
                .build();

        ImageView itemIcon2 = new ImageView(BaseActivity.this);
        itemIcon2.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.microphone));
        buttonSec = itemBuilder
                .setContentView(itemIcon2)
                //.setBackgroundDrawable(ContextCompat.getDrawable(BaseActivity.this,R.drawable.small_circle_button) )
                .build();

        ImageView itemIcon3 = new ImageView(BaseActivity.this);
        itemIcon3.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.clear));
        buttonThr = itemBuilder
                .setContentView(itemIcon3)
                //.setBackgroundDrawable(ContextCompat.getDrawable(BaseActivity.this,R.drawable.small_circle_button) )
                .build();

        ImageView itemIcon4 = new ImageView(BaseActivity.this);
        itemIcon4.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.bluetooth));
        buttonFou = itemBuilder
                .setContentView(itemIcon4)
                //.setBackgroundDrawable(ContextCompat.getDrawable(BaseActivity.this,R.drawable.small_circle_button) )
                .build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(BaseActivity.this)
                .addSubActionView(buttonFir)
                .addSubActionView(buttonSec)
                .addSubActionView(buttonThr)
                .addSubActionView(buttonFou)
                .setStartAngle(250)
                .setEndAngle(120)
                .setRadius(240)
                .attachTo(baseButton)
                .build();

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                baseButton.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(
                        View.ROTATION, 45);

                ObjectAnimator animation = ObjectAnimator
                        .ofPropertyValuesHolder(baseButton, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                baseButton.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(
                        View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator
                        .ofPropertyValuesHolder(baseButton, pvhR);
                animation.start();
            }
        });

        buttonFir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOdd(p[0])){
                    //向其他成员发送急救信息
                    warnTypes = WarnOthers;
                    Intent intent = new Intent("com.example.myapplication.SELF_WARN_BROADCAST");
                    intent.setPackage("com.example.myapplication");
                    intent.setComponent(new ComponentName(BaseActivity.this, SelfWarningBroadcastReceiver.class));
                    mLocalBroadcastManager.sendBroadcast(intent);
                    Toast.makeText(BaseActivity.this,"sos",Toast.LENGTH_LONG).show();
                    //toolbar.setBackgroundColor(getResources().getColor(R.color.lightgreen));
                    //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(red)));
                    BE_WARNED = true;
                }else {
                    //停止呼救
                    warnTypes = 0;
                    //toolbar.setBackgroundColor(getResources().getColor(dodgerblue));
                    //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(orange)));
                    BE_WARNED = false;
                }
                p[0]++;

            }
        });
        buttonSec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RecognizeModule().startNoDialogOffline(BaseActivity.this);
                //startNoDialogOffline(BaseActivity.this);
            }
        });
        final int[] i = {1};
        buttonFou.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOdd(i[0])){
                    onBluetoothAction();
                }else {
                    onBluetoothDisconnect();
                }
                i[0]++;
            }
        });

        Tv6.setGravity(Gravity.START);

        mDrawerLayout.openDrawer(GravityCompat.START);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if(!EXTEND_LEGACY){
                    loadChoice();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });



        SharedPreferences setting = getSharedPreferences("com.ms.app", 0);
        boolean first = setting.getBoolean("FIRST", true);
        if (first) {// 第一次则跳转到注册页面
            setting.edit().putBoolean("FIRST", false).apply();
            guideView();
        }

    }

    private void guideView(){

        WindowManager mWindowManager = getWindowManager();
        final int[] i = {0};
        LayoutInflater layoutInflater = getLayoutInflater();
        final View guideView = layoutInflater.inflate(R.layout.guide, null);
        final ImageView img = guideView.findViewById(R.id.img);
        RelativeLayout swtich = guideView.findViewById(R.id.swtich);


        swtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (i[0]) {
                    case 0:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        img.setBackgroundResource(R.drawable.guide4);
                        i[0]++;
                        break;
                    case 1:
                        i[0] = 0;
                        mWindowManager.removeView(guideView);
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;
                }
            }
        });
        //设置参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.CENTER;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels;         // 屏幕宽度（像素）
        params.height = dm.heightPixels;       // 屏幕高度（像素）
        mWindowManager.addView(guideView, params);
    }


    public void makeStatusBarTransparent() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int option = window.getDecorView().getSystemUiVisibility() | SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(option);
        window.setStatusBarColor(Color.TRANSPARENT);
        changeStatusBarTextColor(true);
    }
    private void changeStatusBarTextColor(boolean isBlack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            }else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
    }


    private void loadChoice(){
        //测试按键
        AlertDialog.Builder loadBuilder = new AlertDialog.Builder(BaseActivity.this);
        loadBuilder.setTitle("检测到存档，是否继承上一次的数据？")
                .setIcon(R.drawable.ic_attention)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChooseNewStorageData();
                        dialog.dismiss();
                    }
                });
        loadBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EXTEND_LEGACY = true;
                loadingClick();
            }
        });
        loadBuilder.show();
    }
    private void ChooseNewStorageData(){
        //测试按键
        AlertDialog.Builder newStorageBuilder = new AlertDialog.Builder(BaseActivity.this);
        newStorageBuilder.setTitle("选择不继承将直接删除之前存储的数据，是否继续？")
                .setIcon(R.drawable.ic_attention)
                .setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadChoice();
                    }
                });
        newStorageBuilder.setPositiveButton("我确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadingClick();
                LitePal.deleteAll(SelfLocationDatabase.class);
                LitePal.deleteAll(Node1.class);
                LitePal.deleteAll(Node2.class);
                LitePal.deleteAll(Node3.class);
                LitePal.deleteAll(Node4.class);
                LitePal.deleteAll(Node5.class);
                LitePal.deleteAll(Node6.class);
                LitePal.deleteAll(Node7.class);
                LitePal.deleteAll(Node8.class);
                LitePal.deleteAll(Node9.class);
                LitePal.deleteAll(Node10.class);
                LitePal.deleteAll(Node11.class);
                LitePal.deleteAll(Node12.class);
                LitePal.deleteAll(Node13.class);
                LitePal.deleteAll(Node14.class);
                LitePal.deleteAll(Node15.class);
                LitePal.deleteAll(Node16.class);
                LitePal.deleteAll(Node17.class);
                LitePal.deleteAll(Node18.class);
                LitePal.deleteAll(Node19.class);
                LitePal.deleteAll(Node20.class);
            }
        });
        newStorageBuilder.show();
    }

    private void loadingClick(){
        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.waiting_activity,null);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);

        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                },3000
        );
    }


    private void onItemPlusSelected(MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.button_offline_map) {// 求救
            startActivity(new Intent(this, OfflineActivity.class));
        } else if (itemId == R.id.button_trace) {// 轨迹
            getCurrentTime();
            TrackdialogChoice();
        } else if (itemId == R.id.button_num) {// 编号
            //测试按键
            final EditText inputServer = new EditText(BaseActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle("输入本机序号(1~20)")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(inputServer)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String text = inputServer.getText().toString();
                    int buffer;
                    if (checkStrIsNum02(text)) {
                        buffer = Integer.parseInt(text);
                        if (buffer > 0 && buffer <= 20) {
                            SELFNUMBER_SETTLED = true;
                            SelfNumber = buffer;
                        } else {
                            Toast.makeText(BaseActivity.this, "请输入1到20之间的数字！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BaseActivity.this, "请输入数字！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        } else if (itemId == R.id.button_heart_rate) {// 心率
            if (!BAND_CONNECTED) {
                Intent newIntent = new Intent(BaseActivity.this, DeviceListActivity.class);
                startActivityForResult(newIntent, REQUEST_SELECT_DEVICE_BAND);
            } else {
                BleCmd06_getData getData = new BleCmd06_getData();
                setTx_data(getData.onHR());
                if (timerStartHR != null || timerStartHRTask != null)
                    stopTimer();
                //startTimer();
                //timerStartHR.schedule(timerStartHRTask,0,5000);//心跳检测开启
                //button4.setClickable(false);
                dialogChoice();
            }
        } else if(itemId == R.id.ic_fence){//围栏
            setTarget = isOdd(q[0]);
            q[0]++;

        } else if(itemId == R.id.route_search){//路线搜索
            EventBus.getDefault().post(new BaseActivity.routeSearchEvent(true));

        } else if(itemId == R.id.hw_setting){
            Intent intent = new Intent(BaseActivity.this, LoraSetting.class);
            startActivity(intent);
        }

    }

    public void onBluetoothAction(){
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(BaseActivity.this, "蓝牙已打开", Toast.LENGTH_SHORT)
                    .show();
            if (!SELFNUMBER_SETTLED) {
                Toast.makeText(BaseActivity.this, "请先设置本机编号！", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Intent serverIntent = new Intent(BaseActivity.this, BlueToothListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        }
    }
    public void onBluetoothDisconnect(){
        if (!CONNECT_STATUS && !BAND_CONNECTED) {
            Toast.makeText(BaseActivity.this, "无连接", Toast.LENGTH_SHORT)
                    .show();
        } else if (CONNECT_STATUS && BAND_CONNECTED) {
            if (intf == intf_ble_uart) {
                mUartService.disconnect();
                mUartService.close();
                stopTimer();
            }
            cancelconnect();
            Toast.makeText(BaseActivity.this, "已断开连接", Toast.LENGTH_SHORT)
                    .show();
        } else if (CONNECT_STATUS) {
            cancelconnect();
            Toast.makeText(BaseActivity.this, "已断开节点连接", Toast.LENGTH_SHORT)
                    .show();
        } else if (BAND_CONNECTED) {
            //if (intf == intf_ble_uart) {
                //hr_config.clear_config();
                mUartService.disconnect();
                mUartService.close();
                stopTimer();
                Toast.makeText(BaseActivity.this, "已断开手环连接", Toast.LENGTH_SHORT)
                        .show();
            //}

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.open) {// 打开蓝牙设备
            if (!mBtAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                Toast.makeText(BaseActivity.this, "蓝牙已打开", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (itemId == R.id.scan) {// 扫描设备
            if (!mBtAdapter.isEnabled()) {
                Toast.makeText(BaseActivity.this, "未打开蓝牙", Toast.LENGTH_SHORT)
                        .show();
            } else if (!SELFNUMBER_SETTLED) {
                Toast.makeText(BaseActivity.this, "请先设置本机编号！", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Intent serverIntent = new Intent(BaseActivity.this, BlueToothListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            }
        } else if (itemId == R.id.disconnect) {// 断开连接
            if (!CONNECT_STATUS && !BAND_CONNECTED) {
                Toast.makeText(BaseActivity.this, "无连接", Toast.LENGTH_SHORT)
                        .show();
            } else if (CONNECT_STATUS && BAND_CONNECTED) {
                if (intf == intf_ble_uart) {
                    mUartService.disconnect();
                    mUartService.close();
                    stopTimer();
                }
                cancelconnect();
                Toast.makeText(BaseActivity.this, "已断开连接", Toast.LENGTH_SHORT)
                        .show();
            } else if (CONNECT_STATUS) {
                cancelconnect();
                Toast.makeText(BaseActivity.this, "已断开节点连接", Toast.LENGTH_SHORT)
                        .show();
            } else if (BAND_CONNECTED) {
                if (intf == intf_ble_uart) {
                    //hr_config.clear_config();
                    mUartService.disconnect();
                    mUartService.close();
                    stopTimer();
                }
                Toast.makeText(BaseActivity.this, "已断开手环连接", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (itemId == R.id.cleandata) {// 清除异常数据
            Snackbar.make(this.mMapView, "是否需要删除错误的历史数据", Snackbar.LENGTH_SHORT)
                    .setAction("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LitePal.deleteAll(SelfLocationDatabase.class, "latitude < ? and longitude < ?", "1", "1");
                            Toast.makeText(BaseActivity.this, "错误数据已清理", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).show();
        } else if (itemId == R.id.lorasetting) {
            Intent intent = new Intent(BaseActivity.this, LoraSetting.class);
            startActivity(intent);

        } else if (itemId == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return true;
    }

    private void setNumber(){
        //测试按键
        final EditText inputServer = new EditText(BaseActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("输入本机序号(1~20)")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String text = inputServer.getText().toString();
                int buffer = 0;
                if(checkStrIsNum02(text)){
                    buffer = Integer.parseInt(text);
                    if(buffer > 0 && buffer <= 20){
                        SELFNUMBER_SETTLED = true;
                        SelfNumber = buffer;
                        TextView selfNum = findViewById(R.id.num_and_class);
                        TextView selfName = findViewById(R.id.username);
                        selfNum.setText("自身编号: " + SelfNumber);
                        selfName.setText("测试人员" + SelfNumber);
                        headImage.setImageResource(R.drawable.head_new);
                        //TestNotification();

                    }else {
                        Toast.makeText(BaseActivity.this, "请输入1到20之间的数字！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(BaseActivity.this, "请输入数字！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void startTimer(){
        if (timerStartHR == null) {
            timerStartHR = new Timer();
        }

        if (timerStartHRTask == null) {
            timerStartHRTask = new TimerTask() {
                @Override
                public void run() {
                    BLE_STOPPED=false;
                    int tens = 0;
                    int warn_count = 10;
//                    BleCmd06_getData getData = new BleCmd06_getData();
//                    setTx_data(getData.onHR());
                    BleCmd09_getAllData getAllData = new BleCmd09_getAllData();
                    setTx_data(getAllData.getAll());
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(2000);//休眠2秒
                                BleCmd09_getAllData getAllData = new BleCmd09_getAllData();
                                setTx_data(getAllData.getAll());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    if(BleNotifyParse.received != null){
                        if(BleNotifyParse.received.startsWith("86", 2) && BleNotifyParse.received.startsWith("00", 8)){
                            cutted = BleNotifyParse.received.substring(10,12);
                            tens =  Integer.parseInt(cutted, 16);
                            cutted = String.valueOf(tens);
                            HR_DETECTED = true;
                                handler.post(runnableUiTv6);
                        }
                        if(Integer.parseInt(cutted) <40 && BAND_CONNECTED){
                            if(danger_HR >= 2){
                                warnTypes = HR_Warning;
                                Intent intent = new Intent("com.example.myapplication.SELF_WARN_BROADCAST");
                                intent.setPackage("com.example.myapplication");
                                intent.setComponent(new ComponentName(BaseActivity.this, SelfWarningBroadcastReceiver.class));
                                mLocalBroadcastManager.sendBroadcast(intent);
                                //toolbar.setBackgroundColor(getResources().getColor(R.color.materialComplementary));
                                sendNotificationSelf();
                                BE_WARNED = true;
                                hasBeenBackToNormal = false;
                            }
                            if(danger_HR <= 5)
                                danger_HR++;
                        }else if(BAND_CONNECTED && Integer.parseInt(cutted) >= 40 && danger_HR > 0 && warnTypes != 1){
                            danger_HR--;
                            warnTypes = 0;
                            BE_WARNED = false;
                            hasBeenBackToNormal = true;
                        }

                    }
                }
            };
        }

        if(timerStartHR != null && timerStartHRTask != null )
            timerStartHR.schedule(timerStartHRTask, 0, ScanPeriod);

    }
    /**
     * 单选
     */
    private void dialogChoice() {

        final String[] items = {"10秒", "30秒", "1分钟","5分钟"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("设置心率监测间隔");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, 0,
                (dialog, which) -> {
                    switch (items[which]){
                        case "10秒":
                          ScanPeriod = 10000;
                          break;
                        case "30秒":
                            ScanPeriod = 30000;
                            break;
                        case "1分钟":
                            ScanPeriod = 60000;
                            break;
                        case "5分钟":
                            ScanPeriod = 300000;
                            break;
                    }

                    Toast.makeText(BaseActivity.this, "监测间隔设置为：" + items[which],
                            Toast.LENGTH_SHORT).show();
                });
        builder.setPositiveButton("确定", (dialog, which) -> {
            startTimer();
            dialog.dismiss();
            Toast.makeText(BaseActivity.this, "确定", Toast.LENGTH_SHORT).show();
        });
        builder.create().show();
        //startTimer();

    }

    /**
     * 单选
     */
    private void TrackdialogChoice() {
        AlertDialog.Builder builder;
        final String[] items = {"一小时", "五小时", "十二小时","一天","全部"};
        builder = new AlertDialog.Builder(this);
        builder = builder.setIcon(R.drawable.ic_trace)
                .setTitle("设置路径复现范围")
        .setSingleChoiceItems(items, 0,
                (dialog, which) -> {
                    switch (items[which]){
                        case "一小时":
                            trackShowup(3600);
                            break;
                        case "五小时":
                            trackShowup(18000);
                            break;
                        case "十二小时":
                            trackShowup(43200);
                            break;
                        case "一天":
                            trackShowup(86400);
                            break;
                        case "全部":
                            trackShowup(90000000);
                            break;
                    }
                });
        builder.setPositiveButton("确定", (dialog, which) -> {
                        dialog.dismiss();
            Log.d(TAG, "TrackdialogChoice: "+trackSlot);
            //startActivity(new Intent(this,TextOverlayActivity.class));
                    });
        builder.create().show();
    }

    public void trackShowup(int trackSlot) {
        getCurrentTime();
        int slots = 0;
        points.clear();
        try {
             slots = Integer.parseInt(dateToStamp(format)) - trackSlot -1;
            Log.d(TAG, "trackShowup: "+ slots);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<SelfLocationDatabase> selfLocationDatabaseDatas =
                LitePal.order("time desc")
                .where("time > ?", String.valueOf(slots))
                .find(SelfLocationDatabase.class);

        for (SelfLocationDatabase selfLocationDatabase : selfLocationDatabaseDatas){
            LatLng point = new LatLng(selfLocationDatabase.getLatitude(), selfLocationDatabase.getLongitude());
            points.add(point);
        }

        //return trackSlot;
    }

    Runnable runnableUiTv6 = new Runnable(){
        @Override
        public void run() {
            //更新界面
            if(setTarget){
                        View paintTry = new PaintOnMap(BaseActivity.this);
                        mMapView.addView(paintTry);

            }
            if(!BLE_STOPPED){
                if(HR_DETECTED){
                    //Tv6.setText("当前心率：" + cutted +"bpm");
                    Tv6.setText(cutted);
                }
                //else Tv6.setText("暂无数据");
            }
//            else if(BAND_CONNECTED){
//                Tv6.setText("手环已连接");
//            }
//            else{
//                Tv6.setText("手环连接已断开");
//            }
            Tv6.setGravity(Gravity.START);
        }
    };
    private void stopTimer(){
        if (timerStartHR != null) {
            timerStartHR.cancel();
            timerStartHR = null;
        }
        if (timerStartHRTask != null) {
            timerStartHRTask.cancel();
            timerStartHRTask = null;
        }
        BLE_STOPPED = true;
//        Tv6.setText("手环连接已断开");
//        Tv6.setGravity(Gravity.CENTER);
        handler.post(runnableUiTv6);
    }

    public Timer timerStartHR;
    public TimerTask timerStartHRTask;

    private final Handler HRhandler = new Handler();
    private final Runnable HRrunnable = new Runnable() {
        public void run() {
            int len;
            if(tx_data_len > 0)
            {
                len = Math.min(tx_data_len, 20);

                byte[] send_buf = new byte[len];
                for(int i = 0; i < len; i++)
                {
                    send_buf[i] = tx_data[tx_data_rear];
                    tx_data_rear = (tx_data_rear + 1) % 512;
                }

                if(mUartService != null)
                {
                    mUartService.writeRXCharacteristic(send_buf);
                }
                tx_data_len = tx_data_len - len;
            }

            if(tx_data_len > 0) {
                HRhandler.postDelayed(this, 200);
            }
            else {
                time_flag = 0;
            }
        }
    };

    public void setTx_data(byte[] tx_data) {
        if(tx_data == null || mUartService == null || !mUartService.isConnected())
        {
            return;
        }

        //int len = tx_data.length;

        for (byte tx_datum : tx_data) {
            if (tx_data_len >= 512) {
                tx_data_rear = (tx_data_rear + 1) % 512;
                tx_data_len--;
            }
            this.tx_data[tx_data_front] = tx_datum;
            tx_data_front = (tx_data_front + 1) % 512;
            tx_data_len++;
        }
        if(time_flag == 0) {
            HRhandler.postDelayed(HRrunnable, 200);
            time_flag = 1;
        }
    }

    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT);
        return intentFilter;
    }

    // UART service connected/disconnected
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            if (intf == intf_ble_uart) {
                mUartService = ((UartService.LocalBinder) rawBinder)
                        .getService();
                Log.d(TAG, "onServiceConnected mService= " + mUartService);
                if (!mUartService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                }
                else {
                    if(hr_config.isValid()) {
                        if(mBandBtAdapter.isEnabled())
                        {
                            mUartService.connect(hr_config.getAddr());
                            BAND_CONNECTED = true;

                        }
                    }
                }
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            if (intf == intf_ble_uart) {
                mUartService = null;
            }
        }
    };
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // *********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_CONNECT_MSG");
                        if(!hr_config.isValid())
                        {
                            hr_config.save_config(mDevice.getName(), mDevice.getAddress());
                        }

                        BleCmd20_setTime bleCmd20 = new BleCmd20_setTime();
                        try {
                            setTx_data(bleCmd20.getAllData());//手机端发送请求数据格式
                            Log.d(TAG,"发送时间同步请求");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //接收手环回复的数据格式解析
                        if(BleNotifyParse.received != null){
                            Log.d(TAG,"接收时间同步请求");
                            Log.d(TAG,BleNotifyParse.received);
                            if(BleNotifyParse.received.startsWith("A0", 2)&& BleNotifyParse.received.startsWith("08", 8)){
                                Log.d(TAG,"时间已同步");
                            }
                        }
                    }
                });
            }

            // *********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        mUartService.close();
                        if(hr_config.isValid())
                        {
                            mUartService.connect(mDevice.getAddress());
                            BAND_CONNECTED = true;
                        }

                    }
                });
            }

            // *********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mUartService.enableTXNotification();
                try {
                    Thread.currentThread();
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // *********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                bleNotify.doParse(BaseActivity.this, txValue);
            }
            // *********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT)) {
                mUartService.disconnect();
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mUartService.close();
                if(hr_config.isValid())
                {
                    mUartService.connect(mDevice.getAddress());
                }
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE_BAND:
                // When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice
                            + "mserviceValue" + mUartService);
                    //if (intf == intf_ble_uart)
                        mUartService.connect(mDevice.getAddress());
                }
                break;
            case REQUEST_ENABLE_BT_BAND:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                    // 当DeviceListActivity返回与设备连接的消息
                    if (resultCode == Activity.RESULT_OK) {
                        // 得到链接设备的MAC
                        String address = data.getExtras().getString(
                                EXTRA_DEVICE_ADDRESS, "");
                        // 得到BLuetoothDevice对象
                        if (!TextUtils.isEmpty(address)) {
                            BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
                            ConnectThread(device);

                        }
                    }
                break;

                default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
    /**
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum02(String str) {
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        return isNum.matches();
    }

    // 连接设备
    public void ConnectThread(BluetoothDevice device) {
        try {
            mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            mBtAdapter.cancelDiscovery();
            mmSocket.connect();
            Toast.makeText(BaseActivity.this, "连接成功", Toast.LENGTH_LONG).show();
            ImageView itemIconBlue = new ImageView(BaseActivity.this);
            itemIconBlue.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.bluetooth_lighted));
            buttonFou.removeAllViews();
            buttonFou.setContentView(itemIconBlue);
            CONNECT_STATUS = true;
            CAN_NOTIFY = true;
             //接收数据进程
            ReceiveData receivethread = new ReceiveData();// 连接成功后开启接收数据服务
            receivethread.start();
        } catch (IOException e) {
            Toast.makeText(BaseActivity.this, "连接失败", Toast.LENGTH_SHORT)
                    .show();
            CONNECT_STATUS = false;
            try {
                mmSocket.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }
    }

    // 创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Intent接收器，返回结果

    public boolean isOdd(int a){
        //是奇数
        return (a & 1) == 1;
    }
    // 读数据线程
    private class ReceiveData extends Thread {

        private ReceiveData() {
            try {
                mmInStream = mmSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            int msgbytes;
            int ch;
            byte[] msgbuffer = new byte[1024];

            while (CONNECT_STATUS) {
                try { // 接收数据
                    msgbytes = 0;
                    while ((ch = mmInStream.read()) != '\n')
                    {
                        if(ch != -1){
                            msgbuffer[msgbytes] = (byte) ch;
                            msgbytes++;
                        }
                    }
                    msgbuffer[msgbytes] = (byte) '\n';
                    msgbytes++;
                    readStr = new String(msgbuffer, 0, msgbytes);
                    new HeadAnalysis().Head(readStr, BaseActivity.this);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    Runnable runnableUi = new Runnable(){
        @Override
        public void run() {
            //更新界面
            if(EXTEND_LEGACY){
                Log.d(TAG, "run: "+TotalNumber+" & "+UnconnectableNumber);
                //Tv5.setText("节点数：" + TotalNumber  + " 邻居数：" + NeighborNumber +" 丢失数："+ UnconnectableNumber);
                //Tv5.setText("节点数：" + TotalNumber +" 丢失数："+ UnconnectableNumber);
                Tv5.setText(String.valueOf(TotalNumber) );
                Tv7.setText(String.valueOf(UnconnectableNumber));
            }
//            else{
//                Tv5.setText("蓝牙尚未连接");
//            }
            Tv5.setGravity(Gravity.START);

        }
    };

    public Timer timerFlash = new Timer();
    public TimerTask timerFlashTask = new TimerTask() {
        @Override
        public void run() {
            new Thread(){
                public void run(){
                    handler.post(runnableUi);
                    Log.d(TAG, "Timer: record now is " + recordMessage);
                }
            }.start();
        }
    };

    public Runnable runnableUiBlue = new Runnable(){
        @Override
        public void run() {
            //更新界面
            //toolbar.setBackgroundColor(getResources().getColor(dodgerblue));
        }
    };
    public Runnable runnableUiYellow =new Runnable(){
        @Override
        public void run() {
            //toolbar.setBackgroundColor(getResources().getColor(R.color.materialComplementary));
        }
    };
    public Runnable runnableUiRed = new Runnable(){
        @Override
        public void run() {
            //更新界面
            //toolbar.setBackgroundColor(getResources().getColor(R.color.materialTriadic2));
        }
    };

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWarningMessageEvent(LocationActivity.MessageEvent event) {
        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMessageEvent: " + event.message);
        sendNotificationOthers(Integer.parseInt(event.message) );

    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPeaceMessageEvent(LocationActivity.PeaceMessageEvent event) {
        Log.d(TAG, "onPeaceMessageEvent: " + event.peaceIndex);
        if(event.peaceIndex)
            handler.post(runnableUiBlue);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecognizeModule.MessageEvent event) {
        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMessageEvent: " + event.message);
        String extraMessage = StringTrans.str2HexStr(event.message);
        EventBus.getDefault().post(new BaseActivity.RecordMessageEvent(extraMessage));
        //receivedMessage = StringTrans.hexStr2Str(extraMessage);
//        tvRecordResult.setText("录音结果："+event.message);
//        txRecordEncode.setText("转码结果"+extraMessage);
//        if(CONNECT_STATUS)
//            write(extraMessage);
//        //txEncodeCompressed.setText(receivedMessage);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRecordMessageEvent(HeadAnalysis.RecordMessageEvent event) {
//        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
//
//        //String mandarin = StringTrans.hexStr2Str(event.recordMessage[1]);
//
//        String mandarin = StringTrans.hexUTF82String(event.recordMessage[1]);
//        Log.d(TAG, "onRecordMessageEvent: " + mandarin);
//        new SpeakModule().speechSync(BaseActivity.this, mandarin);
//
//    }

    public class RecordMessageEvent {
        public final String message;
        public RecordMessageEvent(String message) {
            this.message = message;
        }

    }

    public class routeSearchEvent {

        public final Boolean callUp;
        //= "Eventbus test";

        public routeSearchEvent(Boolean callUp) {
            this.callUp = callUp;
        }

    }


//    public class WarningBroadcastReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals("com.example.myapplication.WARNING_BROADCAST")) {
//                int dangerNode = intent.getIntExtra("dangerNode",0);
//                sendNotificationOthers(dangerNode);
//            }
//        }
//    }
    public class LostBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.myapplication.LOST_BROADCAST"))
            edgeWarningNotification();
        }
    }
    public class SelfWarningBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.myapplication.SELF_WARN_BROADCAST"))
            sendNotificationSelf();
        }
    }
    public class PeaceBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.myapplication.PEACE_BROADCAST"))
                handler.post(runnableUiBlue);
        }
    }

    public void sendNotificationSelf() {
        String id1 = "channel_001";
        String name1 = "WarningMessageFromSelf";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;

        if(warnTypes == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(id1, name1, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100,200,300});
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this, id1)
                        .setChannelId(id1)
                        .setContentTitle("提醒")
                        .setContentText("节点" + SelfNumber + "（自身）发出警报")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("提醒")
                        .setContentText("节点" + SelfNumber + "（自身）发出警报")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setVibrate(new long[]{100,200,300})
                        .setContentIntent(pendingIntent)
                        .setChannelId(id1)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        ;//无效
                notification = notificationBuilder.build();
            }
        }else if (warnTypes == HR_Warning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(id1, name1, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300});
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this, id1)
                        .setChannelId(id1)
                        .setContentTitle("警报")
                        .setContentText("心率异常！！！请寻求救助！！！")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("警报")
                        .setContentText("心率异常！！！请寻求救助！！！")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setVibrate(new long[]{100, 200, 300})
                        .setContentIntent(pendingIntent)
                        .setChannelId(id1)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);//无效
                notification = notificationBuilder.build();
            }
        }

        if(notification != null)
        notificationManager.notify(NotificationId1, notification);
    }

    public void sendNotificationOthers(int dangerNode ){
        String id2 = "channel_002";
        String name2 = "WarningMessageFromOthers";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        String showUpText = null;
        new Thread(){
            public void run(){
                handler.post(runnableUiYellow);
            }
        }.start();
        if(GET_WARNED && getWarnedTypes != 0 && getWarnedTypes != 2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id2, name2, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100,200,300});
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);
                if (1 == getWarnedTypes) {
                    showUpText = "节点" + dangerNode + "发出警报";
                }
                else if (2 == getWarnedTypes) {
                    showUpText = "节点" + dangerNode + "濒临走失";
                } else if (3 == getWarnedTypes) {
                    showUpText = "节点" + dangerNode + "生命体征异常";
                }

                notification = new Notification.Builder(this, id2)
                        .setChannelId(id2)
                        .setContentTitle("提醒")
                        .setContentText(showUpText)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .build();

            }else  {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id2)
                        .setContentTitle("提醒")
                        .setContentText("节点" + dangerNode + "发出警报")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setVibrate(new long[]{100,200,300})
                        .setChannelId(id2)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        ;//无效
                notification = notificationBuilder.build();
            }
        }
        if(notification != null)
        notificationManager.notify(NotificationId2, notification);
    }


    public void edgeWarningNotification() {
        String id3 = "channel_003";
        String name3 = "EdgeWarning";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        new Thread(){
            public void run(){
                handler.post(runnableUiRed);
            }
        }.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id3, name3, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,200,100});
            notificationManager.createNotificationChannel(mChannel);

            notification = new Notification.Builder(this, id3)
                    .setChannelId(id3)
                    .setContentTitle("警报")
                    .setContentText("超出边缘，请退回或检查蓝牙连接！！！！！")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
        }else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("警报")
                    .setContentText("超出边缘，请退回或检查蓝牙连接！！！！！")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setVibrate(new long[]{100,200,300,200,100})
                    .setChannelId(id3)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    ;//无效
            notification = notificationBuilder.build();
        }
        notificationManager.notify(NotificationId3, notification);
    }

    // 取消链接
    public void cancelconnect() {
        try {
            mmSocket.close();
            CONNECT_STATUS = false;
            CAN_NOTIFY = false;
            ImageView itemIcon = new ImageView(BaseActivity.this);
            itemIcon.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this ,R.drawable.bluetooth));
            buttonFou.removeAllViews();
            buttonFou.setContentView(itemIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //子类去实现初始化方法
    public abstract void init();

    public void showToast(CharSequence text){
        Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        points.clear();
        timerFlash.cancel();
        //timerChanged.cancel();
        stopTimer();
        Log.d(TAG, "onDestroy: Before super");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        unbindService(mServiceConnection);
        if (mUartService != null) {
            mUartService.stopSelf();
            mUartService = null;
        }

        //mLocalBroadcastManager.unregisterReceiver(mWarningBroadcastReceiver);
        mLocalBroadcastManager.unregisterReceiver(mSWBroadcastReceiver);
        mLocalBroadcastManager.unregisterReceiver(mLostBroadcastReceiver);
        Log.d(TAG, "onDestroy: After super");
    }
}
