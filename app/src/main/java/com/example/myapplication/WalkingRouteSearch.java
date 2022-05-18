package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
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

public class WalkingRouteSearch extends BaseActivity implements OnGetRoutePlanResultListener {

    // 浏览路线节点相关
    private final Button mBtnPre = null; // 上一个节点
    private final Button mBtnNext = null; // 下一个节点
    private RouteLine mRouteLine = null;
    private OverlayManager mRouteOverlay = null;
    private final boolean mUseDefaultIcon = false;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可

    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch = null;
    private WalkingRouteResult mWalkingRouteResult = null;
    private boolean hasShowDialog = false;

    private final String TAG = "WalkingSearch";

    @Override
    public void init() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
//        //地图长击事件
//        BaiduMap.OnMapLongClickListener listener = new BaiduMap.OnMapLongClickListener() {
//            public  void onMapLongClick(LatLng newPoint){
//                if(newPoint != null){
//                    targetPosition = newPoint;
//                }
//            }
//        };
    }



    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess() {
        // 重置浏览节点的路线数据
        mRouteLine = null;
        // 设置起终点信息 起点参数
        PlanNode startNode = PlanNode.withLocation(new LatLng(longitude, latitude));
        Log.d(TAG, "searchButtonProcess: " + new LatLng(longitude, latitude));
        // 终点参数
        PlanNode endNode = PlanNode.withLocation(newDestination);
        Log.d(TAG, "searchButtonProcess: " + newDestination);

        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(startNode) // 起点
                .to(endNode)); // 终点
    }

//    /**
//     * 节点浏览示例
//     */
//    public void nodeClick(View view) {
//        if (null != mRouteLine) {
//            mNodeUtils.browseRoutNode(view,mRouteLine);
//        }
//    }
//
//    /**
//     * 切换路线图标，刷新地图使其生效
//     * 注意： 起终点图标使用中心对齐.
//     */
//    public void changeRouteIcon(View v) {
//        if (mRouteOverlay == null) {
//            return;
//        }
//        if (mUseDefaultIcon) {
//            ((Button) v).setText("自定义起终点图标");
//            Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();
//        } else {
//            ((Button) v).setText("系统起终点图标");
//            Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();
//        }
//        mUseDefaultIcon = !mUseDefaultIcon;
//        mRouteOverlay.removeFromMap();
//        mRouteOverlay.addToMap();
//    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 步行路线结果回调
     *
     * @param result  步行路线结果
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (null == result) {
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(WalkingRouteSearch.this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(WalkingRouteSearch.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {

            if (result.getRouteLines().size() > 1) {
                mWalkingRouteResult = result;
                if (!hasShowDialog) {
                    SelectRouteDialog selectRouteDialog = new SelectRouteDialog(WalkingRouteSearch.this,
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
                            mRouteOverlay = overlay;
                            overlay.setData(mWalkingRouteResult.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
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
                mRouteOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放检索对象
        if (mSearch != null) {
            mSearch.destroy();
        }
    }
}
