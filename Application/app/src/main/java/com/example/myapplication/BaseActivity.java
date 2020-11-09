package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends Activity {

    public static MapView mMapView;
    public static BaiduMap baiduMap;
    protected MapStatusUpdate mapStatusUpdate;
    public LocationClient mLocationClient;

    private static final String TAG = "BaseActivity";
    //下面四个是用来测试项目的坐标，真实开发中应该用蓝牙进行获取真实坐标
    protected static LatLng xupt = new LatLng(34.1606259200,108.9074823500);
    protected static LatLng ssd = new LatLng(34.1603459200,108.9074826500);
    protected static LatLng xqzf = new LatLng(34.1602259300,108.9074323600);
    protected static LatLng xxx = new LatLng(34.1602569300,108.9077783600);
    public static Button button1;
    public static Button button2;
    public static Button button3;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        //获取到位置信息时会回调该定位监听器


        setContentView(R.layout.activity_main);


        //添加这下面的一部分
        //动态申请权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
           permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
         }
         if(ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
          permissionList.add(Manifest.permission.READ_PHONE_STATE);
         }
         if(ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
         }
         if(!permissionList.isEmpty()){
            String[] permissions =permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, 1);
         }


        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        mMapView = (MapView) findViewById(R.id.bmapView);

        //获得地图控制器
        baiduMap = mMapView.getMap();

        /*隐藏比例按钮，默认是显示的
            mMapView.showScaleControl(false);

          隐藏缩放按钮，默认是显示的
            mMapView.showZoomControls(false);
         */

        //设置中心点.MapStatusUpdate放入setMapStatus方法中用来描述即将的变化
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(xupt);
        baiduMap.setMapStatus(mapStatusUpdate);

        //设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(20);
        baiduMap.setMapStatus(mapStatusUpdate);

        init();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向其他成员发送急救信息
                Toast.makeText(BaseActivity.this,"sos",Toast.LENGTH_LONG).show();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向请求急就的成员地点前进
            }
        });
    }

    //子类去实现初始化方法
    public abstract void init();

    /**
     * 在屏幕中央显示一个Toast
     * @param text
     */
    public void showToast(CharSequence text){
        Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
