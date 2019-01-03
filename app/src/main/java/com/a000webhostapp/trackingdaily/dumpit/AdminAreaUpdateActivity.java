package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
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
import com.google.firebase.database.DatabaseReference;

public class AdminAreaGPS extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  Marker marker=null;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private LatLng mPoint;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    private double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_areagps);

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




       Marker mark= mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("I'm here...")
                    .snippet("My Location")
                    .rotation((float) -15.0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        /*here all other pins will be shown*/


        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42),new LatLng(p51,p52))
                            .strokeColor(Color.RED));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        polygon.setClickable(true);
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                Intent intent= new Intent(getApplicationContext(),AdminAreaUpdateActivity.class);
                startActivity(intent);
            }
        });


        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(p11,p12));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
/*fillColor(0x550000FF));*/