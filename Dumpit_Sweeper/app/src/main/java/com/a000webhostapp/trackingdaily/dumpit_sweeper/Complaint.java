package com.a000webhostapp.trackingdaily.dumpit_sweeper;

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
    String areacode;
    //String comp_url;
    //String accomp_url;
    String rspns;
    String sid;
    boolean claim;



    public Complaint(){

    }

    public Complaint(String uid,String type,int val,double longitude,double latitude,String date,String status,String id,String rspns,String areacode,String sid,boolean claim){
        this.uid=uid;
        this.type=type;
        this.val=val;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date=date;
        this.status=status;
        this.id=id;
        this.areacode=areacode;
        this.sid=sid;
        //this.comp_url=comp_url;
        //this.accomp_url=accomp_url;
        this.rspns=rspns;
        this.claim= claim;
    }

    public boolean isClaim() {
        return claim;
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

    public String getSid() {
        return sid;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setRspns(String rspns) {
        this.rspns = rspns;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }
}
