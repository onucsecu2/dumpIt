package com.a000webhostapp.trackingdaily.dumpit_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by onu on 7/27/18.
 */

public class admin_new_sweeper_admin extends Fragment {

    private DatabaseReference myRef;
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private String uid;
    private Button done;
    private Button ok;
    private EditText acode,code;
    private RadioGroup radioType;
    private RadioButton radioButton;
    private String area_code;
    private LinearLayout extend;
    private LinearLayout info;
    private LinearLayout info_process;
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    int i=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_new_sweeper_admin, container, false);
        done=(Button)rootView.findViewById(R.id.admin_done);
        ok=(Button)rootView.findViewById(R.id.ok);
        acode=(EditText)rootView.findViewById(R.id.areacode);
        code=(EditText)rootView.findViewById(R.id.code);
        radioType=(RadioGroup)rootView.findViewById(R.id.radio_type);
        extend=(LinearLayout)rootView.findViewById(R.id.code_extend);
        info=(LinearLayout)rootView.findViewById(R.id.getinfo);
        info_process=(LinearLayout)rootView.findViewById(R.id.info_process);
        animatedCircleLoadingView=rootView.findViewById(R.id.circle_loading_view);
        extend.setVisibility(View.INVISIBLE);
        code.setVisibility(View.GONE);
        info_process.setVisibility(View.GONE);
        ok.setVisibility(View.GONE);
        //layoutManager = (LinearLayoutManager) rootView.findViewById(R.id.comp_recycleview_informer_linearLayout);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        mAuth= FirebaseAuth.getInstance();


        radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radio_admin:
                        // TODO Something
                        extend.setVisibility(View.GONE);
                        code.setVisibility(View.VISIBLE);
                        i=1;
                        break;
                    case R.id.radio_sweeper:
                        // TODO Something
                        code.setVisibility(View.VISIBLE);
                        extend.setVisibility(View.VISIBLE);
                        i=2;
                        break;
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getActivity(),AdminHomeActivity.class);
                startActivity(i);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setVisibility(View.GONE);
                info_process.setVisibility(View.VISIBLE);
                animatedCircleLoadingView.startDeterminate();
                animatedCircleLoadingView.setPercent(10);
                String codetxt,areacodetxt;
                codetxt=code.getText().toString();
                animatedCircleLoadingView.setPercent(20);
                if(i==2){
                    areacodetxt=acode.getText().toString();
                    SweeperCode sweeperCode =new SweeperCode(codetxt,false,"",areacodetxt);
                    animatedCircleLoadingView.setPercent(40);
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Resource");
                    animatedCircleLoadingView.setPercent(60);
                    myRef.child("Sweepers").child(codetxt).setValue(sweeperCode);
                    animatedCircleLoadingView.setPercent(80);
                }
                else{
                    AdminCode adminCode =new AdminCode(codetxt,false,"");
                    animatedCircleLoadingView.setPercent(40);
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Resource");
                    animatedCircleLoadingView.setPercent(60);
                    myRef.child("Admins").child(codetxt).setValue(adminCode);
                    animatedCircleLoadingView.setPercent(80);
                }
                animatedCircleLoadingView.stopOk();
                ok.setVisibility(View.VISIBLE);
            }
        });




        return rootView;
    }

    private void ToastMessage(String message) {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


}
