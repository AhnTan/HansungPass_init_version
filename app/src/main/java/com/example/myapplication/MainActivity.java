package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String host = "223.194.158.91";    // 서버 컴퓨터 IP
    //String host = "121.161.183.214";
    int port = 5001;
    //yyyyyyjjjjhhhhhjjjjj
    FirstConnectThread thread;
    Bundle bundle;
    Handler mHandler;

    long now;
    Date date;
    SimpleDateFormat sdfNow;
    String formatDate;
    TextView dateNow;

    private SharedPreferences pref;
    private TelephonyManager telephonyManager;
    private EditText Stuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) ){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE
            },466);
        }

        if((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},466);
        }

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        formatDate = sdfNow.format(date);

        dateNow = (TextView) findViewById(R.id.qr_date);

        pref = getSharedPreferences("pref" , MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        Button btn = (Button)findViewById(R.id.login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            String id = "허가";
            @Override
            public void onClick(View v) {
                telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                String phoneNum = telephonyManager.getLine1Number();
                Stuid = (EditText)findViewById(R.id.login_id_et);
                phoneNum = phoneNum.replace("+82", "0") + Stuid.getText().toString();

                if(pref.getString("Phone","").equals("")){
                    editor.putString("Phone",phoneNum);
                    editor.commit();
                    //Intent intent = new Intent(getApplicationContext(),NewFirstView.class);
                    //startActivity(intent);
                    thread = new FirstConnectThread();
                    thread.start();
                }
                else if(pref.getString("Phone","").equals(phoneNum)){
                    //Intent intent = new Intent(getApplicationContext(),NewFirstView.class);
                    //startActivity(intent);
                    thread = new FirstConnectThread();
                    thread.start();
                }
                else{
                    Toast.makeText(getApplicationContext(),"자기 핸드폰으로 로그인하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                bundle = msg.getData();

                String ss = bundle.getString("key");

                Toast.makeText(getApplicationContext(), bundle.getString("key") , Toast.LENGTH_SHORT).show();
            }
        };
    }

    class FirstConnectThread extends Thread{
        int port = 80;
        Object input;
        String output_id;
        String output_pw;
        SharedPreferences id_pref;
        SharedPreferences.Editor id_commit;
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);
        public void run(){
            //String host = "localhost";
            //String host = "223.194.158.91";
            //String host = "223.194.134.161";
            //String host = "110.70.15.167";
            //String host = "172.20.10.12";
            //String host = "223.194.156.151";
            //String host = "192.168.0.89";

            //String host = "223.194.156.151";
            String host = "113.198.84.55";

            //int port = 5001;
            //String host = "172.30.1.53";
            //int port = 8080;

            try {
                Socket socket = new Socket(host, port);
                System.out.println("서버로 연결되었습니다. : " + host + ", " + port);
                //Toast.makeText(MainActivity.this, "connect server : " + host + ", " + port , Toast.LENGTH_SHORT).show();

                EditText id = (EditText)findViewById(R.id.login_id_et);
                EditText password = (EditText)findViewById(R.id.login_pw_et);

                //String output = m_etSendData.getText().toString();
                output_id = id.getText().toString();;
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(output_id);
                outstream.flush();
                System.out.println("서버로 보낸 데이터 : " + output_id);
                //Toast.makeText(MainActivity.this, "서버로 보낸 데이터 : " + output , Toast.LENGTH_SHORT).show();

                output_pw = password.getText().toString();
                //output_pw = formatDate;
                ObjectOutputStream outstream2 = new ObjectOutputStream(socket.getOutputStream());
                outstream2.writeObject(output_pw);
                outstream2.flush();
                System.out.println("서버로 보낸 데이터 : " + output_pw);

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                //Bitmap bitmap = (Bitmap)instream.readObject();
                input = instream.readObject();
                //System.out.println(instream.readObject());
                System.out.println("서버로부터 받은 데이터: " + input);
                //Toast.makeText(getApplicationContext(), "서버로부터 받은 데이터 : " + input , Toast.LENGTH_SHORT).show();

                /*
                // 서버에서 허가를 받으면 다음 화면으로
                if(input.toString().equals("허가")){
                    Intent intent = new Intent(getApplicationContext(),NewFirstView.class);
                    startActivity(intent);
                }
                */

                if(input.toString().equals("허가")){
                    Intent intent = new Intent(getApplicationContext(), NewFirstView.class);
                    //intent.putExtra("pid", id.getText().toString());
                    String pid = id.getText().toString();
                    id_pref = getSharedPreferences("login", MODE_APPEND);
                    id_commit = id_pref.edit();
                    id_commit.putString("id", pid);
                    id_commit.commit();
                    System.out.println(">>>>>>>>>>>>>>>> : " + id_pref.getString("id", ""));
                    startActivity(intent);
                }

                else if(input.toString().equals("불허가")){
                    bundle = new Bundle();
                    bundle.putString("key", "ID와 PW를 다시 확인해주세요");
                    Message msg = new Message();
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }

                instream.close();
                outstream.close();
                socket.close();

            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("접근실패");
                return;
            }
        }
    }



}