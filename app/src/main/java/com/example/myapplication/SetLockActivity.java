package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

public class SetLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);


        final Switch FPsb =(Switch)findViewById(R.id.FPsb);
        //  final  Switch PTsb =(Switch)findViewById(R.id.PTsb);




/*     패턴할때 짰던코드
       final ToggleButton FPtb=(ToggleButton)findViewById(R.id.FPtb);
       final ToggleButton PTtb=(ToggleButton)findViewById(R.id.PTtb);
        final Switch FPsb = (Switch)findViewById(R.id.switch1);
        FPtb.setChecked(true);
        FPtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FPtb.setChecked(true);
                PTtb.setChecked(false);
            }
        });
        PTtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTtb.setChecked(true);
                FPtb.setChecked(false);
            }
        });*/
    }
}