package com.zzh.demo;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zzh
 * data : 2020/9/28
 * description：
 */
public class CarPosInfo {
    private String carno;
    private LatLng startLatlng;
    private LatLng endLatlng;
    private boolean isFirst;
    List<LatLng> polylines = new ArrayList<>();

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public LatLng getStartLatlng() {
        return startLatlng;
    }

    public void setStartLatlng(LatLng startLatlng) {
        this.startLatlng = startLatlng;
    }

    public LatLng getEndLatlng() {
        return endLatlng;
    }

    public void setEndLatlng(LatLng endLatlng) {
        this.endLatlng = endLatlng;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public List<LatLng> getPolylines() {
        return polylines;
    }

    public void setPolylines(List<LatLng> polylines) {
        this.polylines = polylines;
    }
}
