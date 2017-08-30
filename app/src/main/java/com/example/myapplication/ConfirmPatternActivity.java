package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.util.PatternLockUtils;
import com.example.myapplication.util.PreferenceContract;
import com.example.myapplication.util.PreferenceUtils;
import com.example.myapplication.util.ThemeUtils;

import java.util.List;

import me.zhanghai.android.patternlock.PatternView;

public class ConfirmPatternActivity extends me.zhanghai.android.patternlock.ConfirmPatternActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isStealthModeEnabled() {
        return !PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, PreferenceContract.DEFAULT_PATTERN_VISIBLE, this);
    }


    @Override
    public boolean isPatternCorrect(List<PatternView.Cell> pattern) {

        return PatternLockUtils.isPatternCorrect(pattern, this);
    }






    /*
    @Override
    protected void onForgotPassword() {

        startActivity(new Intent(this, ResetPatternActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }
    */
}
