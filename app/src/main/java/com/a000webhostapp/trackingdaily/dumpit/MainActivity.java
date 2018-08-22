package com.a000webhostapp.trackingdaily.dumpit;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        final CardView cardView_1=(CardView)findViewById(R.id.cardmenu_1);
        final CardView cardView_2=(CardView)findViewById(R.id.cardmenu_2);
        final CardView cardView_3=(CardView)findViewById(R.id.cardmenu_3);

        cardView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                intent.putExtra("user", "informer");
                startActivity(intent);
            }
        });
        cardView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("user", "sweeper");
                startActivity(intent);
            }
        });
        cardView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("user", "admin");
                startActivity(intent);
            }
        });
    }
}
