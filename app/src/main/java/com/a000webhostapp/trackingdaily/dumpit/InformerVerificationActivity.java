package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class InformerVerificationActivity extends AppCompatActivity {

    private Button verify;
    private Button finish;
    private Button cancel;
    private EditText verifyTxt;
    private FirebaseAuthSettings firebaseAuthSettings;
    private FirebaseAuth mAuth;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_informer_verification);
        Intent recievedIntent= getIntent();
        name = recievedIntent.getStringExtra("name");
        type="informer";
        address = recievedIntent.getStringExtra("address");
        nid = recievedIntent.getStringExtra("nid");
        phone = "+880 1727-946938";//"+88"+recievedIntent.getStringExtra("mobile");
        ward = recievedIntent.getStringExtra("ward");
        email = recievedIntent.getStringExtra("email");
        password = recievedIntent.getStringExtra("password");
        verify = (Button)findViewById(R.id.verifyButton);
        verifyTxt = (EditText) findViewById(R.id.verifyTxt);
        finish = (Button)findViewById(R.id.finishButton);
        cancel = (Button)findViewById(R.id.cancelButton);
        vStatus=(TextView)findViewById(R.id.verificationStatus);

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
                Toast.makeText(InformerVerificationActivity.this,"yap",Toast.LENGTH_SHORT).show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,120,TimeUnit.SECONDS,InformerVerificationActivity.this,mCallbacks);
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

        mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                Toast.makeText(InformerVerificationActivity.this,"onCodeSent: ",Toast.LENGTH_LONG).show();
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(InformerVerificationActivity.this,"successful",Toast.LENGTH_SHORT).show();

                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                String id;

                                     @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    id = mAuth.getUid();
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("informer");
                                    Informer informer = new Informer(name, email, nid, ward, phone, address,type);
                                    myRef.child(id).setValue(informer);
                                    Toast.makeText(InformerVerificationActivity.this,"Succesfully Registered", Toast.LENGTH_LONG).show();

                                } else {
                                Toast.makeText(InformerVerificationActivity.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }

                    });



                            Intent intent = new Intent(InformerVerificationActivity.this,InformerHomeActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(InformerVerificationActivity.this,"successful",Toast.LENGTH_LONG).show();
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

}
