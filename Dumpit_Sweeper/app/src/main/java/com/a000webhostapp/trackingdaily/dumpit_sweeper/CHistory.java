package com.a000webhostapp.trackingdaily.dumpit_sweeper;

/**
 * Created by onu on 1/1/19.
 */

public class CHistory {

    int val;
    double latitude;
    double longitude;
    String date;
    String areacode;
    public CHistory() {
    }

    public CHistory(int val,double longitude,double latitude,String date,String areacode){
        this.val=val;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date=date;
        this.areacode=areacode;
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

    public String getAreacode() {
        return areacode;
    }


}
