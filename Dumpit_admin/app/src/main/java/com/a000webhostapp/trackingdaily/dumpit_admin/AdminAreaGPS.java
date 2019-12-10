package com.a000webhostapp.trackingdaily.dumpit_admin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
    private Button back_btn,next_btn;
    private ImageView type_1,type_2,del_btn;
    private double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
    private String areacode,descript;

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
        next_btn.setVisibility(View.INVISIBLE);
        back_btn.setVisibility(View.INVISIBLE);
        del_btn.setVisibility(View.INVISIBLE);
        next_btn.setEnabled(false);
        back_btn.setEnabled(false);
        del_btn.setEnabled(true);
        del_btn.setVisibility(View.VISIBLE);

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
        areacode=recievedIntent.getStringExtra("areacode");
        descript=recievedIntent.getStringExtra("descript");

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
                intent.putExtra("p11",p11);
                intent.putExtra("p12", p12);
                intent.putExtra("p21", p21);
                intent.putExtra("p22", p22);
                intent.putExtra("p31", p31);
                intent.putExtra("p32", p32);
                intent.putExtra("p41", p41);
                intent.putExtra("p42", p42);
                intent.putExtra("p51", p51);
                intent.putExtra("p52", p52);
                intent.putExtra("areacode", areacode);
                intent.putExtra("descript", descript);
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