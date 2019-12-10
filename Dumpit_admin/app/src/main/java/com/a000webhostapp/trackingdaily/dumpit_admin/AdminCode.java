package com.a000webhostapp.trackingdaily.dumpit_admin;



/**
 * Created by onu on 8/15/18.
 */

public class AdminCode {
    String str,uid;
    boolean bool;
    public AdminCode(){

    }

    public AdminCode(String str, boolean bool,String uid) {
        this.str = str;
        this.uid = uid;
        this.bool = bool;
    }

    public String getStr() {
        return str;
    }
    public String getUid() {
        return uid;
    }
    public boolean isBool() {
        return bool;
    }
}
