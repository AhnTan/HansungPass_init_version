package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    String host = "223.194.158.91";    // 서버 컴퓨터 IP
    int port = 5001;
    private FirstConnectThread thread;
    private Bundle bundle;
    private Handler mHandler;
    private Button btn;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            String id = "허가";
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),NewFirstView.class);
                startActivity(intent);
                //thread = new FirstConnectThread();
                //thread.start();
                /*
                Log.d("kkk", "아 왜안돼");
                */

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
        int port = 5001;
        Object input;
        String output_id;
        String output_pw;
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);
        public void run(){
            //String host = "localhost";
            //String host = "223.194.158.91";
            //String host = "223.194.134.161";
            //int port = 5001;
            String host = "172.30.1.53";
            //int port = 8080;

            try {
                Socket socket = new Socket(host, port);
                System.out.println("서버로 연결되었습니다. : " + host + ", " + port);
                //Toast.makeText(MainActivity.this, "connect server : " + host + ", " + port , Toast.LENGTH_SHORT).show();

                EditText id = (EditText)findViewById(R.id.login_id_et);
                EditText password = (EditText)findViewById(R.id.login_pw_et);

                //String output = m_etSendData.getText().toString();
                output_id = id.getText().toString();
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(output_id);
                outstream.flush();
                System.out.println("서버로 보낸 데이터 : " + output_id);
                //Toast.makeText(MainActivity.this, "서버로 보낸 데이터 : " + output , Toast.LENGTH_SHORT).show();

                output_pw = password.getText().toString();
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
                // 서버에서 허가를 받으면 다음 화면으로
                if(input.toString().equals("허가")){
                    intent = new Intent(getApplicationContext(),NewFirstView.class);
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
            }
        }
    }
}
