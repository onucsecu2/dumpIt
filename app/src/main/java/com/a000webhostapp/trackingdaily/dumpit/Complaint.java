package com.a000webhostapp.trackingdaily.dumpit;

import java.util.Date;

/**
 * Created by onu on 8/21/18.
 */

public class Locations {
    String uid;
    String type;
    int val;
    double latitude;
    double longitude;
    String date;
    String status;


    public Locations(){

    }

    public Locations(String uid,String type,int val,double longitude,double latitude,String date,String status){
        this.uid=uid;
        this.type=type;
        this.val=val;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date=date;
        this.status=status;
    }

    public String getUid() {
        return uid;
    }

    public String getType() {
        return type;
    }

    public int getVal() {
        return val;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
