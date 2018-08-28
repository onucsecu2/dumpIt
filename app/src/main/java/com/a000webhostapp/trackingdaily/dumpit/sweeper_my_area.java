package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onu on 7/27/18.
 */

public class sweeper_my_area extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Marker marker=null;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        toggler=(Button)rootView.findViewById(R.id.view_toggle);
        next=(Button)rootView.findViewById(R.id.informer_next_2);
        //next.getBackground().setAlpha(80);
        //next.setEnabled(false);
        next.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();
        tog = 0;


        gpsHelper= new GPSHelper(getContext());
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
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    private void toastMessage(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        final LatLng latLng = new LatLng(latitude, longitude);
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(2)
                .strokeWidth(2f)
                .fillColor(0x55ffff99));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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



    }
}
