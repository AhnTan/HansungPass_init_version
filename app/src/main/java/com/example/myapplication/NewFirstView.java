package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NewFirstView extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Intent settingintent;
    private RegisterDialog registerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_first_view);

        btn = (Button) findViewById(R.id.nfv_register_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickView(v);
            }
            //Intent mainintent = getIntent();
            //String pid = mainintent.getStringExtra("pid");
            // Intent intent = new Intent(getApplicationContext(), OldFirstView.class);
            //intent.putExtra("pid", pid);
            //startActivity(intent);

        });


        //설정버튼
        btn2 = (Button) findViewById(R.id.nfv_setting_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingintent = new Intent(getApplicationContext(), Setting.class);
                startActivity(settingintent);
            }
        });

    }

    public void onClickView(View v) {
        registerDialog = new RegisterDialog(this,
                "등록하기", // 제목
                "", // 내용
                leftListener, // 왼쪽 버튼 이벤트
                rightListener); // 오른쪽 버튼 이벤트
        registerDialog.show();
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),SetFPActivity.class);
            startActivity(intent);
            registerDialog.dismiss();
            SetLockActivity.FPcheck =true;
            SetLockActivity.PTcheck = false;
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),SetPatternActivity.class);
            startActivity(intent);
            SetLockActivity.FPcheck =false;
            SetLockActivity.PTcheck = true;
            registerDialog.dismiss();
        }
    };



}
