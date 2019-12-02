package com.a000webhostapp.trackingdaily.dumpit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SweeperVerificationActivity extends AppCompatActivity{

    private Button verify;
    private Button finish;
    private Button cancel;
    private EditText verifyTxt;
    private FirebaseAuthSettings firebaseAuthSettings;
    private FirebaseAuth mAuth;
    private TextView vStatus;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CircleProgressBar circleProgressBar;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String phone;
    private String name;
    private String nid;
    private String ward;
    private String email;
    private String password;
    private String address;
    private String type;
    private String sweeper_id;
    private String areacode;
    private int bool;
    private int res=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sweeper_verification);
        Intent recievedIntent= getIntent();
        name = recievedIntent.getStringExtra("name");
        type="sweeper";
        address = recievedIntent.getStringExtra("address");
        nid = recievedIntent.getStringExtra("nid");
        phone =  phone = "+880 1727-946938";//recievedIntent.getStringExtra("mobile");
        ward = recievedIntent.getStringExtra("ward");
        email = recievedIntent.getStringExtra("email");
        password = recievedIntent.getStringExtra("password");
        sweeper_id = recievedIntent.getStringExtra("sweeper");
        areacode = recievedIntent.getStringExtra("areacode");

        verify = (Button)findViewById(R.id.verifyButton_sw);
        verifyTxt = (EditText) findViewById(R.id.verifyTxt_sw);
        finish = (Button)findViewById(R.id.finishButton_sw);
        cancel = (Button)findViewById(R.id.cancelButton_sw);
        vStatus=(TextView)findViewById(R.id.verificationStatus_sw);
        circleProgressBar=(CircleProgressBar)findViewById(R.id.mProgressbar_sw);
        circleProgressBar.setVisibility(circleProgressBar.INVISIBLE);
        circleProgressBar.setCircleBackgroundEnabled(false);

        mAuth= FirebaseAuth.getInstance();
        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();


            finish.setEnabled(false);
            verify.setEnabled(true);
            cancel.setEnabled(true);
            finish.getBackground().setAlpha(100);
            verify.getBackground().setAlpha(255);
            cancel.getBackground().setAlpha(255);

        verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vStatus.setText(R.string.descript_error_verification_1);
                    mAuth.setLanguageCode("en");
                    circleProgressBar.setVisibility(circleProgressBar.VISIBLE);

                    verify.getBackground().setAlpha(100);
                    cancel.getBackground().setAlpha(100);
                    verify.setEnabled(false);
                    cancel.setEnabled(false);
                    firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phone, "123456");
                    Toast.makeText(SweeperVerificationActivity.this, "yap", Toast.LENGTH_SHORT).show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 120, TimeUnit.SECONDS, SweeperVerificationActivity.this, mCallbacks);
                }
            });

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String verificationCode = verifyTxt.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            });

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    vStatus.setText(R.string.descript_error_verification_4);
                    verify.getBackground().setAlpha(255);
                    cancel.getBackground().setAlpha(255);
                    verify.setEnabled(true);
                    cancel.setEnabled(true);
                    circleProgressBar.setVisibility(circleProgressBar.GONE);
                }

                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    //Log.d(TAG, "onCodeSent:" + verificationId);
                    Toast.makeText(SweeperVerificationActivity.this, "onCodeSent: ", Toast.LENGTH_LONG).show();
                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;
                    vStatus.setText(R.string.descript_error_verification_3);
                    circleProgressBar.setVisibility(circleProgressBar.INVISIBLE);
                    finish.getBackground().setAlpha(255);
                    verify.getBackground().setAlpha(100);
                    cancel.getBackground().setAlpha(100);

                    finish.setEnabled(true);
                    verify.setEnabled(false);
                    cancel.setEnabled(false);

                    // ...
                }

            };
    }



    private void ToastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        String id;
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                id = mAuth.getUid();



                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("sweeper");
                                Sweeper sweeper = new Sweeper(name, email, nid, ward, phone, address, type, sweeper_id, areacode);
                                myRef.child(id).setValue(sweeper);



                                DatabaseReference hopperRef = FirebaseDatabase.getInstance().getReference("Resource").child("Sweepers").child(sweeper_id);
                                SweeperCode sweeperCode =new SweeperCode(sweeper_id,true,id,areacode);
                                hopperRef.setValue(sweeperCode);

                                Intent intent = new Intent(SweeperVerificationActivity.this,SweeperHomeActivity.class);
                                startActivity(intent);
                                finish();
                                ToastMessage("Succeessful ");

                            }
                            else{
                                ToastMessage(" failed");
                            }
                        }
                    });


                }
                else{
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        ToastMessage("failure");
                    }
                }
            }
        });
    }

}
