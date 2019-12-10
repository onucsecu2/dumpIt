package com.a000webhostapp.trackingdaily.dumpit_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class AdminVerificationActivity extends AppCompatActivity {

    private Button verify;
    private Button finish;
    private Button cancel;
    private EditText verifyTxt;
    private FirebaseAuthSettings firebaseAuthSettings;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CircleProgressBar circleProgressBar;
    private TextView vStatus;
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
    private String admin_str;
    private boolean profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_verification);
        Intent recievedIntent= getIntent();
        profile=recievedIntent.getBooleanExtra("profile",false);
        name = recievedIntent.getStringExtra("name");
        type="admin";
        address = recievedIntent.getStringExtra("address");
        nid = recievedIntent.getStringExtra("nid");
        phone = "+8801521227381";//recievedIntent.getStringExtra("mobile");
        ward = recievedIntent.getStringExtra("ward");
        email = recievedIntent.getStringExtra("email");
        if(!profile)
            password = recievedIntent.getStringExtra("password");
        admin_str = recievedIntent.getStringExtra("code");

        verify = (Button)findViewById(R.id.verifyButton);
        verifyTxt = (EditText) findViewById(R.id.verifyTxt);
        finish = (Button)findViewById(R.id.finishButton);
        cancel = (Button)findViewById(R.id.cancelButton);
        vStatus=(TextView)findViewById(R.id.verificationStatus);

        /*re enable other button*/

        verify.setEnabled(true);
        cancel.setEnabled(true);
        verify.getBackground().setAlpha(255);
        cancel.getBackground().setAlpha(255);

        finish.setEnabled(false);
        finish.getBackground().setAlpha(100);
        circleProgressBar=(CircleProgressBar)findViewById(R.id.mProgressbar);
        circleProgressBar.setVisibility(circleProgressBar.INVISIBLE);
        circleProgressBar.setCircleBackgroundEnabled(false);

        mAuth= FirebaseAuth.getInstance();
        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();


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
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,120,TimeUnit.SECONDS,AdminVerificationActivity.this,mCallbacks);

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = verifyTxt.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                if(!profile)
                    signInWithPhoneAuthCredential(credential);
                else{
                    saveProfile();
                }
            }
        });

        mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if(profile){
                    saveProfile();
                }
                else{
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                vStatus.setText(R.string.descript_error_verification_4);
                verify.getBackground().setAlpha(255);
                cancel.getBackground().setAlpha(255);
                verify.setEnabled(true);
                cancel.setEnabled(true);
                toastMessage("jhamela");
                circleProgressBar.setVisibility(circleProgressBar.GONE);
            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(AdminVerificationActivity.this,"onCodeSent: ",Toast.LENGTH_LONG).show();
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

    private void saveProfile() {
        String id = mAuth.getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("admin");
        Admin admin = new Admin(name, email, nid, ward, phone, address,type);
        myRef.child(id).setValue(admin);
        /*After finding the id slot as false this need to be true in the database*/
        DatabaseReference hopperRef = FirebaseDatabase.getInstance().getReference("Resource").child("Admins").child(admin_str);
        AdminCode adminCode =new AdminCode(admin_str,true,id);
        hopperRef.setValue(adminCode);
        toastMessage("updated ");

        Intent intent = new Intent(AdminVerificationActivity.this,AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void ToastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(AdminVerificationActivity.this,"successful",Toast.LENGTH_SHORT).show();

                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                String id;

                                     @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    id = mAuth.getUid();
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("admin");
                                    Admin admin = new Admin(name, email, nid, ward, phone, address,type);
                                    myRef.child(id).setValue(admin);
                                     /*After finding the id slot as false this need to be true in the database*/
                                    DatabaseReference hopperRef = FirebaseDatabase.getInstance().getReference("Resource").child("Admins").child(admin_str);
                                    AdminCode adminCode =new AdminCode(admin_str,true,id);
                                    hopperRef.setValue(adminCode);
                                    Toast.makeText(AdminVerificationActivity.this,"Succesfully Registered", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(AdminVerificationActivity.this,AdminHomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(AdminVerificationActivity.this,"successful",Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(AdminVerificationActivity.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }

                    });



// ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                vStatus.setText(R.string.descript_error_verification_2);
                            }
                        }
                    }
                });
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
