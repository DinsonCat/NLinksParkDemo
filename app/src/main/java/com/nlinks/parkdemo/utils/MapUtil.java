package com.nlinks.parkdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 地图工具类
 *
 * @author lzhixing@linewell.com
 *         Created at 2015/12/06 19:35
 */
public class MapUtil {

    private static Set<String> set = new HashSet<>();//保存搜索历史记录set

    /**
     * 从10个POI点中筛选离当前位置最近的POI信息
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/06 23:14
     */
    public static PoiInfo getClosestPoi(List<PoiInfo> poiInfoList, final LatLng latLng) {
        if (poiInfoList.size() == 0)
            return null;
        Collections.sort(poiInfoList, new Comparator<PoiInfo>() {
            @Override
            public int compare(PoiInfo poi1, PoiInfo poi2) {
                double v1 = DistanceUtil.getDistance(poi1.location, latLng);
                double v2 = DistanceUtil.getDistance(poi2.location, latLng);
                return Double.compare(v1, v2);
            }
        });
        return poiInfoList.get(0);
    }
//
//    /**
//     * 获取最近并且有剩余车位的停车场
//     *
//     * @author lzhixing@linewell.com
//     * Created at 2015/12/24 18:46
//     */
//    public static ParkList getClosestPark(Collection<Marker> values, final LatLng point) {
//        if (values.size() == 0)
//            return null;
//        List<ParkList> list = new LinkedList<>();
//        ParkList parkList;
//        for (Marker marker : values) {
//            parkList = (ParkList) marker.getExtraInfo().getSerializable("park");
//            if (parkList != null && parkList.getUnuedStallNum() > 0) {
//                list.add(parkList);
//            }
//        }
//        Collections.sort(list, new Comparator<ParkList>() {
//            @Override
//            public int compare(ParkList park1, ParkList park2) {
//                double v1 = DistanceUtil.getDistance(new LatLng(park1.getLatitude(), park1.getLongitude()), point);
//                double v2 = DistanceUtil.getDistance(new LatLng(park2.getLatitude(), park2.getLongitude()), point);
//                return Double.compare(v1, v2);
//            }
//        });
//        return list.get(0);
//    }

    /**
     * 获取最近并且有剩余车位的停车场
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/24 18:46
     *//*
    public static ParkMain getClosestPark(final LatLng point, Collection<ParkMarker> values) {
        if (values.size() == 0)
            return null;
        List<ParkMain> list = new LinkedList<>();
        for (ParkMarker parkList : values) {
            if (parkList.getPark().getUnuedStallNum() > 0) {
                list.add(parkList.getPark());
            }
        }
        Collections.sort(list, new Comparator<ParkMain>() {
            @Override
            public int compare(ParkMain park1, ParkMain park2) {
                double v1 = DistanceUtil.getDistance(new LatLng(park1.getLatitude(), park1.getLongitude()), point);
                double v2 = DistanceUtil.getDistance(new LatLng(park2.getLatitude(), park2.getLongitude()), point);
                return Double.compare(v1, v2);
            }
        });
        return list.size() > 0 ? list.get(0) : null;
    }*/

    /**
     * 隐藏百度地图Logo、比例尺及缩放工具
     *
     * @param type 隐藏全部:1 ; 只隐藏比例尺及缩放控件:2
     * @author lzhixing@linewell.com
     * Created at 2015/12/05 14:02
     */
    public static void hideBaiduMapLogo(MapView mapView, int type) {
        if (type == 1) {
            //隐藏logo
            View child = mapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
                child.setVisibility(View.INVISIBLE);
            }
            //地图上比例尺
            mapView.showScaleControl(false);
            //隐藏缩放控件
            mapView.showZoomControls(false);
        } else if (type == 2) {
            //地图上比例尺
            mapView.showScaleControl(false);
            //隐藏缩放控件
            mapView.showZoomControls(false);
        }
    }

    /**
     * 隐藏百度地图Logo、比例尺及缩放工具
     *
     * @param type 隐藏全部:1 ; 只隐藏比例尺及缩放控件:2
     * @author lzhixing@linewell.com
     * Created at 2015/12/05 14:02
     */
    public static void hideBaiduMapLogo(TextureMapView mapView, int type) {
        if (type == 1) {

            //隐藏logo
            View child = mapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
                child.setVisibility(View.INVISIBLE);
            }
            //地图上比例尺
            mapView.showScaleControl(false);
            //隐藏缩放控件
            mapView.showZoomControls(false);
        } else if (type == 2) {
            //地图上比例尺
            mapView.showScaleControl(false);
            //隐藏缩放控件
            mapView.showZoomControls(false);
        }
    }


    /**
     * 设置当前城市
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/09 10:41
     */
    public static void setNowCity(Context context, String city) {
        SharedPreferences sp = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("now_city", city);
        editor.commit();
    }

    /**
     * 获取当前城市
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/09 10:43
     */
    public static String getNowCity(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
        return sp.getString("now_city", null);
    }

    /**
     * 设置最近一次的定位坐标
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/09 10:48
     */
    public static void setLastLocation(Context context, LatLng point) {
        SharedPreferences sp = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("last_latitude", point.latitude + "");
        editor.putString("last_longitude", point.longitude + "");
        editor.commit();
    }

    /**
     * 获取最近一次的定位坐标
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/09 10:55
     */
    public static LatLng getLastLocation(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
        double latitude = Double.valueOf(sp.getString("last_latitude", "0"));
        double longitude = Double.valueOf(sp.getString("last_longitude", "0"));
        return latitude == 0 && longitude == 0 ? null : new LatLng(latitude, longitude);
    }

    /**
     * 根据距离判断显示km或m
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/19 15:27
     */
    public static String getConvertDistance(double distance) {
        DecimalFormat df = new DecimalFormat("#.0");
        return distance >= 1000 ? df.format(distance / 1000) + " km" : (int) distance + " m";
    }

    /**
     * 保存搜索历史记录
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/23 21:28
     */
    public static void setSearchHistory(Context context, PoiInfo poi) {
        SharedPreferences sp = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(poi);
        set.add(json);
        editor.putStringSet("history_set", set);
        editor.commit();
    }

    /**
     * 获取搜索历史记录
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/23 21:32
     */
    public static List<PoiInfo> getSearchHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json;
	    List history_list = new ArrayList();
        Set temp_set = sp.getStringSet("history_set", new LinkedHashSet<String>());
        Iterator iterator = temp_set.iterator();
        while (iterator.hasNext()) {
            json = (String) iterator.next();
	        set.add(json);
            history_list.add(gson.fromJson(json, PoiInfo.class));
        }
        return history_list;
    }
    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = 180 / 3.14169;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }
    /**
     * 清除历史记录
     *
     * @author lzhixing@linewell.com
     * Created at 2015/12/24 11:32
     */
    public static void clearSearchHistory(Context context) {
        set.clear();
        //history_list.clear();
        SharedPreferences sp = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
