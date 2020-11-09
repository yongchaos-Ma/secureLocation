package com.example.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class DemoListActivity extends ListActivity {

    private ClassAndName[] datas = {
            new ClassAndName(MainActivity.class,"进入程序"),
            new ClassAndName(TextOverlayActivity.class,"文字覆盖物"),
            new ClassAndName(MarkerOverlayActivity.class,"标志覆盖物"),
            new ClassAndName(WalkingSearchActivity.class,"步行路线搜索"),
            new ClassAndName(LocationActivity.class,"定位"),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<ClassAndName> adapter =
                new ArrayAdapter<ClassAndName>(this,android.R.layout.simple_list_item_1, datas);
        setListAdapter(adapter);
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
}
