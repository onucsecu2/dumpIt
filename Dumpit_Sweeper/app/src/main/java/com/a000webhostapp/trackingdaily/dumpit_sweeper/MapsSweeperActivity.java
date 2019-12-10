package com.a000webhostapp.trackingdaily.dumpit_sweeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
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

public class MapsSweeperActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button  back;
    private  Marker marker=null;
    private Marker mMarker;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private LatLng mPoint;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_sweeper);
        mAuth=FirebaseAuth.getInstance();
        back=(Button)findViewById(R.id.sweeper_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsSweeperActivity.this, SweeperHomeActivity.class);
                startActivity(intent);
            }
        });
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if(!gps_enabled){
            toastMessage(getResources().getString(R.string.reqLocShare));
            Intent gpsOptionsIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
        onRestart();
        //toastMessage("stepped");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_sweeper);
        mapFragment.getMapAsync(this);
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


        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(18);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


       Marker mark= mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(String.valueOf(R.string.gps))
                    .snippet(String.valueOf(R.string.gps_1))
                    .rotation((float) -15.0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        /*here all other pins will be shown*/

        myRef = FirebaseDatabase.getInstance().getReference("Area").child("A1");
                myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    AreaCode areaCode =dataSnapshot.getValue(AreaCode.class);
                    double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
                    p11=areaCode.getP11();
                    p12=areaCode.getP12();
                    p21=areaCode.getP21();
                    p22=areaCode.getP22();
                    p31=areaCode.getP31();
                    p32=areaCode.getP32();
                    p41=areaCode.getP41();
                    p42=areaCode.getP42();
                    p51=areaCode.getP51();
                    p52=areaCode.getP52();

                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42),new LatLng(p51,p52))
                            .strokeColor(Color.RED));


                    //.fillColor(Color.BLUE));


                    /*for(DataSnapshot area : dataSnapshot.getChildren()){
                        AreaCode areaCode =area.getValue(AreaCode.class);
                        double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
                        p11=areaCode.getP11();
                        p12=areaCode.getP12();
                        p21=areaCode.getP21();
                        p22=areaCode.getP22();
                        p31=areaCode.getP31();
                        p32=areaCode.getP32();
                        p41=areaCode.getP41();
                        p42=areaCode.getP42();
                        p51=areaCode.getP51();
                        p52=areaCode.getP52();

                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42),new LatLng(p51,p52))
                                .strokeColor(Color.RED));
                                //.fillColor(Color.BLUE));

                    }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    toastMessage(databaseError.getMessage().toString());
            }
        });
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
    private void getLocationCurrent(){
        gpsHelper= new GPSHelper(getApplicationContext());
        mLocation = gpsHelper.getLocation();
        try {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }catch (Exception e) {
            toastMessage(getResources().getString(R.string.reqLocShare));
            // toastMessage(e.getMessage());
        }
    }
    public void onRestart() {

        super.onRestart();
        // toastMessage("restart");
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gps_enabled) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getLocationCurrent();


            //mLoading.setVisibility(View.GONE);
            //circleProgressBar.setVisibility(circleProgressBar.GONE);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_sweeper);
            mapFragment.getMapAsync(this);

        }
        else{
            toastMessage(getResources().getString(R.string.reqLocShare));
            Intent gpsOptionsIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }




    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
/*fillColor(0x550000FF));*/