package com.a000webhostapp.trackingdaily.dumpit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gao.jiefly.abilitychartlibrary.AbilityChatView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompStatActivity extends AppCompatActivity {

    private AbilityChatView mAbility1;
    private double val1=0,val2=0,val3=0,val4=0,val5=0,tot=0;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_stat);
        mAbility1=(AbilityChatView)findViewById(R.id.ability);

        mAuth=FirebaseAuth.getInstance();

        mAbility1.setCount(5);
        //mAbility1.changeTitles(getResources().getStringArray(R.array.complaint_type));
        mAbility1.changeTitles(new String[]{"ম্যানহোল ঢাকনা নেই/মেরামত","ড্রেন জ্যাম","ময়লার স্তুপ", "ডাস্টবিন/বিন উপচিয়ে পড়ছে", "বিন পরিপুর্ণ"});
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CHistory");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ch:dataSnapshot.getChildren()){
                    if(ch.getValue(CHistory.class).getVal()==1){
                        val1++;
                    }else if(ch.getValue(CHistory.class).getVal()==2){
                        val2++;
                    }else if(ch.getValue(CHistory.class).getVal()==3){
                        val3++;
                    }else if(ch.getValue(CHistory.class).getVal()==4){
                        val4++;
                    }else if(ch.getValue(CHistory.class).getVal()==5){
                        val5++;
                    }

                }
                tot=val1+val2+val3+val4+val5;
                List<Double> data = new ArrayList<>();
                data.add((val1/tot)*100);
                data.add((val2/tot)*100);
                data.add((val3/tot)*100);
                data.add((val4/tot)*100);
                data.add((val5/tot)*100);

                mAbility1.setData(data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
