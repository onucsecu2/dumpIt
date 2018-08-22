package com.a000webhostapp.trackingdaily.dumpit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private Button mEmailSignInButton;
    private Button mEmailSignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Intent recievedIntent= getIntent();
        final String user = recievedIntent.getStringExtra("user");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });




        mAuth = FirebaseAuth.getInstance();

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressView.setVisibility(View.VISIBLE);
                boolean flag = attemptLogin();
                if (flag) {
                    /*Toast.makeText(LoginActivity.this, "Fill up the Form", Toast.LENGTH_SHORT);
                    mEmailView.setError(getString(R.string.error_field_required));
                    mPasswordView.setError(getString(R.string.error_field_required));
                    View focusView = mEmailView;
                    focusView = mPasswordView;
                    focusView.requestFocus();*/
                    onPause();

                }
                else{
                password = mPasswordView.getText().toString();
                email = mEmailView.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    // Log.d(TAG, "signInWithEmail:success");
                                    final FirebaseUser fuser = mAuth.getCurrentUser();
                                    String uid = fuser.getUid();

                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(user).child(uid);
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override

                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Informer profile = dataSnapshot.getValue(Informer.class);

                                            if (user.equals("informer")) {
                                                Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);
                                                //  intent.putExtra("user", fuser);
                                                startActivity(intent);
                                                mProgressView.setVisibility(View.GONE);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            /*Toast.makeText(LoginActivity.this, "Authentication failed_1",
                                                    Toast.LENGTH_SHORT).show();*/
                                            mProgressView.setVisibility(View.GONE);
                                        }

                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.equalsIgnoreCase("informer")) {
                    Intent intent = new Intent(LoginActivity.this, InformerRegActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);
                    startActivity(intent);
                }
            }
        });



    }

    private boolean attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

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

