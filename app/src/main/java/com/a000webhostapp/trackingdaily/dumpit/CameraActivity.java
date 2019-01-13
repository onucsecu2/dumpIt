package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuhart.stepview.StepView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "error: ";
    private int val;
    private String str;
    private double longitude;
    private double latitude;
    private Button next2;
    private Button capture;
    private ImageView capture_cam;
    private static final int CAM_REQUEST=1313;
    private ProgressBar progressBar;

    private String pathFile;
    private Uri uri;
    private Uri downloadUrl;
    private String areacode;
    private String path;
    String date;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private StepView stepView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        next2=(Button)findViewById(R.id.informer_next_3);
        capture=(Button)findViewById(R.id.capture_button);
        capture_cam=(ImageView)findViewById(R.id.image_capture);
        progressBar=(ProgressBar)findViewById(R.id.information_upload);
        stepView=(StepView)findViewById(R.id.step_view);
        stepView.setStepsNumber(3);

        stepView.go(2,true);

        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        Intent recievedIntent= getIntent();
        val = recievedIntent.getIntExtra("val",0);
        str = recievedIntent.getStringExtra("str");
        areacode = recievedIntent.getStringExtra("areacode");
        longitude=recievedIntent.getDoubleExtra("longitude",0);
        latitude=recievedIntent.getDoubleExtra("latitude",0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CheckFilePermissions();
        }


        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        date = df.format(Calendar.getInstance().getTime());



        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

               /* FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                String locID = UUID.randomUUID().toString();
                StorageReference mStorageReference=storageReference.child(userID).child(locID);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");
                Complaint complaint = new Complaint(userID,str,val,longitude,latitude,date,"Pending",locID,null,areacode,null);


                // it will create a random unique string por each compliants.it will be considered as Complaints id

               myRef.child(userID).child(locID).setValue(complaint);
               mStorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getUploadSessionUri();
                        toastMessage("Upload Success");
                        progressBar.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastMessage("Upload Failed");
                        progressBar.setVisibility(View.GONE);
                    }
                });*/
                Intent intent = new Intent(CameraActivity.this,ConfirmationActivity.class);
                intent.putExtra("val",val);
                intent.putExtra("str",str);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("areacode",areacode);
                intent.putExtra("path",uri.toString());
                startActivity(intent);


            }
        });
        capture.setOnClickListener(new btnTakePhotoClicker());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckFilePermissions() {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = CameraActivity.this.checkSelfPermission("android.Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck+= CameraActivity.this.checkSelfPermission("android.Manifest.permission.WRITE_EXTERNAL_STORAGE");

            if(permissionCheck!=0){
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }else{
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            capture_cam.setImageBitmap(bitmap);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            pathFile= getRealPathFromURI(tempUri);
            uri=tempUri;
            stepView.done(true);
        }

    }
    private class btnTakePhotoClicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
