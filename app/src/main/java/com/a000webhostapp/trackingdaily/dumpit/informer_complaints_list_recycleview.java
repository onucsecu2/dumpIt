package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class informer_complaints_list_recycleview extends RecyclerView.Adapter<informer_complaints_list_recycleview.complaintsviewHolder> {

    private Context context;
    private List<Complaint>complaintList;
    private Complaint complaint;
    public informer_complaints_list_recycleview(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @Override
    public complaintsviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.complaints_card,null);
        return  new complaintsviewHolder(view);
    }

    @Override
    public void onBindViewHolder(complaintsviewHolder holder, final int position) {

             complaint = complaintList.get(position);
            holder.comp_id.setText(complaint.getId());
            holder.comp_status.setText(complaint.getStatus());
            holder.comp_rspns.setText(complaint.getRspns());
            if(complaint.getStatus().equals("Completed")){
                holder.del.setVisibility(View.VISIBLE);
                holder.claim.setVisibility(View.VISIBLE);
            }
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
            StorageReference mStorageReference=storageReference.child(complaint.getUid()).child(complaint.getId());
            Glide.with( context)
                .using(new FirebaseImageLoader())
                .load(mStorageReference)
                .into(holder.comp_img);

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Complaint comp=complaintList.get(position);

                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Complaints").child(comp.getUid()).child(comp.getId());
               // Toast.makeText(v.getContext(),comp.getId(),Toast.LENGTH_SHORT).show();

                myref.removeValue();
            }
        });
        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Complaint comp=complaintList.get(position);

                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Complaints").child(comp.getUid()).child(comp.getId());
                comp.setClaim(true);
                myref.setValue(comp);
            }
        });

        //holder.notifyAll();

    }

    @Override
    public int getItemCount() {
        //return 0;
        return complaintList.size();
    }

    class complaintsviewHolder extends RecyclerView.ViewHolder{

        ImageView comp_img;
        TextView comp_status,comp_id,comp_time,comp_rspns,comp_type;
        Button del,claim;
        public complaintsviewHolder(View itemView) {
            super(itemView);

            comp_img=itemView.findViewById(R.id.comp_img);
            comp_status=itemView.findViewById(R.id.comp_status);
            comp_id=itemView.findViewById(R.id.comp_id);
            comp_time=itemView.findViewById(R.id.comp_time);
            comp_type=itemView.findViewById(R.id.comp_type);
            comp_rspns=itemView.findViewById(R.id.comp_respns);
            del=itemView.findViewById(R.id.del);
            claim =itemView.findViewById(R.id.claim);

        }

    }

}
