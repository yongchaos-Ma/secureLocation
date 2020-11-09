package com.example.myapplication;

import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.example.myapplication.BaseActivity;

public abstract class RoutePlanSearchBaseActivity extends BaseActivity implements OnGetRoutePlanResultListener{
    protected RoutePlanSearch routePlanSearch;

    @Override
    public void init() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);
        routePlanSearchinit();
    }

    //路线搜索初始化代码在这个方法
    public abstract void routePlanSearchinit();
}
