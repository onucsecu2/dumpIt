package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.CircleOptions;
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

public class AdminAddMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker marker = null;
    private Marker mMarker;
    private GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private Button toggler;
    private Button next;
    private ImageView back;
    private boolean flag =false;

    private double latarr[] = new double[600];
    private double lanarr[] = new double[600];
    private double tmplan;
    private double tmplat;
    private int j = 0;


    private int tog;
    private int val;
    private String area_code;
    private LatLng mPoint;
    private String str;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    String areacode;
    public ArrayList<Marker> myList = new ArrayList<Marker>();
    private List<Sector> sector = new ArrayList<Sector>();
    private int k = 0;
    private int p=0;
    private ImageView type_1,type_2;
    private ColorMatrix matrix0 ;
    private ColorMatrix matrix1 ;

    private ColorMatrixColorFilter filter0;
    private ColorMatrixColorFilter filter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        next = (Button) findViewById(R.id.next_btn);
        back = (ImageView) findViewById(R.id.back_btn);
        type_1=(ImageView) findViewById(R.id.map_type_sat);
        type_2=(ImageView) findViewById(R.id.map_type_terrain);


        next.getBackground().setAlpha(80);
        next.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();


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



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddMapsActivity.this, AdminAreaRegisterActivity.class);
                double p11,p12,p21,p22,p31,p32,p41,p42;
                p11=myList.get(0).getPosition().latitude;
                p12=myList.get(0).getPosition().longitude;
                p21=myList.get(1).getPosition().latitude;
                p22=myList.get(1).getPosition().longitude;
                p31=myList.get(2).getPosition().latitude;
                p32=myList.get(2).getPosition().longitude;
                p41=myList.get(3).getPosition().latitude;
                p42=myList.get(3).getPosition().longitude;
                intent.putExtra("p11",p11);
                intent.putExtra("p12",p12);
                intent.putExtra("p21",p21);
                intent.putExtra("p22",p22);
                intent.putExtra("p31",p31);
                intent.putExtra("p32",p32);
                intent.putExtra("p41",p41);
                intent.putExtra("p42",p42);

                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddMapsActivity.this, AdminHomeActivity.class);
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
        getLocationCurrent();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        googleMap.setOnMarkerClickListener(this);
        final LatLng latLng = new LatLng(latitude, longitude);
        if (tog == 0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        } else if (tog == 1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // Add a marker in Sydney and move the camera
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


        Marker mark = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(String.valueOf(R.string.gps))
                .snippet(String.valueOf(R.string.gps_1))
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


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
                    areacode = areaCode.getAreacode();
                    PolygonOptions rectOptions = new PolygonOptions()
                            .add(new LatLng(p11, p12),
                                    new LatLng(p21, p22),
                                    new LatLng(p31, p32),
                                    new LatLng(p41, p42),
                                    new LatLng(p51, p52));
                    Sector sector1 = new Sector(rectOptions, areacode);
                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p11, p12), new LatLng(p21, p22), new LatLng(p31, p32), new LatLng(p41, p42), new LatLng(p51, p52))
                            .strokeColor(Color.RED)
                            .fillColor(0x3C00FF00));
                    sector.add(sector1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage("এলাকা পুনরুদ্ধারে ত্রুটি। অাবার চেষ্টা করুন");
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                mPoint = point;
                flag=false;
                float distance[] = new float[2];

                tmplat = point.latitude;
                tmplan = point.longitude;

                for (k = 0; k < j; k++) {
                    LatLng latLng1 = new LatLng(latarr[k], lanarr[k]);
                    Location.distanceBetween(point.latitude, point.longitude,
                            latLng1.latitude, latLng1.longitude, distance);
                    if (distance[0] < 25) {
                        tmplat = latarr[k];
                        tmplan = lanarr[k];
                        break;
                    }
                }

                    /*if(marker!=null) {
                        marker.remove();
                    }*/
                    //onMarkerClick(marker);
                LatLng latLng2 = new LatLng(tmplat, tmplan);

                marker = mMap.addMarker(new MarkerOptions()
                        .snippet(str)
                        .rotation((float) 0.0)
                        .position(latLng2));


                for (Sector sector1 : sector) {
                    PolygonOptions polygonOptions = sector1.getPolygonOptions();
                    List<LatLng> list;
                    list = polygonOptions.getPoints();
                    if (PolyUtil.containsLocation(point, list, false)) {
                        marker.remove();
                        toastMessage("Conflicting Area");
                        flag=true;
                    }
                }

                if(p<4 && flag!=true) {
                    myList.add(marker);
                    p++;
                }
                else{
                    marker.remove();
                    toastMessage("cant put extra marker");

                }
                if (p==4){
                    next.getBackground().setAlpha(255);
                    next.setEnabled(true);

                }
                else{
                    next.getBackground().setAlpha(100);
                    next.setEnabled(false);
                }


            }
        });


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        myList.remove(marker);
        toastMessage("pin deleted");
        marker.remove();
        toastMessage(String.valueOf(myList.size()));
        next.getBackground().setAlpha(100);
        next.setEnabled(false);
        p--;
        return false;
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
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        else{
            toastMessage(getResources().getString(R.string.reqLocShare));
            Intent gpsOptionsIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }
    @Override
    public void onResume() {

        super.onResume();
    }

}
/*fillColor(0x550000FF));*/