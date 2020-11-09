package com.example.myapplication;

import android.app.Activity;
import android.icu.text.Transliterator;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.HashMap;

import static com.example.myapplication.BaseActivity.baiduMap;
import static com.example.myapplication.BaseActivity.ssd;
import static com.example.myapplication.BaseActivity.xqzf;
import static com.example.myapplication.BaseActivity.xupt;
import static com.example.myapplication.BaseActivity.xxx;

public class MarkerOverlayActivity extends Activity {
    HashMap<LatLng,String> map = new HashMap<>();


    public void init() {
        MarkerOptions options = new MarkerOptions();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        options.position(xupt)//位置
                .title("xupt")//titile
                .icon(icon);     //图标
        baiduMap.addOverlay(options);
        //run();

        BitmapDescriptor iconb = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
        options.position(ssd)//位置
                .title("ssd")//titile
                .icon(iconb);     //图标
        baiduMap.addOverlay(options);

        BitmapDescriptor iconc = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
        options.position(xqzf)//位置
                .title("xqzf")//titile
                .icon(iconc);     //图标
        baiduMap.addOverlay(options);

        BitmapDescriptor icond = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
        options.position(xxx)//位置
                .title("xqzf")//titile
                .icon(icond);     //图标
        baiduMap.addOverlay(options);
    }

    public void show(LatLng latLng,String name){
        MarkerOptions options = new MarkerOptions();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_red);
        options.position(latLng)//位置
                .title(name)//titile
                .icon(icon);     //图标
        baiduMap.addOverlay(options);
    }
}
