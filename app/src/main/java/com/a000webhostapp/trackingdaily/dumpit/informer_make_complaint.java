package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by onu on 7/27/18.
 */

public class informer_make_complaint extends Fragment{

    private int val=0;
    private Button next;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.informer_make_complaint, container, false);
        next= (Button)rootView.findViewById(R.id.informer_next_1);
        //next.setEnabled(false);
        //next.getBackground().setAlpha(80);
        Spinner complaint_type = rootView.findViewById(R.id.spinner_complaint_type);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.complaint_type, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complaint_type.setAdapter(staticAdapter);




        complaint_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selected = parent.getItemAtPosition(position).toString();
                /*Context context = parent.getContext();
                CharSequence text = selected;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/
                switch (position){
                    case 0:
                        val = 0;
                        next.setEnabled(false);
                        next.getBackground().setAlpha(80);
                        //bk.setText(getResources().getString(R.string.Allen));
                        break;
                    case 1:
                       // bk.setText(getResources().getString(R.string.Thomas));
                        next.setEnabled(true);
                        next.getBackground().setAlpha(255);
                        val = 1;
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(getActivity(),MapsActivity.class);
                                intent.putExtra("val",val);
                                intent.putExtra("tog",0);
                                intent.putExtra("str",selected);
                                startActivity(intent);

                            }
                        });
                        break;
                    case 2:
                        val = 2;
                        next.setEnabled(true);
                        next.getBackground().setAlpha(255);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(getActivity(),MapsActivity.class);
                                intent.putExtra("val",val);
                                intent.putExtra("tog",0);
                                intent.putExtra("str",selected);
                                startActivity(intent);

                            }
                        });
                        break;
                    case 3:
                        val = 3;
                        next.setEnabled(true);
                        next.getBackground().setAlpha(255);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(getActivity(),MapsActivity.class);
                                intent.putExtra("val",val);
                                intent.putExtra("tog",0);
                                intent.putExtra("str",selected);
                                startActivity(intent);

                            }
                        });
                        break;
                    case 4:
                        val = 4;
                        next.setEnabled(true);
                        next.getBackground().setAlpha(255);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(getActivity(),MapsActivity.class);
                                intent.putExtra("val",val);
                                intent.putExtra("tog",0);
                                intent.putExtra("str",selected);
                                startActivity(intent);
                            }
                        });
                        break;
                    case 5:
                        val = 5;
                        next.setEnabled(true);
                        next.getBackground().setAlpha(255);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(getActivity(),MapsActivity.class);
                                intent.putExtra("val",val);
                                intent.putExtra("tog",0);
                                intent.putExtra("str",selected);
                                startActivity(intent);

                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Toast.makeText(getActivity(),String.valueOf(Case),Toast.LENGTH_SHORT);



        return rootView;
    }

}
