package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


        Intent beforeintent = getIntent();
        String pid = beforeintent.getStringExtra("pid");

        intent = new Intent(getApplicationContext(), QRcode.class);
        intent.putExtra("pid", pid);
        Log.d("NewFirstView", pid);


        mHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                bundle = msg.getData();

                String ss = bundle.getString("key");
                intent.putExtra("finger", ss);

                //Toast.makeText(getApplicationContext(), bundle.getString("key") , Toast.LENGTH_SHORT).show();
            }
        };

        img = (ImageView)findViewById(R.id.imageView);
        Reprint.initialize(this);
        Reprint.authenticate(new AuthenticationListener() {
            @Override
            public void onSuccess(int moduleTag) {
                img.setImageResource(R.drawable.success);
                try{
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
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

    /*
    class ConnectThread extends Thread{
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);
        public void run(){
            //String host = "localhost";
            //String host = "223.194.158.91";
            int port = 5001;
            //String host = "172.30.1.53";
            //int port = 8080;
            try {
                Socket socket = new Socket(host, port);
                System.out.println("서버로 연결되었습니다. : " + host + ", " + port);
                //Toast.makeText(MainActivity.this, "connect server : " + host + ", " + port , Toast.LENGTH_SHORT).show();
                //String output = m_etSendData.getText().toString();
                String output = "send";
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(output);
                outstream.flush();
                System.out.println("서버로 보낸 데이터 : " + output);
                //Toast.makeText(MainActivity.this, "서버로 보낸 데이터 : " + output , Toast.LENGTH_SHORT).show();
                String output2 = "send2";
                ObjectOutputStream outstream2 = new ObjectOutputStream(socket.getOutputStream());
                outstream2.writeObject(output);
                outstream2.flush();
                System.out.println("서버로 보낸 데이터 : " + output2);
                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                //Bitmap bitmap = (Bitmap)instream.readObject();
                input = instream.readObject();
                //System.out.println(instream.readObject());
                System.out.println("서버로부터 받은 데이터: " + input);
                //Toast.makeText(getApplicationContext(), "서버로부터 받은 데이터 : " + input , Toast.LENGTH_SHORT).show();
                /*
                String s = input.toString();
                WebView imgv = (WebView)findViewById(R.id.img);
                //Uri uri = Uri.parse(s);
                //imgv.loadUrl(s);
                // 서버에서 받은 데이터(QR코드)를 번들을 통해 핸들러 메세지로 전달
                Bundle bundle = new Bundle();
                bundle.putString("key", input.toString());
                Message msg = new Message();
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                /*
                // 프로그래스바 (위와 동일)
                for(int k=0; k<=3000; k++){
                    progressBar.setProgress(k);
                    Bundle tbundle = new Bundle();
                    tbundle.putString("timer", Integer.toString(k));
                    Message timermsg = new Message();
                    timermsg.setData(tbundle);
                    timerHandler.sendMessage(timermsg);
                    //Message timermsg = timerHandler.obtainMessage();
                    //timermsg.arg1 = k;
                    try{
                        Thread.sleep(1);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                instream.close();
                outstream.close();
                socket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("접근실패");
            }
        }
    }
*/
    private boolean checkDeviceSpec(){
        boolean fingerprintFlag = Reprint.isHardwarePresent();
        boolean hasRegisteredFlag = Reprint.hasFingerprintRegistered();
        if(hasRegisteredFlag)
            img.setImageResource(R.drawable.success);
        else
            img.setImageResource(R.drawable.failure);


        return fingerprintFlag && hasRegisteredFlag;
    }
}