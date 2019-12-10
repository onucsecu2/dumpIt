package com.a000webhostapp.trackingdaily.dumpit_admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminAreaRegisterActivity extends AppCompatActivity {
    private Button done;


    private ProgressBar progressBar;

    private double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
    private FirebaseAuth mAuth;
    private EditText arcd,ardesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area_add);
        done=(Button)findViewById(R.id.area_done);
        arcd=(EditText)findViewById(R.id.editText);
        ardesc=(EditText)findViewById(R.id.editText2);

        progressBar=(ProgressBar)findViewById(R.id.information_upload_sw);



        mAuth=FirebaseAuth.getInstance();
        Intent recievedIntent= getIntent();
        p11=recievedIntent.getDoubleExtra("p11",0);
        p12=recievedIntent.getDoubleExtra("p12",0);
        p21=recievedIntent.getDoubleExtra("p21",0);
        p22=recievedIntent.getDoubleExtra("p22",0);
        p31=recievedIntent.getDoubleExtra("p31",0);
        p32=recievedIntent.getDoubleExtra("p32",0);
        p41=recievedIntent.getDoubleExtra("p41",0);
        p42=recievedIntent.getDoubleExtra("p42",0);
        p51=p11;
        p52=p12;




/* A lots of things to be editted  */
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String areacode= String.valueOf(arcd.getText());
                String areadesc = String.valueOf(ardesc.getText());
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Area");
                AreaCode areaCode = new AreaCode(areacode,areadesc,p11,p12,p21,p22,p31,p32,p41,p42,p51,p52);
                myRef.child(areacode).setValue(areaCode);
                toastMessage("successful");

                Intent intent = new Intent(AdminAreaRegisterActivity.this, AdminHomeActivity.class);

                startActivity(intent);
            }
        });




    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
