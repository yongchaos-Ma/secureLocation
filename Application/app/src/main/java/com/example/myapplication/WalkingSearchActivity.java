package com.example.myapplication;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

public class WalkingSearchActivity extends RoutePlanSearchBaseActivity {

    @Override
    public void routePlanSearchinit() {
        baiduMap.setMapStatus(mapStatusUpdate);
        routePlanSearch.walkingSearch(getSearchParams());

    }

    private WalkingRoutePlanOption getSearchParams() {
        WalkingRoutePlanOption params = new WalkingRoutePlanOption();
        params.from(PlanNode.withLocation(xupt));//设置起点
        params.to(PlanNode.withLocation(ssd));//设置终点
        return params;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
        baiduMap.setOnMarkerClickListener(overlay);
        List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();//获取到所有的搜索路线
        overlay.setData(routeLines.get(0));//把搜索结果设置到覆盖物
        overlay.addToMap();//把搜索结果添加到地图
        overlay.zoomToSpan();//把搜索结果在一个屏幕内显示
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

}
