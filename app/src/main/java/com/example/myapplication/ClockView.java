package com.example.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ClockView extends Activity {
    Handler timerHandler;
    Bundle timerbundle;
    TimerThread thread2;
    static int b = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clock_view);


        //Intent intent = getIntent();
        //String data = intent.getStringExtra("SubMenuSelect");

        // 시간초 나오게하는 핸들러
        timerHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                timerbundle = msg.getData();

                TextView timev = (TextView)findViewById(R.id.clock);

                //Uri uri = Uri.parse(s);
                String ss = timerbundle.getString("timer");
                //Toast.makeText(getApplicationContext(), "kkk" , Toast.LENGTH_SHORT).show();


                timev.setText(ss);

                //Toast.makeText(getApplicationContext(), timerbundle.getString("timer") , Toast.LENGTH_SHORT).show();
            }
        };

        thread2 = new TimerThread();
        thread2.start();
    }

    protected void onStop(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    class TimerThread extends Thread{
        int t;
        public void run(){
            // 프로그래스바 (위와 동일)
            for(; b>=0; b--){

                t = b/1000;
                Bundle tbundle = new Bundle();
                tbundle.putString("timer", Integer.toString(t));
                Message timermsg = new Message();
                timermsg.setData(tbundle);
                timerHandler.sendMessage(timermsg);


                try{
                    Thread.sleep(1);
                }

                catch(Exception e){
                    e.printStackTrace();
                }
            }
            finish();
        }
    }

    //확인 버튼 클릭 , 액티비티 닫기
    public void mOnClose(View v){
        finish();
    }

    //액티비티 이벤트
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return false;
    }

    //백버튼 막기
    public void onBackPressed(){
        return;
    }
}