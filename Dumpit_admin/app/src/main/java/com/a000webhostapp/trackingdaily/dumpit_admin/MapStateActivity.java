package com.a000webhostapp.trackingdaily.dumpit_admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;


import java.util.ArrayList;
import java.util.List;

public class MapStateActivity extends FragmentActivity implements OnMapReadyCallback {

    private ProgressBar mprogressBar;
    private GoogleMap mMap;
    private Marker mMarker;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private LatLng myLoc;
    private FirebaseAuth mAuth;
    private List<LatLng> list1 =new ArrayList<>();
    private List<LatLng> list2 =new ArrayList<>();
    private List<LatLng> list3 =new ArrayList<>();
    private int p=10;
    private ImageView back;
    private ImageView type_1,type_2;
    private ColorMatrix matrix0 ;
    private ColorMatrix matrix1 ;
    private ColorMatrixColorFilter filter0;
    private ColorMatrixColorFilter filter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_state);
        mprogressBar= findViewById(R.id.mProgressbar);
        mprogressBar.setVisibility(View.VISIBLE);
        back = (ImageView) findViewById(R.id.back_btn);
        type_1=(ImageView) findViewById(R.id.map_type_sat);
        type_2=(ImageView) findViewById(R.id.map_type_terrain);
        mAuth= FirebaseAuth.getInstance();

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



        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapStateActivity.this, AdminHomeActivity.class);
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
        getLocationCurrent();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
 /*       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hmap);
        mapFragment.getMapAsync(this);
*/
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CHistory");

        //list1.add(new LatLng(22.412015100412386, 91.8214938417077));


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ID:dataSnapshot.getChildren()){

                        CHistory complaint = ID.getValue(CHistory.class);
                        int val =complaint.getVal();
                       // toastMessage(String.valueOf(val));
                        if(val== 3){
                            list1.add(new LatLng(complaint.getLatitude(), complaint.getLongitude()));
                        }
                        else if(val==2) {
                            list2.add(new LatLng(complaint.getLatitude(), complaint.getLongitude()));
                        }
                        else if(val == 4)
                        list3.add(new LatLng(complaint.getLatitude(),complaint.getLongitude()));


                }
                try {
                    mprogressBar.setVisibility(View.GONE);
                    addHeatMap();
                }catch (Exception e){
                    toastMessage(e.getMessage().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage(databaseError.getMessage().toString());
            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;;
        mMap.addCircle(new CircleOptions()
                .center(myLoc)
                .radius(75)
                .strokeWidth(2f));
        //.fillColor(0x55ffff99));



        CameraUpdate center=
                CameraUpdateFactory.newLatLng(myLoc);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(18);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        Marker mark= mMap.addMarker(new MarkerOptions()
                .position(myLoc)
                .title(String.valueOf(R.string.gps))
                .snippet(String.valueOf(R.string.gps_1))
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void addHeatMap() {

        int[] colors1 = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        int[] colors2 = {
                Color.rgb(0, 123   , 255),
                Color.rgb(0, 0, 130) // green
                   // red
        };

        int[] colors3 = {
                Color.rgb(255, 0, 238), // green
                Color.rgb(255, 0, 76)   // red
        };

        float[] startPoints = {
                0.1f, 1f
        };

        Gradient gradient1 = new Gradient(colors1, startPoints);
        Gradient gradient2= new Gradient(colors2, startPoints);
        Gradient gradient3= new Gradient(colors3, startPoints);


        // Create a heat map tile provider, passing it the latlngs of the police stations.
        if(list1.size()>0) {
            HeatmapTileProvider mProvider1 = new HeatmapTileProvider.Builder()
                    .data(list1)
                    .gradient(gradient1)
                    .opacity(0.7)
                    .build();
            TileOverlay mOverlay1 = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider1));
        }
        if(list2.size()>0) {
            HeatmapTileProvider mProvider2 = new HeatmapTileProvider.Builder()
                    .data(list2)
                    .gradient(gradient2)
                    .opacity(0.7)
                    .build();
            TileOverlay mOverlay2 = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider2));
        }
        if(list3.size()>0) {
            HeatmapTileProvider mProvider3 = new HeatmapTileProvider.Builder()
                    .data(list3)
                    .gradient(gradient3)
                    .opacity(0.7)
                    .build();

            TileOverlay mOverlay3 = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider3));
            // Add a tile overlay to the map, using the heat map tile provider.
        }

    }

    private void getLocationCurrent(){
        gpsHelper= new GPSHelper(getApplicationContext());
        mLocation = gpsHelper.getLocation();
        try {
            myLoc = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
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
                    .findFragmentById(R.id.hmap);
            mapFragment.getMapAsync(this);

        }
        else{
            toastMessage(getResources().getString(R.string.reqLocShare));
            Intent gpsOptionsIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }
}
