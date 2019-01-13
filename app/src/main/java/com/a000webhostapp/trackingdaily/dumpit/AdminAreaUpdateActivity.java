package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminAreaUpdateActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener {

    public double diff=0.0001;
    private GoogleMap mMap;
    private ImageView type_1,type_2,del_btn;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private LatLng mPoint;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    private String areacode,areacodeTmp;
    private Button back_btn,next_btn;
    private double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
    private double latarr[] = new double[600];
    private double lanarr[] = new double[600];
    private Marker mark1,mark2,mark3,mark4;
    private  Polygon polygon,tmpPolygon;
    private int j = 0;
    private double tmplat,tmplan;
    private List<Sector> sector = new ArrayList<Sector>();
    private List<AreaCode> areaCodes = new ArrayList<AreaCode>();
    private List<Polygon>polygons=new ArrayList<Polygon>();


    private ColorMatrix matrix0 ;
    private ColorMatrix matrix1 ;

    private ColorMatrixColorFilter filter0;
    private ColorMatrixColorFilter filter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_areagps);
        back_btn=(Button)findViewById(R.id.back_btn);
        next_btn=(Button)findViewById(R.id.next_btn);
        del_btn=(ImageView)findViewById(R.id.del_btn);
        type_1=(ImageView) findViewById(R.id.map_type_sat);
        type_2=(ImageView) findViewById(R.id.map_type_terrain);
        del_btn.setEnabled(false);
        del_btn.setVisibility(View.INVISIBLE);

        matrix0 = new ColorMatrix();
        matrix1 = new ColorMatrix();
        matrix0.setSaturation(0);
        matrix1.setSaturation(3);

        filter0 = new ColorMatrixColorFilter(matrix0);
        filter1 = new ColorMatrixColorFilter(matrix1);


        type_1.setColorFilter(filter0);
        type_2.setColorFilter(filter1);

        type_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_1.setColorFilter(filter1);
                type_2.setColorFilter(filter0);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        type_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_1.setColorFilter(filter0);
                type_2.setColorFilter(filter1);
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });




        next_btn.setVisibility(View.VISIBLE);
        back_btn.setVisibility(View.VISIBLE);
        next_btn.setEnabled(true);
        back_btn.setEnabled(true);
        Intent recievedIntent= getIntent();
        p11 = recievedIntent.getDoubleExtra("p11",0);
        p12 = recievedIntent.getDoubleExtra("p12",0);
        p21 = recievedIntent.getDoubleExtra("p21",0);
        p22 = recievedIntent.getDoubleExtra("p22",0);
        p31 = recievedIntent.getDoubleExtra("p31",0);
        p32 = recievedIntent.getDoubleExtra("p32",0);
        p41 = recievedIntent.getDoubleExtra("p41",0);
        p42 = recievedIntent.getDoubleExtra("p42",0);
        p51 = recievedIntent.getDoubleExtra("p51",0);
        p52 = recievedIntent.getDoubleExtra("p52",0);
        p52 = recievedIntent.getDoubleExtra("p52",0);
        areacode = recievedIntent.getStringExtra("areacode");


        mAuth=FirebaseAuth.getInstance();
        gpsHelper= new GPSHelper(getApplicationContext());
        mLocation = gpsHelper.getLocation();
        try {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }catch (Exception e){
            toastMessage(String.valueOf(R.string.reqLocShare));
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_adminareagps);
        mapFragment.getMapAsync(this);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference("Area");
                myRef.child(areacode).removeValue();
                finish();
                Intent intent= new Intent(getApplicationContext(),AdminHomeActivity.class);
                startActivity(intent);

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),AdminAreaUpdateDetailsActivity.class);

                intent.putExtra("p11",p11);
                intent.putExtra("p12", p12);
                intent.putExtra("p21", p21);
                intent.putExtra("p22", p22);
                intent.putExtra("p31", p31);
                intent.putExtra("p32", p32);
                intent.putExtra("p41", p41);
                intent.putExtra("p42", p42);
                intent.putExtra("areacode", areacode);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng latLng = new LatLng(latitude, longitude);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



       final Marker mark= mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("I'm here...")
                    .snippet("My Location")
                    .rotation((float) -15.0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        /*here all other pins will be shown*/


        mark1= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(p11,p12))
                .title("Area Pin#1")
                .snippet("Area Pin#1")
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mark2= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(p21,p22))
                .title("Area Pin#2")
                .snippet("Area Pin#2")
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mark3= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(p31,p32))
                .title("Area Pin#3")
                .snippet("Area Pin#3")
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mark4= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(p41,p42))
                .title("Area Pin#4")
                .snippet("Area Pin#4")
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        mark1.setDraggable(true);
        mark2.setDraggable(true);
        mark3.setDraggable(true);
        mark4.setDraggable(true);

        myRef = FirebaseDatabase.getInstance().getReference("Area");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot area : dataSnapshot.getChildren()) {

                    AreaCode areaCode = area.getValue(AreaCode.class);
                    double p11, p12, p21, p22, p31, p32, p41, p42, p51, p52;
                    p11 = areaCode.getP11();
                    p12 = areaCode.getP12();
                    latarr[j] = p11;
                    lanarr[j] = p12;
                    j++;
                    p21 = areaCode.getP21();
                    p22 = areaCode.getP22();
                    latarr[j] = p21;
                    lanarr[j] = p22;
                    j++;
                    p31 = areaCode.getP31();
                    p32 = areaCode.getP32();
                    latarr[j] = p31;
                    lanarr[j] = p32;
                    j++;
                    p41 = areaCode.getP41();
                    p42 = areaCode.getP42();
                    latarr[j] = p41;
                    lanarr[j] = p42;
                    j++;
                    p51 = areaCode.getP51();
                    p52 = areaCode.getP52();
                    areacodeTmp = areaCode.getAreacode();
                    PolygonOptions rectOptions = new PolygonOptions()
                            .add(new LatLng(p11, p12),
                                    new LatLng(p21, p22),
                                    new LatLng(p31, p32),
                                    new LatLng(p41, p42),
                                    new LatLng(p51, p52));
                    Sector sector1 = new Sector(rectOptions, areacodeTmp);
                    /*Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42), new LatLng(p51, p52))
                            .strokeColor(Color.RED)
                            .fillColor(0x3C00FF00));*/
                    sector.add(sector1);
                    areaCodes.add(areaCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage("এলাকা পুনরুদ্ধারে ত্রুটি। অাবার চেষ্টা করুন");
            }
        });


      /*  Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42),new LatLng(p51,p52))
                            .strokeColor(Color.RED));*/

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(p11,p12));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);




        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                try{
                    polygon.remove();
                }catch(Exception e){

                }
                




                    if (marker.getId().equalsIgnoreCase("m1")) {

                        tmplat = p11;
                        tmplan = p12;

                    } else if (marker.getId().equalsIgnoreCase("m2")) {
                        tmplat = p21;
                        tmplan = p22;
                    } else if (marker.getId().equalsIgnoreCase("m3")) {
                        tmplat = p31;
                        tmplan = p32;
                    } else if (marker.getId().equalsIgnoreCase("m4")) {
                        tmplat = p41;
                        tmplan = p42;
                    }

               /* else{
                    tmplat=marker.getPosition().latitude;
                    tmplan=marker.getPosition().longitude;
               // toastMessage("zoom level: "+String.valueOf(mMap.getCameraPosition().zoom)+" "+String.valueOf(tmplat)+" "+String.valueOf(tmplan));
                }*/

            }

            @Override
            public void onMarkerDrag(Marker marker) {

                try{
                    tmpPolygon.remove();
                }catch (Exception e){

                }

                if (marker.getId().equalsIgnoreCase("m1")) {

                    tmpPolygon= mMap.addPolygon(new PolygonOptions()
                            .add(marker.getPosition(), mark2.getPosition(), mark3.getPosition(), mark4.getPosition())
                            .strokeColor(Color.GREEN)
                            .fillColor(0x3C00FF00));

                } else if (marker.getId().equalsIgnoreCase("m2")) {
                    tmpPolygon= mMap.addPolygon(new PolygonOptions()
                            .add(mark1.getPosition(), marker.getPosition(), mark3.getPosition(), mark4.getPosition())
                            .strokeColor(Color.GREEN)
                            .fillColor(0x3C00FF00));
                } else if (marker.getId().equalsIgnoreCase("m3")) {
                    tmpPolygon= mMap.addPolygon(new PolygonOptions()
                            .add(mark1.getPosition(), mark2.getPosition(), marker.getPosition(), mark4.getPosition())
                            .strokeColor(Color.GREEN)
                            .fillColor(0x3C00FF00));
                } else if (marker.getId().equalsIgnoreCase("m4")) {
                    tmpPolygon= mMap.addPolygon(new PolygonOptions()
                            .add(mark1.getPosition(), mark2.getPosition(), mark3.getPosition(), marker.getPosition())
                            .strokeColor(Color.GREEN)
                            .fillColor(0x3C00FF00));
                }

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                tmpPolygon.remove();
                if (marker.getId().equalsIgnoreCase("m1")) {

                    p11=marker.getPosition().latitude;
                    p12=marker.getPosition().longitude;

                } else if (marker.getId().equalsIgnoreCase("m2")) {

                    p21=marker.getPosition().latitude;
                    p22=marker.getPosition().longitude;
                } else if (marker.getId().equalsIgnoreCase("m3")) {

                    p31=marker.getPosition().latitude;
                    p32=marker.getPosition().longitude;
                } else if (marker.getId().equalsIgnoreCase("m4")) {

                    p41=marker.getPosition().latitude;
                    p42=marker.getPosition().longitude;
                }


                // toastMessage(String.valueOf(areaCodes.size()));
                for (AreaCode area : areaCodes)
                {
                    //if(area.getAreacode().equalsIgnoreCase("A1"))
                    //toastMessage(area.areacode+" "+String.valueOf(tmplat)+" "+String.valueOf(area.getP11())+" "+String.valueOf(Math.abs(tmplat-area.getP11())));
                    if(Math.abs(tmplat-area.getP11())<=diff && Math.abs(tmplan-area.getP12())<=diff){
                        area.p11=marker.getPosition().latitude;
                        area.p51=marker.getPosition().latitude;
                        area.p12=marker.getPosition().longitude;
                        area.p52=marker.getPosition().longitude;


                    }
                    else if(Math.abs(tmplat-area.getP21())<=diff && Math.abs(tmplan-area.getP22())<=diff){
                        area.p21=marker.getPosition().latitude;
                        area.p22=marker.getPosition().longitude;

                    }
                    else if(Math.abs(tmplat-area.getP31())<=diff && Math.abs(tmplan-area.getP32())<=diff){
                        area.p31=marker.getPosition().latitude;
                        area.p32=marker.getPosition().longitude;


                    }
                    else if(Math.abs(tmplat-area.getP41())<=diff && Math.abs(tmplan-area.getP42())<=diff){
                        area.p41=marker.getPosition().latitude;
                        area.p42=marker.getPosition().longitude;

                    }
                }




                for(Polygon polygon1 : polygons){

                    polygon1.remove();

                }
                polygons.clear();
                for (AreaCode area : areaCodes) {
                             polygons.add( mMap.addPolygon(new PolygonOptions()
                            .add(area.M1(),area.M2(), area.M3(), area.M4(), area.M1())
                            .strokeColor(Color.RED)
                            .fillColor(0x3C00FF00)));
                }

            }

        });

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
/*fillColor(0x550000FF));*/