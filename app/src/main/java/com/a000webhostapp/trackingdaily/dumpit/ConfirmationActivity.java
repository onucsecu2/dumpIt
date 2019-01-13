package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class ConfirmationActivity extends AppCompatActivity {

    private int val;
    private String str;
    private double longitude;
    private double latitude;
    private String areacode;
    private String path;
    private Uri uri;
    private Uri downloadUrl;
    String date;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private int animated_percent=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        animatedCircleLoadingView=findViewById(R.id.circle_loading_view);

        animatedCircleLoadingView.startDeterminate();
        //animatedCircleLoadingView.stopOk();

        animatedCircleLoadingView.setPercent(animated_percent);


        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();


        animatedCircleLoadingView.setPercent(20);
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        date = df.format(Calendar.getInstance().getTime());




        Intent recievedIntent= getIntent();
        val = recievedIntent.getIntExtra("val", 0);
        str = recievedIntent.getStringExtra("str");
        areacode = recievedIntent.getStringExtra("areacode");
        longitude=recievedIntent.getDoubleExtra("longitude",0);
        latitude=recievedIntent.getDoubleExtra("latitude",0);
        path=recievedIntent.getStringExtra("path");
        uri=Uri.parse(path);

        toastMessage(areacode);
        animatedCircleLoadingView.setPercent(30);
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        String locID = UUID.randomUUID().toString();
        animatedCircleLoadingView.setPercent(40);
        StorageReference mStorageReference=storageReference.child(userID).child(locID);
        animatedCircleLoadingView.setPercent(50);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");
        Complaint complaint = new Complaint(userID,str,val,longitude,latitude,date,"Pending",locID,null,areacode,null,false);
        myRef.child(userID).child(locID).setValue(complaint);

        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("CHistory");
        CHistory cHistory= new CHistory(val,longitude,latitude,date,areacode);
        myRef1.push().setValue(cHistory);

        animatedCircleLoadingView.setPercent(70);
        mStorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getUploadSessionUri();
                //toastMessage("Upload Success");
                animatedCircleLoadingView.setPercent(100);
                Intent intent = new Intent(ConfirmationActivity.this,InformerHomeActivity.class);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //toastMessage("Upload Failed");
                animatedCircleLoadingView.stopFailure();
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
