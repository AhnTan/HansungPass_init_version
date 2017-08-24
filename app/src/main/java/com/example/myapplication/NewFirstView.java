package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewFirstView extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_first_view);

        btn = (Button)findViewById(R.id.nfv_register_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),OldFirstView.class);
                startActivity(intent);
            }
        });



        //설정버튼
        btn2 = (Button)findViewById(R.id.nfv_setting_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),Setting.class);
                startActivity(intent);
            }
        });

    }
}

