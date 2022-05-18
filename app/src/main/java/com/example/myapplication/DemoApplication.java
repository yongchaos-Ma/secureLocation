package com.example.myapplication;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext   
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        // 语音识别初始化
        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

//        StringBuffer param = new StringBuffer();
//        param.append("appid="+"600a34da");
//        param.append(",");
//        // 设置使用v5+
//        param.append(SpeechConstant.ENGINE_MODE+"="+ SpeechConstant.MODE_MSC);
//        SpeechUtility.createUtility(com.example.myapplication.DemoApplication.this, param.toString());
    }
}