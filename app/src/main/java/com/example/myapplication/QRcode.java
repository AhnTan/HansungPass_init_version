package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRcode extends FragmentActivity {
    String host = "223.194.134.161";    // 서버 컴퓨터 IP
    //String host = "121.161.183.214";
    int port = 5001;
    private Handler mHandler;
    private Handler timerHandler;
    private Handler stopHandler;
    private Handler ontimeHandler;
    private Bundle bundle;
    private Bundle timerbundle;
    private ConnectThread thread;
    private TimerThread thread2;
    //private pausetimer thread3;
    private onTimeThread thread4;
    static int k = 3000;
    private long now;
    private Date date;
    private SimpleDateFormat sdfNow;
    private String formatDate;
    private String qr_id;
    private String md5;
    private TextView dateNow;
    private TextView dateNow_b;
    private TextView timev;
    private ImageButton ibtn;
    private SharedPreferences se;
    int reset_btn_count;
    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qrcode);

        //se = getSharedPreferences("id", 0);
        //스샷막아주는 코드
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        reset_btn_count=0;

        backPressCloseHandler = new BackPressCloseHandler(this); //뒤로버튼 종료

        thread = new ConnectThread();
        thread.start();


        timev = (TextView)findViewById(R.id.qr_timer_t);
        ibtn = (ImageButton)findViewById(R.id.qr_time);
        dateNow = (TextView) findViewById(R.id.qr_date);
        dateNow_b = (TextView) findViewById(R.id.qr_date_b);


        //서버에서 받은 QR코드 url을 핸들러를 통해 웹뷰에 붙여줌
        mHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                bundle = msg.getData();
                String qr = bundle.getString("qr_key");
                String user_id = bundle.getString("id_key");
                String user_name = bundle.getString("name_key");

                String[] ReturnList = qr.split("%3B%3B");
                qr_id = ReturnList[0] + "%3B%3B";
                md5 = ReturnList[1];
                Log.d("aabbccdd", qr_id);
                Log.d("bbddee", md5);

                TextView name_tv = (TextView)findViewById(R.id.qr_stu_name);
                name_tv.setText(user_name);
                TextView id_tv = (TextView)findViewById(R.id.qr_stu_id);
                id_tv.setText(user_id);

                // QRCodeWriter 라이브러리
                QRCodeWriter qrCodeWriter = new QRCodeWriter();

                try {
                    Bitmap bitmap = toBitmap(qrCodeWriter.encode(qr, BarcodeFormat.QR_CODE, 250, 250));
                    ImageView qr_code_view = (ImageView) findViewById(R.id.qr_qrcode_img);
                    qr_code_view.setImageBitmap(bitmap);
                    qr_code_view.setVisibility(View.VISIBLE);
                    qr_code_view.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Toast.makeText(getApplicationContext(), bundle.getString("key") , Toast.LENGTH_SHORT).show();
            }
        };

        stopHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                bundle = msg.getData();
                String qr = bundle.getString("qr_key");
                String user_id = bundle.getString("id_key");
                String user_name = bundle.getString("name_key");

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    ImageView qr_code_view = (ImageView) findViewById(R.id.qr_qrcode_img);
                    qr_code_view.setVisibility(View.INVISIBLE);
                    qr_code_view.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Toast.makeText(getApplicationContext(), bundle.getString("key") , Toast.LENGTH_SHORT).show();
            }
        };

        ontimeHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                bundle = msg.getData();
                //Uri uri = Uri.parse(s);
                String a = bundle.getString("year");
                String b = bundle.getString("sec");

                //int timess = ontimebundle.getInt("timer");
                //Toast.makeText(getApplicationContext(), "kkk" , Toast.LENGTH_SHORT).show();
                //timev.setText(Integer.toString(timess));


                dateNow.setText(a);
                dateNow_b.setText(b);
            }
        };

        // 시간초 나오게하는 핸들러
        timerHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                timerbundle = msg.getData();
                //Uri uri = Uri.parse(s);
                int timess = timerbundle.getInt("timer");
                //Toast.makeText(getApplicationContext(), "kkk" , Toast.LENGTH_SHORT).show();
                timev.setText(Integer.toString(timess));

                if(timess < 15){
                    //timev.setVisibility(View.INVISIBLE);
                    if(reset_btn_count==0) {
                        ibtn.setVisibility(View.VISIBLE);
                    }
                }else{
                    timev.setVisibility(View.VISIBLE);
                    ibtn.setVisibility(View.INVISIBLE);
                }

                //Toast.makeText(getApplicationContext(), timerbundle.getString("timer") , Toast.LENGTH_SHORT).show();
            }
        };


        /*
        thread3 = new pausetimer();
        thread3.setDaemon(true);
        thread3.start();
        */
        thread2 = new TimerThread();
        thread2.setDaemon(true);
        thread2.start();

        thread4 = new onTimeThread();
        thread4.setDaemon(true);
        thread4.start();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

/*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),OldFirstView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        thread.interrupt();
        thread2.interrupt();
        thread4.interrupt();

        //thread.setDaemon(true);
        //thread2.setDaemon(true);
        //thread4.setDaemon(true);
        startActivity(intent);
        super.onBackPressed();
    }*/

    // 새로운 QR코드를 받고싶을때 버튼 이벤트
    public void onButtonClicked(View v){

        reset_btn_count++;

            k=0;

            timev = (TextView)findViewById(R.id.qr_timer_t);
            ibtn = (ImageButton)findViewById(R.id.qr_time);
            ibtn.setVisibility(View.INVISIBLE);
            timev.setVisibility(View.VISIBLE);

            thread2.interrupt();
            thread4.interrupt();
            thread = new ConnectThread();
            thread.start();
            thread2 = new TimerThread();
            thread2.setDaemon(true);
            thread2.start();


    }

    class onTimeThread extends Thread{
        public void run() {
            while(true) {
                // 현재시간을 msec 으로 구한다.
                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                formatDate = sdfNow.format(date);
                String[] nowtimer = formatDate.split(" ");
                String nowtimer_a = nowtimer[0];
                String nowtimer_b = nowtimer[1];

                Bundle ontimebundle = new Bundle();
                ontimebundle.putString("year", nowtimer_a);
                ontimebundle.putString("sec", nowtimer_b);
                Message timermsg = new Message();
                timermsg.setData(ontimebundle);
                ontimeHandler.sendMessage(timermsg);
            }
        }
    }


    class TimerThread extends Thread{
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);

        public void run(){
            // 프로그래스바 (위와 동일)

            k=30;
            //시간 지날때마다 진동
            final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

            for(; k>=0; k--){
                progressBar.setProgress(k);

                Bundle tbundle = new Bundle();
                tbundle.putInt("timer", k);
                Message timermsg = new Message();
                timermsg.setData(tbundle);
                timerHandler.sendMessage(timermsg);
                vibrator.vibrate(100);

                try{
                    Thread.sleep(998);
                }

                catch(Exception e){
                    e.printStackTrace();
                    return;
                }
            }
            Bundle stopbundle = new Bundle();
            stopbundle.putString("timer", "ee");
            Message stopmsg = new Message();
            stopmsg.setData(stopbundle);
            stopHandler.sendMessage(stopmsg);
        }
    }

    // 쓰레드 (서버연결 및 프로그래스바) - 현재는 임시로 한 쓰레드에 그냥 넣어둠
    class ConnectThread extends Thread{
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);
        public void run(){
            String host = "113.198.84.55";
            int port = 80;

            try {
                Socket socket = new Socket(host, port);
                System.out.println("서버로 연결되었습니다. : " + host + ", " + port);

                se = getSharedPreferences("login", MODE_APPEND);
                String ppid = se.getString("id", "");

                System.out.println(">>>>>>>>>>>>>>>> : " + ppid);
                String output = "send";

                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(output);
                outstream.flush();
                System.out.println("서버로 보낸 데이터 : " + output);

                String output2 = ppid + "%3B%3B" + formatDate ;
                ObjectOutputStream outstream2 = new ObjectOutputStream(socket.getOutputStream());
                outstream2.writeObject(output2);
                outstream2.flush();
                System.out.println("서버로 보낸 데이터 : " + output2);


                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                Object input = instream.readObject();
                System.out.println("서버로부터 받은 QR데이터: " + input);

                ObjectInputStream instream2 = new ObjectInputStream(socket.getInputStream());
                Object input2 = instream2.readObject();
                System.out.println("서버로부터 받은 학번 데이터: " + input2);

                ObjectInputStream instream3 = new ObjectInputStream(socket.getInputStream());
                Object input3 = instream3.readObject();
                System.out.println("서버로부터 받은 이름 데이터: " + input3);


                // 서버에서 받은 데이터(QR코드)를 번들을 통해 핸들러 메세지로 전달
                Bundle bundle = new Bundle();
                bundle.putString("qr_key", input.toString());
                bundle.putString("id_key", input2.toString());
                bundle.putString("name_key", input3.toString());
                Message msg = new Message();
                msg.setData(bundle);
                mHandler.sendMessage(msg);


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

    public static Bitmap toBitmap(BitMatrix martrix){
        int height = martrix.getHeight();
        int width = martrix.getWidth();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                bmp.setPixel(i,j,martrix.get(i,j) ? Color.BLACK : Color.alpha(1));
            }
        }
        return bmp;
    }
}