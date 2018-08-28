package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

public class InformerRegActivity extends AppCompatActivity {
    private AutoCompleteTextView email;
    private EditText password;
    private EditText rpassword;
    private EditText name;
    private EditText nid;
    private EditText ward;
    private EditText mobile;
    private EditText address;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    String email_str,password_str,ward_str,phone_str,nid_str,address_str,name_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_informer_reg);
        email = (AutoCompleteTextView)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        rpassword=(EditText)findViewById(R.id.password_again);
        name=(EditText)findViewById(R.id.name);
        nid=(EditText)findViewById(R.id.nid);
        ward=(EditText)findViewById(R.id.ward);
        mobile=(EditText)findViewById(R.id.phone);
        address=(EditText)findViewById(R.id.addr);
        Button submit=(Button)findViewById(R.id.sign_up);

        //FirebaseAuth.getInstance().signOut();
        mAuth=FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status=attemptRegistration();


                email_str=email.getText().toString().trim();
                password_str=password.getText().toString();
                nid_str=nid.getText().toString();
                name_str=name.getText().toString();
                phone_str=mobile.getText().toString();
                ward_str=ward.getText().toString();
                address_str=address.getText().toString();

                if(status==false) {
                  /*  email_str="asd@gmail.com";
                    password_str="123456";

                    mAuth.createUserWithEmailAndPassword(email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        String id;

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //id = mAuth.getUid();
                                //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Informer");
                                //Informer informer = new Informer(name_str, email_str, nid_str, ward_str, phone_str, address_str);
                                //myRef.child(id).setValue(informer);
                                Toast.makeText(InformerRegActivity.this, "Succesfully Registered", Toast.LENGTH_LONG).show();

                            } else {
                                String err= String.valueOf(task.getException().getMessage());
                                Toast.makeText(InformerRegActivity.this, err, Toast.LENGTH_LONG).show();
                            }
                        }

                    });*/
                   Intent intent = new Intent(InformerRegActivity.this, InformerVerificationActivity.class);
                    intent.putExtra("email", email_str);
                    intent.putExtra("password", password_str);
                    intent.putExtra("name", name_str);
                    intent.putExtra("mobile", phone_str);
                    intent.putExtra("ward", ward_str);
                    intent.putExtra("address", address_str);
                    intent.putExtra("nid", nid_str);
                    startActivity(intent);
                }
            }
        });


    }

    private boolean attemptRegistration() {

        // Reset errors.
        email.setError(null);
        password.setError(null);
        rpassword.setError(null);
        name.setError(null);
        nid.setError(null);
        ward.setError(null);
        mobile.setError(null);
        address.setError(null);

        // Store values at the time of the login attempt.
        String email_str = email.getText().toString();
        String password_str = password.getText().toString();
        String rpassword_str = rpassword.getText().toString();
        String name_str = name.getText().toString();
        String nid_str = nid.getText().toString();
        String ward_str = ward.getText().toString();
        String mobile_str = mobile.getText().toString();
        String address_str = address.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password_str) && !isPasswordValid(password_str)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }
        if(!password_str.equals(rpassword_str)){
            password.setError(getString(R.string.error_mismatch_password));
            focusView = password;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email_str)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(email_str)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(nid_str)) {
            nid.setError(getString(R.string.error_field_required));
            focusView = nid;
            cancel = true;
        }
        if (TextUtils.isEmpty(mobile_str)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }
        if (TextUtils.isEmpty(ward_str)) {
            ward.setError(getString(R.string.error_field_required));
            focusView = ward;
            cancel = true;
        }
        if (TextUtils.isEmpty(name_str)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new LoginActivity.UserLoginTask(email, password);
           // mAuthTask.execute((Void) null);
        }
        return cancel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

}
