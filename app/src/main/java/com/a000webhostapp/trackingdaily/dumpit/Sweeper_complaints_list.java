package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by onu on 7/27/18.
 */

public class Sweeper_complaints_list extends Fragment{

    private Button show_area;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sweeper_complaints_list, container, false);
        show_area =(Button)rootView.findViewById(R.id.sweeper_show_area);
        show_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),MapsSweeperActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void toastMessage(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }
}
