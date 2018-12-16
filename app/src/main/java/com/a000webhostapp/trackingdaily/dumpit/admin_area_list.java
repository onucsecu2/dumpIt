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

public class sweeper_complaints_list extends Fragment {
    private RecyclerView mRecyclerView;
    sweeper_complaints_list_recycleview adapter1;
    List<Complaint> complaintList1;
    private DatabaseReference myRef;
    private DatabaseReference myRef1;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private String uid;
    private Button sweeper_show_area ;
    private String area_code;
    int i=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sweeper_complaints_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.comp_recycleview_sweeper);
        sweeper_show_area=(Button)rootView.findViewById(R.id.sweeper_show_area);
        //layoutManager = (LinearLayoutManager) rootView.findViewById(R.id.comp_recycleview_informer_linearLayout);
        layoutManager = new LinearLayoutManager(rootView.getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAuth= FirebaseAuth.getInstance();
        complaintList1= new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        sweeper_show_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),MapsSweeperActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //complaintList.add()
        uid=mAuth.getUid();

        myRef1 = FirebaseDatabase.getInstance().getReference("sweeper").child(uid);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sweeper sweeper=dataSnapshot.getValue(Sweeper.class);
                String choda =sweeper.getAreacode();
                area_code=choda;
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

        myRef = FirebaseDatabase.getInstance().getReference("Complaints");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userID : dataSnapshot.getChildren()) {
                    for (DataSnapshot locID : userID.getChildren()) {
                        Complaint complaint = locID.getValue(Complaint.class);
                        String areacode_complaint = complaint.getAreacode();
                        String complaint_status = complaint.getStatus();
                         if(area_code.equals(areacode_complaint)) {
                             if(complaint_status.equals("Pending")) {
                                 complaintList1.add(complaint);
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
        adapter1 = new sweeper_complaints_list_recycleview(rootView.getContext(),complaintList1);
        //adapter1.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter1);

        return rootView;
    }

    private void ToastMessage(String message) {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


}
