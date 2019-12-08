package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AdminAreaUpdateDetailsActivity extends FragmentActivity {

    private FirebaseAuth mAuth;

    private String areacode,descript;
    private double p11,p12,p21,p22,p31,p32,p41,p42,p51,p52;
    private EditText arcd,ardesc;
    private Button done;
    //private AreaCode areaCode;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area_add);
        done=(Button)findViewById(R.id.area_done);
        arcd=(EditText)findViewById(R.id.editText);
        ardesc=(EditText)findViewById(R.id.editText2);

        progressBar=(ProgressBar)findViewById(R.id.information_upload_sw);

        Intent recievedIntent= getIntent();
        p11 = recievedIntent.getDoubleExtra("p11",0);
        p12 = recievedIntent.getDoubleExtra("p12",0);
        p21 = recievedIntent.getDoubleExtra("p21",0);
        p22 = recievedIntent.getDoubleExtra("p22",0);
        p31 = recievedIntent.getDoubleExtra("p31",0);
        p32 = recievedIntent.getDoubleExtra("p32",0);
        p41 = recievedIntent.getDoubleExtra("p41",0);
        p42 = recievedIntent.getDoubleExtra("p42",0);
        p51 = p11;
        p52 = p12;
        areacode = recievedIntent.getStringExtra("areacode");
        progressBar.setVisibility(View.GONE);
        disableEditText(arcd);


        mAuth=FirebaseAuth.getInstance();
        final String[] descript1 = new String[1];
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Area").child(areacode);


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AreaCode areaCode = dataSnapshot.getValue(AreaCode.class);
                    descript1[0] = areaCode.getDescr();
                    ardesc.setText(descript1[0]);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    toastMessage(databaseError.getMessage());
                }
            });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                descript= ardesc.getText().toString();
                DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("Area");
                toastMessage(descript);
                AreaCode areaCode = new AreaCode(areacode,descript,p11,p12,p21,p22,p31,p32,p41,p42,p51,p52);
                myRef1.child(areacode).setValue(areaCode);
                Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                startActivity(intent);
            }
        });


    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}
/*fillColor(0x550000FF));*/