package com.a000webhostapp.trackingdaily.dumpit_sweeper;

import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by onu on 8/29/18.
 */

public class Sector {
    PolygonOptions polygonOptions;
    String areacode;

    public Sector(){

    }
    public Sector(PolygonOptions polygonOptions, String areacode) {
        this.polygonOptions = polygonOptions;
        this.areacode = areacode;
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public String getAreacode() {
        return areacode;
    }
}
