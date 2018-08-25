package com.a000webhostapp.trackingdaily.dumpit;

import android.net.Uri;

import java.util.Date;

/**
 * Created by onu on 8/21/18.
 */

public class Complaint {
    String uid;
    String type;
    int val;
    double latitude;
    double longitude;
    String date;
    String status;
    String id;
    //String comp_url;
    //String accomp_url;
    String rspns;



    public Complaint(){

    }

    public Complaint(String uid,String type,int val,double longitude,double latitude,String date,String status,String id,String rspns){
        this.uid=uid;
        this.type=type;
        this.val=val;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date=date;
        this.status=status;
        this.id=id;
        //this.comp_url=comp_url;
        //this.accomp_url=accomp_url;
        this.rspns=rspns;
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

    public String getId() {
        return id;
    }

    public String getRspns() {
        return rspns;
    }
}
