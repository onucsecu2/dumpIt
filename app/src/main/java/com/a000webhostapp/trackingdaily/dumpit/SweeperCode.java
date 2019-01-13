package com.a000webhostapp.trackingdaily.dumpit;



/**
 * Created by onu on 8/15/18.
 */

public class SweeperCode {
    String str,uid,areacode;
    boolean bool;
    public SweeperCode(){

    }

    public SweeperCode(String str, boolean bool, String uid,String areacode) {
        this.str = str;
        this.uid = uid;
        this.areacode=areacode;
        this.bool = bool;
    }

    public String getStr() {
        return str;
    }
    public String getAreacode() {
        return areacode;
    }
    public String getUid() {
        return uid;
    }
    public boolean isBool() {
        return bool;
    }
}
