package com.a000webhostapp.trackingdaily.dumpit_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class admin_area_list extends Fragment {
    private RecyclerView mRecyclerView;
    admin_area_recycleview adapter1;
    List<AreaCode> areaList1;
    private DatabaseReference myRef;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private String uid;
    private Button admin_add_new_area ;
    private String area_code;
    private ProgressBar mProgressView;
    int i=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_area_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.area_recycleview_admin);
        admin_add_new_area=(Button)rootView.findViewById(R.id.admin_add_area);
        mProgressView = (ProgressBar)rootView.findViewById(R.id.arealist_progress);





        layoutManager = new LinearLayoutManager(rootView.getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAuth= FirebaseAuth.getInstance();
        areaList1= new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));


        //Admin can add new area via this button



        admin_add_new_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),AdminAddMapsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //complaintList.add()
        myRef = FirebaseDatabase.getInstance().getReference("Area");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProgressView.setVisibility(View.VISIBLE);
                for(DataSnapshot areacode : dataSnapshot.getChildren()) {
                        AreaCode areaCode = areacode.getValue(AreaCode.class);
                        areaList1.add(areaCode);
                    mProgressView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastMessage("Area load error");
            }
        });

        adapter1 = new admin_area_recycleview(rootView.getContext(),areaList1);
        mRecyclerView.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();

        return rootView;
    }

    private void ToastMessage(String message) {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }





}
