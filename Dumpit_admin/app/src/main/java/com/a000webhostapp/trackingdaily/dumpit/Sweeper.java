package com.a000webhostapp.trackingdaily.dumpit;



/**
 * Created by onu on 8/15/18.
 */

public class Sweeper {
    String email;
    String name;
    String nid;
    String ward;
    String mobile;
    String address;
    String type;
    String sweeper_id;
    String areacode;
    public Sweeper(){

    }

    public Sweeper(String email, String name, String nid, String ward, String mobile, String address, String type, String sweeper_id, String areacode) {
        this.email = email;
        this.name = name;
        this.nid = nid;
        this.ward = ward;
        this.mobile = mobile;
        this.address = address;
        this.type = type;
        this.sweeper_id = sweeper_id;
        this.areacode = areacode;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNid() {
        return nid;
    }

    public String getWard() {
        return ward;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getSweeper_id() {
        return sweeper_id;
    }

    public String getAreacode() {
        return areacode;
    }
}
