package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class SweeperIDActivity extends AppCompatActivity {

    private EditText sweeper_id;
    private Button verify;
    private Button next;
    private Button cancel;
    private EditText verifyTxt;
    private FirebaseAuthSettings firebaseAuthSettings;
    private TextView vStatus;
    private CircleProgressBar circleProgressBar;
    private  boolean bool;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private String phone;
    private String name;
    private String nid;
    private String ward;
    private String email;
    private String password;
    private String address;
    private String areacode;
    private String type;
    private String sweeper_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sweeper_id_verify);
        //next=(Button)findViewById(R.id.verifyButton_sw_id);

        verify = (Button)findViewById(R.id.verifyButton_sw_id);
        verifyTxt = (EditText) findViewById(R.id.verifyTxt_sw_id);
        next = (Button)findViewById(R.id.finishButton_sw_id);
        cancel = (Button)findViewById(R.id.cancelButton_sw_id);
        vStatus=(TextView)findViewById(R.id.verificationStatus_sw_id);
        circleProgressBar=(CircleProgressBar)findViewById(R.id.mProgressbar_sw_id);
        circleProgressBar.setVisibility(circleProgressBar.INVISIBLE);
        circleProgressBar.setCircleBackgroundEnabled(false);



        Intent recievedIntent= getIntent();
        name = recievedIntent.getStringExtra("name");
        type="sweeper";
        address = recievedIntent.getStringExtra("address");
        nid = recievedIntent.getStringExtra("nid");
        phone = recievedIntent.getStringExtra("mobile");
        ward = recievedIntent.getStringExtra("ward");
        email = recievedIntent.getStringExtra("email");
        password = recievedIntent.getStringExtra("password");


        //FirebaseAuth.getInstance().signOut();
       // FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
       // mAuth=FirebaseAuth.getInstance();



        next.getBackground().setAlpha(100);
        next.setEnabled(false);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweeper_str=verifyTxt.getText().toString();
                vStatus.setText(R.string.descript_verify_2);
                circleProgressBar.setVisibility(circleProgressBar.VISIBLE);

                verify.getBackground().setAlpha(100);
                cancel.getBackground().setAlpha(100);
                next.getBackground().setAlpha(100);
                verify.setEnabled(false);
                cancel.setEnabled(false);
                next.setEnabled(false);

                DatabaseReference myRef2= FirebaseDatabase.getInstance().getReference("Resource").child("Sweepers").child(sweeper_str);
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            SweeperCode sweeperCode = dataSnapshot.getValue(SweeperCode.class);
                            boolean bool=sweeperCode.isBool();
                            ToastMessage(sweeper_str);
                            if(bool){
                                verify.getBackground().setAlpha(100);
                                cancel.getBackground().setAlpha(255);
                                next.getBackground().setAlpha(100);
                                verify.setEnabled(false);
                                cancel.setEnabled(true);
                                next.setEnabled(false);
                                circleProgressBar.setVisibility(circleProgressBar.GONE);
                                vStatus.setText(R.string.descript_error_verification_6);
                                //ToastMessage(" True paice");

                            }
                            else{
                                verify.getBackground().setAlpha(100);
                                cancel.getBackground().setAlpha(100);
                                next.getBackground().setAlpha(255);

                                areacode=sweeperCode.getAreacode();

                                verify.setEnabled(false);
                                cancel.setEnabled(false);
                                next.setEnabled(true);
                                circleProgressBar.setVisibility(circleProgressBar.GONE);
                                vStatus.setText("Successful!!");
                                //ToastMessage("false paice");
                            }
                          //  ToastMessage(String.valueOf(bool));
                        }
                        else{
                            verify.getBackground().setAlpha(100);
                            cancel.getBackground().setAlpha(255);
                            next.getBackground().setAlpha(100);
                            verify.setEnabled(false);
                            cancel.setEnabled(true);
                            next.setEnabled(false);
                            circleProgressBar.setVisibility(circleProgressBar.GONE);
                            vStatus.setText(R.string.descript_error_verification_6);
                          //  ToastMessage("nai nai");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        verify.getBackground().setAlpha(100);
                        cancel.getBackground().setAlpha(255);
                        next.getBackground().setAlpha(100);
                        verify.setEnabled(false);
                        cancel.setEnabled(true);
                        next.setEnabled(false);
                        circleProgressBar.setVisibility(circleProgressBar.GONE);
                        vStatus.setText(R.string.descript_error_verification_6);
                       // ToastMessage("something is not wrong");
                    }
                });



            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SweeperIDActivity.this, SweeperVerificationActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("name", name);
                intent.putExtra("mobile", phone);
                intent.putExtra("ward", ward);
                intent.putExtra("address", address);
                intent.putExtra("nid", nid);
                intent.putExtra("areacode", areacode);
                intent.putExtra("sweeper", sweeper_str);

                startActivity(intent);
                finish();

            }
        });


    }



    private void ToastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
