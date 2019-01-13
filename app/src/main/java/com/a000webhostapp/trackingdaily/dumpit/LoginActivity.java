package com.a000webhostapp.trackingdaily.dumpit;import android.content.Intent;import android.os.Bundle;import android.support.annotation.NonNull;import android.support.v7.app.AppCompatActivity;import android.text.TextUtils;import android.util.Log;import android.view.KeyEvent;import android.view.View;import android.view.View.OnClickListener;import android.view.Window;import android.view.inputmethod.EditorInfo;import android.widget.AutoCompleteTextView;import android.widget.Button;import android.widget.EditText;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import com.google.android.gms.auth.api.signin.GoogleSignIn;import com.google.android.gms.auth.api.signin.GoogleSignInAccount;import com.google.android.gms.auth.api.signin.GoogleSignInClient;import com.google.android.gms.auth.api.signin.GoogleSignInOptions;import com.google.android.gms.auth.api.signin.SignInAccount;import com.google.android.gms.common.SignInButton;import com.google.android.gms.common.api.ApiException;import com.google.android.gms.tasks.OnCompleteListener;import com.google.android.gms.tasks.Task;import com.google.firebase.auth.AuthCredential;import com.google.firebase.auth.AuthResult;import com.google.firebase.auth.FirebaseAuth;import com.google.firebase.auth.FirebaseUser;import com.google.firebase.auth.GoogleAuthProvider;import com.google.firebase.database.DataSnapshot;import com.google.firebase.database.DatabaseError;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.database.ValueEventListener;/** * A login screen that offers login via email/password. */public class LoginActivity extends AppCompatActivity {    /**     * Id to identity READ_CONTACTS permission request.     */    private static final int REQUEST_READ_CONTACTS = 0;    /**     * A dummy authentication store containing known user names and passwords.     * TODO: remove after connecting to a real authentication system.     */    private static final String[] DUMMY_CREDENTIALS = new String[]{            "foo@example.com:hello", "bar@example.com:world"    };    private static final String TAG = "Errata" ;    /**     * Keep track of the login task to ensure we can cancel it if requested.     */    // UI references.    private AutoCompleteTextView mEmailView;    private EditText mPasswordView;    private View mProgressView;    private View mLoginFormView;    private String email;    private String password;    private Button mEmailSignInButton;    private Button mEmailSignUpButton;    private SignInButton signInButton;    private String user;    private GoogleSignInClient mGoogleSignInClient;    private static int RC_SIGN_IN = 100;    private FirebaseAuth mAuth;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);        setContentView(R.layout.activity_login);        Intent recievedIntent= getIntent();        user = recievedIntent.getStringExtra("user");        // Set up the login form.        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);        mPasswordView = (EditText) findViewById(R.id.password);        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);        mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);        signInButton = findViewById(R.id.sign_in_button);        mLoginFormView = findViewById(R.id.login_form);        mProgressView =(ProgressBar)findViewById(R.id.login_progress);        mProgressView.setVisibility(View.GONE);        mAuth = FirebaseAuth.getInstance();        /*google sign in*/        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)                .requestEmail()                .requestIdToken(getString(R.string.default_web_client_id))                .requestId()                .requestProfile()                .build();        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);        //mGoogleSignInClient.signOut();        if(!user.equalsIgnoreCase("informer")) {            signInButton.setVisibility(View.GONE);        }else{            if(account!=null){            Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);            startActivity(intent);            }        }        FirebaseUser fuser = mAuth.getCurrentUser();                if (fuser!=null){            gotoAccess(fuser.getUid());            toastMessage(fuser.getEmail());        }        signInButton.setOnClickListener(new OnClickListener() {            @Override            public void onClick(View v) {                signIn();            }        });        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {            @Override            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {                    attemptLogin();                    return true;                }                return false;            }        });        mEmailSignInButton.setOnClickListener(new OnClickListener() {            @Override            public void onClick(View view) {                mProgressView.setVisibility(View.VISIBLE);                final boolean flag = attemptLogin();                if (flag) {                    onPause();                }                else{                password = mPasswordView.getText().toString();                email = mEmailView.getText().toString();                mAuth.signInWithEmailAndPassword(email, password)                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {                            @Override                            public void onComplete(@NonNull Task<AuthResult> task) {                                if (task.isSuccessful()) {                                    mProgressView.setVisibility(View.VISIBLE);                                    // Sign in success, update UI with the signed-in user's information                                    // Log.d(TAG, "signInWithEmail:success");                                    final FirebaseUser fuser = mAuth.getCurrentUser();                                    String uid = fuser.getUid();                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(user).child(uid);                                    myRef.addValueEventListener(new ValueEventListener() {                                        @Override                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                                            if(dataSnapshot.exists()){                                                if (user.equals("informer")) {                                                    Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);                                                    startActivity(intent);                                                    finish();                                                }                                                else if (user.equals("sweeper")) {                                                    Intent intent = new Intent(LoginActivity.this, SweeperHomeActivity.class);                                                    startActivity(intent);                                                    finish();                                                }                                                else if (user.equals("admin")) {                                                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);                                                    startActivity(intent);                                                    finish();                                                }                                            }else{                                                mAuth.signOut();                                                toastMessage("Access denied");                                            }                                        }                                        @Override                                        public void onCancelled(@NonNull DatabaseError databaseError) {                                            Toast.makeText(LoginActivity.this, "Authentication failed_1",                                                    Toast.LENGTH_SHORT).show();                                            mProgressView.setVisibility(View.GONE);                                        }                                    });                                } else {                                    // If sign in fails, display a message to the user.                                    Log.w(TAG, "signInWithEmail:failure", task.getException());                                    mProgressView.setVisibility(View.GONE);                                    Toast.makeText(LoginActivity.this, "Authentication failed",                                            Toast.LENGTH_SHORT).show();                                }                                // ...                            }                        });            }            }        });        mEmailSignUpButton.setOnClickListener(new OnClickListener() {            @Override            public void onClick(View v) {                if(user.equalsIgnoreCase("informer")) {                    Intent intent = new Intent(LoginActivity.this, InformerRegActivity.class);                    startActivity(intent);                    finish();                }                else if(user.equalsIgnoreCase("sweeper")) {                    Intent intent = new Intent(LoginActivity.this, SweeperRegActivity.class);                    startActivity(intent);                    finish();                }                else{                    Intent intent = new Intent(LoginActivity.this,AdminRegActivity.class);                    startActivity(intent);                    finish();                }            }        });    }    private void gotoAccess(String uid) {        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(user).child(uid);        myRef.addValueEventListener(new ValueEventListener() {            @Override            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                if(dataSnapshot.exists()){                    if (user.equals("informer")) {                        Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);                        startActivity(intent);                        finish();                    }                    else if (user.equals("sweeper")) {                        Intent intent = new Intent(LoginActivity.this, SweeperHomeActivity.class);                        startActivity(intent);                        finish();                    }                    else if (user.equals("admin")) {                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);                        startActivity(intent);                        finish();                    }                }else{                    mAuth.signOut();                    toastMessage("Access denied");                }            }            @Override            public void onCancelled(@NonNull DatabaseError databaseError) {                Toast.makeText(LoginActivity.this, "Authentication failed_1",                        Toast.LENGTH_SHORT).show();                mProgressView.setVisibility(View.GONE);            }        });    }    private void signIn() {        Intent signInIntent = mGoogleSignInClient.getSignInIntent();        startActivityForResult(signInIntent, RC_SIGN_IN);    }    private void updateUI(final FirebaseUser account) {        //toastMessage(account.getEmail().toString());        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("informer").child(account.getUid());        myRef.addListenerForSingleValueEvent(new ValueEventListener() {            @Override            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                if(dataSnapshot.exists()) {                    Intent intent = new Intent(LoginActivity.this, InformerHomeActivity.class);                    startActivity(intent);                    finish();                }            }            @Override            public void onCancelled(@NonNull DatabaseError databaseError) {            }        });        if(user.equalsIgnoreCase("informer")) {            Intent intent = new Intent(LoginActivity.this, InformerRegActivity.class);            intent.putExtra("email", account.getEmail().toString());            intent.putExtra("name", account.getDisplayName().toString());            intent.putExtra("uid", account.getUid().toString());            startActivity(intent);        }        else if(user.equalsIgnoreCase("sweeper")) {            Intent intent = new Intent(LoginActivity.this, SweeperRegActivity.class);            intent.putExtra("email", account.getEmail().toString());            intent.putExtra("name", account.getDisplayName().toString());            startActivity(intent);        }        else{            Intent intent = new Intent(LoginActivity.this,AdminRegActivity.class);            intent.putExtra("email", account.getEmail().toString());            intent.putExtra("name", account.getDisplayName().toString());            startActivity(intent);        }    }    @Override    public void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        if (requestCode == RC_SIGN_IN) {            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);            try {                GoogleSignInAccount account = task.getResult(ApiException.class);                firebaseAuthWithGoogle(account);            } catch (ApiException e) {                toastMessage("Signin failed");            }        }    }    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);        mAuth.signInWithCredential(credential)                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {                    @Override                    public void onComplete(@NonNull Task<AuthResult> task) {                        if (task.isSuccessful()) {                            // Sign in success, update UI with the signed-in user's information                            Log.d(TAG, "signInWithCredential:success");                            FirebaseUser user = mAuth.getCurrentUser();                            updateUI(user);                        } else {                            // If sign in fails, display a message to the user.                            Log.w(TAG, "signInWithCredential:failure", task.getException());                            //updateUI(null);                        }                        // ...                    }                });    }    private boolean attemptLogin() {        // Reset errors.        mEmailView.setError(null);        mPasswordView.setError(null);        // Store values at the time of the login attempt.        String email = mEmailView.getText().toString();        String password = mPasswordView.getText().toString();        boolean cancel = false;        View focusView = null;        // Check for a valid password, if the user entered one.        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {            mPasswordView.setError(getString(R.string.error_invalid_password));            focusView = mPasswordView;            cancel = true;        }        // Check for a valid email address.        if (TextUtils.isEmpty(email)) {            mEmailView.setError(getString(R.string.error_field_required));            focusView = mEmailView;            cancel = true;        } else if (!isEmailValid(email)) {            mEmailView.setError(getString(R.string.error_invalid_email));            focusView = mEmailView;            cancel = true;        }        if (cancel) {            // There was an error; don't attempt login and focus the first            // form field with an error.            focusView.requestFocus();        } else {        }        return cancel;    }    private boolean isEmailValid(String email) {        //TODO: Replace this with your own logic        return email.contains("@");    }    private boolean isPasswordValid(String password) {        //TODO: Replace this with your own logic        return password.length() > 5;    }    private void toastMessage(String message){        Toast.makeText(this,message,Toast.LENGTH_LONG).show();    }}