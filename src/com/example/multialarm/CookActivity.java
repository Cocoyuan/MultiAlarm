package com.example.multialarm;

import com.example.multialarm.SleepActivity.ButtonListener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CookActivity extends Activity{

	private Button bt_noisy, bt_set;
	private TextView tvTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		tvTime = (TextView) findViewById(R.id.mytime);
		new TimeThread().start(); //启动新的线程
		bt_noisy = (Button) this.findViewById(R.id.bt_noisy);
        bt_noisy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
							
				Intent noisyIntent = new Intent(CookActivity.this,CookNActivity.class);
				startActivityForResult(noisyIntent, 0);
			}
		});
        
        bt_set = (Button) this.findViewById(R.id.bt_set);
        bt_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmManager alarmManager;
				LayoutInflater inflater;
				LinearLayout setAlarmLayout;
				inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);		// 用于加载alertdialog布局
				alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				setAlarmLayout = (LinearLayout) inflater.inflate(
						R.layout.activity_alarm, null);
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
