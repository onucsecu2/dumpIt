package com.a000webhostapp.trackingdaily.dumpit;



/**
 * Created by onu on 8/15/18.
 */

public class Informer {
    String email;
    String name;
    String nid;
    String ward;
    String mobile;
    String address;
    String type;
    public Informer(){

    }
    public Informer(  String name,String email,String nid, String ward,String mobile,String address,String type){
        this.email=email;
        this.name=name;
        this.nid=nid;
        this.ward=ward;
        this.mobile=mobile;
        this.address=address;
        this.type=type;
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
}
