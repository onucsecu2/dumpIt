package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by onu on 8/24/18.
 */

public class admin_area_recycleview extends RecyclerView.Adapter<admin_area_recycleview.areaviewHolder> {

    private Context context;
    private List<AreaCode>areaList;

    public admin_area_recycleview(Context context, List<AreaCode> areaList) {
        this.context = context;
        this.areaList = areaList;
    }

    @Override
    public areaviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.area_card,null);
        return  new areaviewHolder(view);
    }

    @Override
    public void onBindViewHolder(areaviewHolder holder, final int position) {

        AreaCode areaCode = areaList.get(position);
            holder.admin_areacode.setText(areaCode.getAreacode());
            holder.admin_area.setText(areaCode.getDescr());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AdminAreaGPS.class);
                AreaCode areaCode=areaList.get(position);
                intent.putExtra("p11", areaCode.getP11());
                intent.putExtra("p12", areaCode.getP12());
                intent.putExtra("p21", areaCode.getP21());
                intent.putExtra("p22", areaCode.getP22());
                intent.putExtra("p31", areaCode.getP31());
                intent.putExtra("p32", areaCode.getP32());
                intent.putExtra("p41", areaCode.getP41());
                intent.putExtra("p42", areaCode.getP42());
                intent.putExtra("p51", areaCode.getP51());
                intent.putExtra("p52", areaCode.getP52());
                intent.putExtra("areacode", areaCode.getAreacode());
                intent.putExtra("descript", areaCode.getDescr());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    class areaviewHolder extends RecyclerView.ViewHolder{

        TextView admin_areacode,admin_area;

        public areaviewHolder(View itemView) {
            super(itemView);

            admin_area=itemView.findViewById(R.id.admin_area);
            admin_areacode=itemView.findViewById(R.id.admin_areacode);

        }
    }

}
