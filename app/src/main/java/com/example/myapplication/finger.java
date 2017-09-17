package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;

public class finger extends AppCompatActivity {
    private ImageView img;
    private Handler mHandler;
    private Bundle bundle;
    /*
    String host = "223.194.158.91";    // 서버 컴퓨터 IP
    //String host = "121.161.183.214";
    int port = 5001;
    */
    //ConnectThread fingerthread;
    //Object input;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Intent beforeintent = getIntent();
        //String pid = beforeintent.getStringExtra("pid");

        intent = new Intent(getApplicationContext(), QRcode.class);
        //intent.putExtra("pid", pid);


        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                bundle = msg.getData();

                String ss = bundle.getString("key");
                intent.putExtra("finger", ss);

                //Toast.makeText(getApplicationContext(), bundle.getString("key") , Toast.LENGTH_SHORT).show();
            }
        };

        img = (ImageView) findViewById(R.id.imageView);
        Reprint.initialize(this);
        Reprint.authenticate(new AuthenticationListener() {
            @Override
            public void onSuccess(int moduleTag) {
                img.setImageResource(R.drawable.success);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //intent.putExtra("finger", "sss");
                startActivity(intent);
            }

            @Override
            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int moduleTag, int errorCode) {
                img.setImageResource(R.drawable.failure);
            }
        });
    }

    private boolean checkDeviceSpec() {
        boolean fingerprintFlag = Reprint.isHardwarePresent();
        boolean hasRegisteredFlag = Reprint.hasFingerprintRegistered();
        if (hasRegisteredFlag)
            img.setImageResource(R.drawable.twice);
        else
            img.setImageResource(R.drawable.failure);


        return fingerprintFlag && hasRegisteredFlag;
    }
}