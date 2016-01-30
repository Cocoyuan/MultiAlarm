package com.example.multialarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SelfActivity extends Activity{
	private TextView tvTime;
	private Button bt_noisy;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self);
		tvTime = (TextView) findViewById(R.id.mytime);
		new TimeThread().start(); //启动新的线程
		bt_noisy = (Button) this.findViewById(R.id.bt_noisy);
        bt_noisy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
							
				Intent noisyIntent = new Intent(SelfActivity.this,SelfNActivity.class);
				startActivityForResult(noisyIntent, 0);
			}
		});
	}
	class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    };

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
            	long sysTime = System.currentTimeMillis();
            	CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
                tvTime.setText(sysTimeStr); //更新时间
                break;
                default:
                	break;

            }
        }
    };
}