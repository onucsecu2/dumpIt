package com.a000webhostapp.trackingdaily.dumpit_sweeper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by onu on 8/28/18.
 */

public class AreaCode {
    String areacode;
    String descr;
    double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;

    public AreaCode(){

    }

    public AreaCode(String areacode, String descr,double p11, double p12, double p21, double p22, double p31, double p32, double p41, double p42, double p51, double p52) {
        this.areacode = areacode;
        this.descr=descr;
        this.p11 = p11;
        this.p12 = p12;
        this.p21 = p21;
        this.p22 = p22;
        this.p31 = p31;
        this.p32 = p32;
        this.p41 = p41;
        this.p42 = p42;
        this.p51 = p51;
        this.p52 = p52;
    }
    public LatLng M1() {
        return new LatLng(p11,p12);
    }
    public LatLng M2() {
        return new LatLng(p21,p22);
    }
    public LatLng M3() {
        return new LatLng(p31,p32);
    }
    public LatLng M4() {
        return new LatLng(p41,p42);
    }
    public String getAreacode() {
        return areacode;
    }
    public String getDescr() {
        return descr;
    }

    public double getP11() {
        return p11;
    }

    public double getP12() {
        return p12;
    }

    public double getP21() {
        return p21;
    }

    public double getP22() {
        return p22;
    }

    public double getP31() {
        return p31;
    }

    public double getP32() {
        return p32;
    }

    public double getP41() {
        return p41;
    }

    public double getP42() {
        return p42;
    }

    public double getP51() {
        return p51;
    }

    public double getP52() {
        return p52;
    }
}
