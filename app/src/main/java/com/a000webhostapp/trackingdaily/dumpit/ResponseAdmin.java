package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResponseSweeper extends AppCompatActivity {

    private String uid,type,id,time,status,rspns_date,areacode,sid;
    private int val;
    private double lat,lon;
    private ImageView aftr,bfr;
    private Button nxt,done;
    private TextView id_txt,typ_txt,rspns_status,rspns_time,comp_time;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_response_sweeper);


        id_txt=(TextView)findViewById(R.id.rspns_id);
        typ_txt=(TextView)findViewById(R.id.rspns_type);
        rspns_status=(TextView)findViewById(R.id.rspns_status);
        rspns_time=(TextView)findViewById(R.id.rspns_time_txt);
        comp_time=(TextView)findViewById(R.id.comp_time_txt);
        bfr=(ImageView)findViewById(R.id.before_rspns);
        aftr=(ImageView)findViewById(R.id.after_rspns);
        done=(Button)findViewById(R.id.button_done);
        nxt=(Button)findViewById(R.id.button_next);
        //done.setEnabled(false);

        Intent recievedIntent= getIntent();
        uid = recievedIntent.getStringExtra("uid");
        id = recievedIntent.getStringExtra("id");
        type = recievedIntent.getStringExtra("type");
        status= recievedIntent.getStringExtra("status");
        time= recievedIntent.getStringExtra("time");
        lat = recievedIntent.getDoubleExtra("lat",0);
        lon = recievedIntent.getDoubleExtra("lon",0);
        val = recievedIntent.getIntExtra("val",0);
        areacode = recievedIntent.getStringExtra("areacode");
        rspns_date= recievedIntent.getStringExtra("response_date");


        //toastMessage(id);
        id_txt.setText(id);
        typ_txt.setText(type);
        rspns_status.setText(status);
        comp_time.setText(time);


        //rspns_status.setText(status);

        mAuth= FirebaseAuth.getInstance();
        sid=mAuth.getUid();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();;
        StorageReference mStorageReference=storageReference.child(uid).child(id);

        Glide.with(ResponseSweeper.this)
                .using(new FirebaseImageLoader())
                .load(mStorageReference)
                .into(this.bfr);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
                rspns_date = df.format(Calendar.getInstance().getTime());
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");
                Complaint complaint= new Complaint(uid,type,val,lon,lat,time,status,id,rspns_date,areacode,sid);
                myRef.child(uid).child(id).setValue(complaint);
                toastMessage("successful");
                Intent intent = new Intent(ResponseSweeper.this, SweeperHomeActivity.class);
                //intent.putExtra("user", fuser);
                startActivity(intent);
            }
        });
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResponseSweeper.this, SweeperCameraActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("uid",uid);
                intent.putExtra("type",type);
                intent.putExtra("status",status);
                intent.putExtra("time",time);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                intent.putExtra("val",val);
                intent.putExtra("areacode",areacode);



                startActivity(intent);
            }
        });
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_engaging:
                if (checked){
                    done.setVisibility(View.VISIBLE);
                    nxt.setVisibility(View.INVISIBLE);
                    nxt.setEnabled(false);
                    done.setEnabled(true);
                    status="Engaging";
                }
                    // Pirates are the best
                    break;
            case R.id.radio_completed:
                if (checked){
                    nxt.setVisibility(View.VISIBLE);
                    nxt.setEnabled(true);
                    done.setEnabled(false);
                    done.setVisibility(view.GONE);
                    status="Completed";
                }
                    // Ninjas rule

                    break;
        }
    }

}
