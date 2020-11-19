package com.example.myapplication;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tbruyelle.rxpermissions2.RxPermissions;


public class DemoListActivity extends ListActivity {

    private final ClassAndName[] datas = {
            new ClassAndName(LocationActivity.class,"主程序"),
            new ClassAndName(LoginActivity.class,"登录程序测试"),
            new ClassAndName(MainActivity.class,"基础标记显示"),
            new ClassAndName(TextOverlayActivity.class,"文字覆盖物"),
            //new ClassAndName(MarkerOverlayActivity.class,"标志覆盖物"),
            new ClassAndName(WalkingSearchActivity.class,"步行路线搜索"),
            new ClassAndName(OfflineActivity.class,"离线地图包下载"),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<ClassAndName> adapter =
                new ArrayAdapter<ClassAndName>(this,android.R.layout.simple_list_item_1, datas);
        setListAdapter(adapter);

        getPermissions();
    }
    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        Toast.makeText(getApplicationContext(), "已经获取所需权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "未能获取所需权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //取出单击位置的ClassAndName
        ClassAndName classAndName = (ClassAndName) l.getItemAtPosition(position);
        startActivity(new Intent(this,classAndName.cls));
    }

    class ClassAndName{
        public Class<?> cls;
        public String name;

        public ClassAndName(Class<?> cls, String name) {
            this.cls = cls;
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
