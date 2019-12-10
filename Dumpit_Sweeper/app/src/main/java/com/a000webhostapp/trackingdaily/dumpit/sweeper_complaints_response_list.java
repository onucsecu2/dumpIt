package com.a000webhostapp.trackingdaily.dumpit;

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

public class sweeper_complaints_response_list extends Fragment {
    private RecyclerView mRecyclerView;
    sweeper_complaints_list_recycleview adapter2;
    List<Complaint> complaintList2;
    private DatabaseReference myRef;
    private DatabaseReference myRef1;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private String uid;
    private String area_code;
    int i=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sweeper_complaints_response_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.comp_response_recycleview_sweeper);

        layoutManager = new LinearLayoutManager(rootView.getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAuth= FirebaseAuth.getInstance();
        complaintList2= new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));


        //complaintList.add()
        uid=mAuth.getUid();

        myRef1 = FirebaseDatabase.getInstance().getReference("sweeper").child(uid);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sweeper sweeper=dataSnapshot.getValue(Sweeper.class);
                String ch =sweeper.getAreacode();
                area_code=ch;
                //ToastMessage(area_code);
                i++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastMessage("something is wrong");
            }
        });

       // ToastMessage(String.valueOf(i));
        //ToastMessage(achoda.get(1));
        adapter2 = new sweeper_complaints_list_recycleview(rootView.getContext(),complaintList2);

        myRef = FirebaseDatabase.getInstance().getReference("Complaints");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userID : dataSnapshot.getChildren()) {
                    for (DataSnapshot locID : userID.getChildren()) {
                        Complaint complaint = locID.getValue(Complaint.class);
                        String areacode_complaint = complaint.getAreacode();
                        String status_complaint = complaint.getStatus();
                         if(area_code.equals(areacode_complaint)) {
                           if(!status_complaint.equals("Pending")) {
                               complaintList2.add(complaint);
                               adapter2.notifyDataSetChanged();
                           }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastMessage("complaints load error");
            }
        });

        //adapter1.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter2);

        return rootView;
    }

    private void ToastMessage(String message) {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


}
