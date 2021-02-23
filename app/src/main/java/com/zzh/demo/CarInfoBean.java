package com.zzh.demo;

/**
 * @author: zzh
 * data : 2020/9/25
 * description：
 */
public class CarInfoBean {
    private String carno;

    private String latitude;

    private String location;

    private String longitude;

    private String status;

    private String time;

    public void setCarno(String carno){
        this.carno = carno;
    }
    public String getCarno(){
        return this.carno;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
}
