package com.example.myapplication;

import com.baidu.mapapi.map.TextOptions;

public class TextOverlayActivity extends BaseActivity {
    @Override
    public void init() {
        TextOptions options = new TextOptions();
        options.position(xupt)          //文字位置
                .text("sos")            //文字内容
                .fontSize(20)           //文字大小
                .fontColor(0XFF000000)  //文字颜色
                .bgColor(0X55FF00)      //背景颜色
                .rotate(0);             //旋转
        baiduMap.addOverlay(options);

    }
}
