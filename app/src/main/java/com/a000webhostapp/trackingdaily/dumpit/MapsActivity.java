package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.shuhart.stepview.StepView;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  Marker marker=null;
    private Marker mMarker;
    private  GPSHelper gpsHelper;
    private Location mLocation;
    private double longitude, latitude;
    private Button next;
    private ImageView back;


    private int tog;
    private int val;
    private String area_code;
    private LatLng mPoint;
    private String str;
    private FirebaseAuth mAuth;
    private Handler mHandler = new Handler();
    private DatabaseReference myRef;
    String areacode;

    private ImageView type_1,type_2;
    private ColorMatrix matrix0 ;
    private ColorMatrix matrix1 ;
    private ColorMatrixColorFilter filter0;
    private ColorMatrixColorFilter filter1;
    private StepView stepView;

    private List<Sector>sector= new ArrayList<Sector>();
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        next = (Button) findViewById(R.id.next_btn);
        back = (ImageView) findViewById(R.id.back_btn);
        type_1=(ImageView) findViewById(R.id.map_type_sat);
        type_2=(ImageView) findViewById(R.id.map_type_terrain);



        stepView=(StepView)findViewById(R.id.step_view);
        stepView.setStepsNumber(3);

        stepView.go(1,true);

        next.getBackground().setAlpha(80);
        next.setEnabled(false);
        mAuth=FirebaseAuth.getInstance();


        Intent recievedIntent= getIntent();
        val = recievedIntent.getIntExtra("val",0);
        str = recievedIntent.getStringExtra("str");

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
                Intent intent = new Intent(MapsActivity.this, CameraActivity.class);
                intent.putExtra("val",val);
                intent.putExtra("str",str);
                intent.putExtra("longitude",mPoint.longitude);
                intent.putExtra("latitude",mPoint.latitude);
                intent.putExtra("areacode",area_code);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, InformerHomeActivity.class);
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

      /*  */

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

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
        myRef = FirebaseDatabase.getInstance().getReference("Area");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot area : dataSnapshot.getChildren()){

                    AreaCode areaCode = area.getValue(AreaCode.class);
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
                    areacode=areaCode.getAreacode();
                    PolygonOptions rectOptions = new PolygonOptions()
                            .add(new LatLng(p11, p12),
                                    new LatLng(p21, p22),
                                    new LatLng(p31, p32),
                                    new LatLng(p41, p42),
                                    new LatLng(p51, p52));
                    Sector sector1= new Sector(rectOptions,areacode);
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
                mPoint=point;
                float distance[] = new float[2];
                Location.distanceBetween( point.latitude, point.longitude,
                        latLng.latitude, latLng.longitude, distance);

                if( distance[0] > 75  ){
                    Toast.makeText(getBaseContext(), R.string.gps2, Toast.LENGTH_SHORT).show();
                } else {

                   // Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_SHORT).show();
                    if(marker!=null) {
                        marker.remove();
                    }
                    stepView.done(true);
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
                    for (Sector sector1 : sector)
                    {
                        PolygonOptions polygonOptions=sector1.getPolygonOptions();
                        List<LatLng> list;
                        list=polygonOptions.getPoints();
                        if(PolyUtil.containsLocation(point,list,false)){
                            area_code=sector1.getAreacode();
                            toastMessage(area_code);
                        }
                    }

                }


            }
        });


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
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