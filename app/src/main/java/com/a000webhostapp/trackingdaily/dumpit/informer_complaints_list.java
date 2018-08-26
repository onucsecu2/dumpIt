package com.a000webhostapp.trackingdaily.dumpit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class informer_complaints_list extends Fragment {
    private RecyclerView mRecyclerView;
    informer_complaints_list_recycleview adapter;
    List<Complaint> complaintList;
    private DatabaseReference myRef;
    private LinearLayoutManager layoutManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.informer_complaints_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.comp_recycleview_informer);
        //layoutManager = (LinearLayoutManager) rootView.findViewById(R.id.comp_recycleview_informer_linearLayout);
        layoutManager = new LinearLayoutManager(rootView.getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        complaintList= new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        //complaintList.add()

        myRef = FirebaseDatabase.getInstance().getReference("Complaints");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userID : dataSnapshot.getChildren()) {
                    for (DataSnapshot locShot : userID.getChildren()) {
                        Complaint location = locShot.getValue(Complaint.class);
                        complaintList.add(location);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new informer_complaints_list_recycleview(rootView.getContext(),complaintList);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }


}
