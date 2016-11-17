package com.example.home_pc.thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private int seconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.textView);
        Date theLastDay = new Date(117,5,23);
        Date toDay = new Date();
        seconds = (int)(theLastDay.getTime()-toDay.getTime())/1000;
    }
    public void anr(View v){
        for (int i = 0;i<100000;i++){
            BitmapFactory.decodeResource(getResources(),R.drawable.android);
        }
    }
    public void threadclass(View v){
        class ThreadSample extends Thread{
            Random rm;
            public ThreadSample(String tname){
                super(tname);
                rm = new Random();
            }
            public void run(){
                for(int i = 0;i<10;i++){
                    System.out.println(i+" "+getName());
                    try {
                        sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("普通线程"+getName()+"完成");
            }
        }
        ThreadSample td1 = new ThreadSample("线程一");
        ThreadSample td2 = new ThreadSample("线程二");
        td1.start();
        td2.start();
    }
    public void runnableinterdace(View v){
        class RunnableExample implements  Runnable{
            Random rm;
            String name;
            public RunnableExample(String tname){
                this.name = tname;
                rm = new Random();
            }
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    System.out.println(i+" "+name);
                    try {
                        Thread.sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("RunnableExample线程"+name+"完成");
            }
        }
        Thread td1 = new Thread(new RunnableExample("线程一"));
        Thread td2 = new Thread(new RunnableExample("线程二"));
        td1.start();
        td2.start();
    }
    public void timertask(View v){
        class MyThread extends TimerTask{
            Random rm;
            String name;
            public MyThread(String name){
                this.name = name;
                rm = new Random();
            }
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    System.out.println(i+":"+name);
                    try {
                        Thread.sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("TimerTask线程"+name+"完成");
            }
        }
        Timer tm1 = new Timer();
        Timer tm2 = new Timer();
        MyThread td1 = new MyThread("线程一");
        MyThread td2 = new MyThread("线程二");
        tm1.schedule(td1,0);
        tm2.schedule(td2,0);
    }
    public void handlermessage(View v){
        final Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        showmsg(String.valueOf(msg.arg1+msg.getData().get("attach").toString()));
                        break;
                }
            }
        };
        class MyTask extends TimerTask{
            int countdown;
            double achievement1=1,achievement2=2;
            public MyTask(int seconds){
                this.countdown=seconds;
            }
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what=1;
                msg.arg1=countdown--;
                achievement1=achievement1*1.01;
                achievement2=achievement2*1.02;
                Bundle bundle = new Bundle();
                bundle.putString("attach","\n努力多1%:"+achievement1+"\n努力多2%:"+achievement2);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }
        Timer tm1=new Timer();
        tm1.schedule(new MyTask(seconds),1000);
    }
    public void showmsg(String msg){
        tv1.setText(msg);
    }
    public void asynctask(View v){
        class LearHard extends AsyncTask<Long,String,String>{
            private Context context;
            final int duration=10;
            int count=0;
            public LearHard(Activity context){
                this.context=context;
            }
            @Override
            protected String doInBackground(Long... params) {
                long num = params[0].longValue();
                while(count<duration){
                    num--;
                    count++;
                    String status = "离毕业还有"+num+"秒，努力学习"+count+"秒";
                    publishProgress(status);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "这"+duration+"秒有收获，没虚度";
            }

            @Override
            protected void onProgressUpdate(String... values) {
                ((MainActivity)context).tv1.setText(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                showmsg(s);
                super.onPostExecute(s);
            }
        }
        LearHard lh = new LearHard(this);
        lh.execute((long)seconds);
    }
}
