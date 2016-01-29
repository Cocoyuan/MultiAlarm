package com.example.multialarm;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button bt_sleep, bt_run, bt_cook, bt_self;
	private TextView tvTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvTime = (TextView) findViewById(R.id.mytime);
		new TimeThread().start(); //启动新的线程
		bt_sleep = (Button) this.findViewById(R.id.bt_sleep);
		bt_run = (Button) this.findViewById(R.id.bt_run);
		bt_cook = (Button) this.findViewById(R.id.bt_cook);
		bt_self = (Button) this.findViewById(R.id.bt_self);
        bt_sleep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//			
				Intent sleepIntent = new Intent(MainActivity.this,SleepActivity.class);
				startActivityForResult(sleepIntent, 0);
				System.out.print("test");
			}
		});
        bt_run.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//			
				Intent runIntent = new Intent(MainActivity.this,RunActivity.class);
				startActivityForResult(runIntent, 0);
			}
		});
        bt_cook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//			
				Intent cookIntent = new Intent(MainActivity.this,CookActivity.class);
				startActivityForResult(cookIntent, 0);
			}
		});
        bt_self.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//			
				Intent selfIntent = new Intent(MainActivity.this,SelfActivity.class);
				startActivityForResult(selfIntent, 0);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
