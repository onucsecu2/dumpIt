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

public class admin_complaints_response_list_recycleview extends RecyclerView.Adapter<admin_complaints_response_list_recycleview.complaintsviewHolder> {

    private Context context;
    private List<Complaint>complaintList;

    public admin_complaints_response_list_recycleview(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @Override
    public complaintsviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.admin_complaints_card,null);
        return  new complaintsviewHolder(view);
    }

    @Override
    public void onBindViewHolder(complaintsviewHolder holder, final int position) {

        Complaint complaint = complaintList.get(position);
            holder.comp_id.setText(complaint.getUid());
            holder.comp_status.setText(complaint.getStatus());
            holder.comp_rspns.setText(complaint.getRspns());
            String str_date =complaint.getDate();


            String date = "";
            try {
                DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");

                Date dt1 = df.parse(str_date);

                DateFormat df2 = new SimpleDateFormat("dd MMM,yyyy HH:mm:ss a");
                date = df2.format(dt1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            holder.comp_time.setText(date);
            holder.comp_type.setText(complaint.getType());
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();;
            StorageReference mStorageReference=storageReference.child("Completed").child(complaint.getId());
            Glide.with( context)
                .using(new FirebaseImageLoader())
                .load(mStorageReference)
                .into(holder.comp_img1);
            mStorageReference=storageReference.child("Completed").child(complaint.getId());
            Glide.with( context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageReference)
                    .into(holder.comp_img2);

    }

    @Override
    public int getItemCount() {
        //return 0;
        return complaintList.size();
    }

    class complaintsviewHolder extends RecyclerView.ViewHolder{

        ImageView comp_img1,comp_img2;
        TextView comp_status,comp_id,comp_time,comp_rspns,comp_type;

        public complaintsviewHolder(View itemView) {
            super(itemView);

            comp_img1=itemView.findViewById(R.id.comp_img1);
            comp_img2=itemView.findViewById(R.id.comp_img2);
            comp_status=itemView.findViewById(R.id.comp_status);
            comp_id=itemView.findViewById(R.id.comp_id);
            comp_time=itemView.findViewById(R.id.comp_time);
            comp_type=itemView.findViewById(R.id.comp_type);
            comp_rspns=itemView.findViewById(R.id.comp_respns);


        }
    }

}
