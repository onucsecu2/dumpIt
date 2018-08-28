package com.a000webhostapp.trackingdaily.dumpit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  Marker marker=null;
    private Marker mMarker;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private Button toggler;
    private Button next;
    private int tog;
    private int val;
    private LatLng mPoint;
    private String str;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toggler=(Button)findViewById(R.id.view_toggle);
        next=(Button)findViewById(R.id.informer_next_2);
        next.getBackground().setAlpha(80);
        next.setEnabled(false);
        mAuth=FirebaseAuth.getInstance();
        Intent recievedIntent= getIntent();
        tog = recievedIntent.getIntExtra("tog",0);
        val = recievedIntent.getIntExtra("val",0);
        str = recievedIntent.getStringExtra("str");

       if(tog==0){
            toggler.setText("Terrain View");
        }
        if(tog==1){
            toggler.setText("Satellite View");
        }
        if(tog==2){
            toggler.setText("Normal View");
        }
        toggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tog=tog+1;
                if(tog>2){
                    tog=0;
                }
                //startActivity(getIntent());
                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                intent.putExtra("tog",tog);
                intent.putExtra("val",val);
                intent.putExtra("str",str);
                finish();
                startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, CameraActivity.class);
                intent.putExtra("val",val);
                intent.putExtra("str",str);
                intent.putExtra("longitude",mPoint.longitude);
                intent.putExtra("latitude",mPoint.latitude);
                startActivity(intent);
            }
        });


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
                .findFragmentById(R.id.map);
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
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(75)
                .strokeWidth(2f));
                //.fillColor(0x55ffff99));
        if(tog==0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        }
        else if(tog==1){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // Add a marker in Sydney and move the camera
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

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
                    .title("I'm here...")
                    .snippet("Now I am here,around the incidence")
                    .rotation((float) -15.0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        /*here all other pins will be shown*/

        myRef = FirebaseDatabase.getInstance().getReference("Complaints");
                myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot userID : dataSnapshot.getChildren()){
                        for(DataSnapshot locShot:userID.getChildren()){
                            Complaint location  = locShot.getValue(Complaint.class);


                            int valu=location.getVal();
                            double lon=location.getLongitude();
                            double lat=location.getLatitude();

                            String snip=location.getType();
                            snip+="\n"+location.getDate();
                            LatLng p = new LatLng(lat,lon);

                           int height = 100;
                            int width = 100;
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.comp_8);;
                            if(valu==1) {
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.com_8);
                            }
                            else if(valu==2){
                                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_5);
                            }
                            else if(valu==3){
                                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_9);
                            }
                            else if(valu==4){
                                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_6);
                            }
                            else if(valu==5) {
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.comp_8);
                            }
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            mMarker = mMap.addMarker(new MarkerOptions()
                                        .snippet(snip)
                                        .title(location.getType())
                                        .rotation((float) 0.0)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .position(p));

                           /* String s=snip;
                            s+=String.valueOf(lat);
                            s+=String.valueOf(lon);
                            s+=String.valueOf(valu);
                            toastMessage(s);*/

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    toastMessage(databaseError.getMessage().toString());
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                mPoint=point;
                float distance[] = new float[2];
                Location.distanceBetween( point.latitude, point.longitude,
                        latLng.latitude, latLng.longitude, distance);

                if( distance[0] > 75  ){
                    Toast.makeText(getBaseContext(), "You are far from the incidence", Toast.LENGTH_SHORT).show();
                } else {


                   // Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_SHORT).show();
                    if(marker!=null) {
                        marker.remove();
                    }

                    int height = 100;
                    int width = 100;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.comp_8);;
                    if(val==1) {
                        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.com_8);
                    }
                    else if(val==2){
                        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_5);
                    }
                    else if(val==3){
                        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_9);
                    }
                    else if(val==4){
                        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.com_6);
                    }
                    else if(val==5) {
                        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.comp_8);

                    }
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    marker=mMap.addMarker(new MarkerOptions()
                                .snippet(str)
                                .rotation((float)0.0)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .position(point));
                   // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.complaints_5));

                        next.getBackground().setAlpha(255);
                        next.setEnabled(true);
                }


            }
        });


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
/*fillColor(0x550000FF));*/