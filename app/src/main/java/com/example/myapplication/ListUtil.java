package com.example.myapplication;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListUtil {

    public static List<LatLng> getKey(Map<LatLng,Double> map, Double value){
        List<LatLng> keyList = new ArrayList<>();
        for(LatLng key: map.keySet()){
            if(Objects.equals(map.get(key), value)){
                keyList.add(key);
            }
        }

        return keyList;
    }

}
