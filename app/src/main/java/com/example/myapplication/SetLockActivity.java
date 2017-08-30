package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class SetLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);


        final Switch FPsb =(Switch)findViewById(R.id.FPsb);
        final Switch PTsb =(Switch)findViewById(R.id.PTsb);

        FPsb.setChecked(true);
        FPsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FPsb.setChecked(true);
                PTsb.setChecked(false);
            }
        });
        PTsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTsb.setChecked(true);
                FPsb.setChecked(false);
            }
        });
    }
}