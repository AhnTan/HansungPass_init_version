package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class SetLockActivity extends AppCompatActivity {

    public static Boolean FPcheck;
    public static Boolean PTcheck;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);

        final Switch FPsb = (Switch) findViewById(R.id.FPsb);
        final Switch PTsb = (Switch) findViewById(R.id.PTsb);
        pref = getSharedPreferences("pref", MODE_PRIVATE); // Shared Preference를 불러옵니다.

        // 저장된 값들을 불러옵니다.
        FPcheck = pref.getBoolean("FP", true);
        PTcheck = pref.getBoolean("PT", false);

        FPsb.setChecked(FPcheck);
        PTsb.setChecked(PTcheck);

        FPsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FPsb.setChecked(true);
                PTsb.setChecked(false);
                FPcheck = true;
                PTcheck = false;
            }
        });
        PTsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTsb.setChecked(true);
                FPsb.setChecked(false);
                FPcheck = false;
                PTcheck = true;

            }
        });

    }

    public void onStop() { // 어플리케이션이 화면에서 사라질때
        super.onStop();

        pref = getSharedPreferences("pref", MODE_PRIVATE); // UI 상태를 저장합니다.
        SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다.

        // 저장할 값들을 입력합니다.
        editor.putBoolean("FP", FPcheck);
        editor.putBoolean("PT", PTcheck);

        editor.commit(); // 저장합니다.

    }
}