package com.example.multialarm;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SleepActivity extends Activity{

	private Button bt_set,bt_noisy;									// setting alarm button
	private ToggleButton btn_enClk;						// alarm enable button
	private ToggleButton togbtn_AlarmStyle;
	private TextView tv_alarm,tvTime;

	private SharedPreferences sharedData;
	SharedPreferences.Editor edit;
	private static boolean alarmStyle = true;			// output mode (true:sound;false:vibrate)

	Calendar c = Calendar.getInstance();

	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	static SleepActivity instance;
	static String shakeSenseValue;

	public static void setAlarmStyle(boolean style)
	{
		alarmStyle = style;
	}

	public static boolean getAlarmStyle()
	{
		return alarmStyle;
	}

	private void loadData()
	{
		sharedData = getSharedPreferences("main_activity", MODE_PRIVATE);
		edit = sharedData.edit();
		tv_alarm.setText(sharedData.getString("time",
				sdf.format(new Date(c.getTimeInMillis()))));
		btn_enClk.setChecked(sharedData.getBoolean("on_off", false));
	}

	private void saveData()
	{
		edit.putString("time", bt_set.getText().toString());
		edit.putBoolean("on_off", btn_enClk.isChecked());
		edit.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep);
		tvTime = (TextView) findViewById(R.id.mytime);
		new TimeThread().start(); //启动新的线程
		tv_alarm = (TextView) findViewById(R.id.textView_alarm);
		bt_noisy = (Button) this.findViewById(R.id.bt_noisy);
        bt_noisy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
							
				Intent noisyIntent = new Intent(SleepActivity.this,SleepNActivity.class);
				startActivityForResult(noisyIntent, 0);
			}
		});
		instance = this;										// close this activity in ShakeAlarm layout
		shakeSenseValue = getResources().getString(R.string.shakeSenseValue_2);
		String timeOnBtn = "";

		timeOnBtn = sdf.format(new Date(c.getTimeInMillis()));

		ButtonListener buttonListener = new ButtonListener();	
		bt_set = (Button) findViewById(R.id.button_set);
		//bt_set.setText(timeOnBtn);
		tv_alarm.setText(timeOnBtn);
		bt_set.setOnClickListener(buttonListener);

		btn_enClk = (ToggleButton) findViewById(R.id.btn_enClk); 
		btn_enClk.setOnClickListener(buttonListener);

		loadData();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		saveData();
	}

		class ButtonListener implements OnClickListener
	{
		private TimePicker timePicker;			

		private PendingIntent pi;
		private Intent intent;
		AlarmManager alarmManager;
		LayoutInflater inflater;
		LinearLayout setAlarmLayout;

		/**
		 * load dialog layout in ButtonListener operation
		 */
		public ButtonListener()
		{
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);		// 用于加载alertdialog布局
			alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			setAlarmLayout = (LinearLayout) inflater.inflate(
					R.layout.activity_alarm, null);
		}

		@SuppressWarnings("deprecation")
		private void enableClk()
		{
			timePicker = (TimePicker) setAlarmLayout
					.findViewById(R.id.timepicker);
			c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());        //set hour of alarm 
			c.set(Calendar.MINUTE, timePicker.getCurrentMinute());            // set minute of alarm
			c.set(Calendar.SECOND, 0); // set second of alarm
			c.set(Calendar.MILLISECOND, 0); // set millisecond of alarm

			// if (c.getTimeInMillis() - System.currentTimeMillis() < 0)
			// {
			// c.roll(Calendar.DATE, 1);
			// }

			tv_alarm.setText(sdf.format(new Date(c.getTimeInMillis())));
			intent = new Intent(SleepActivity.this, AlarmReceiver.class);    // build Intent object
			pi = PendingIntent.getBroadcast(SleepActivity.this, 0, intent, 0);    //  build PendingIntent

			alarmManager.setRepeating(AlarmManager.RTC,    // setting alarm to wake up in this time
					c.getTimeInMillis(), 24 * 60 * 60 * 1000, pi);
		}

		private void disableClk()
		{
			alarmManager.cancel(pi);
		}

		@Override
		public void onClick(View v)
		{

			switch (v.getId())
			{
			case R.id.button_set:

				setAlarmLayout = (LinearLayout) inflater.inflate(
						R.layout.activity_alarm, null);

				togbtn_AlarmStyle = (ToggleButton) setAlarmLayout
						.findViewById(R.id.togbtn_alarm_style);
				togbtn_AlarmStyle.setChecked(sharedData.getBoolean("style",
						false));
				timePicker = (TimePicker) setAlarmLayout
						.findViewById(R.id.timepicker);
				timePicker.setIs24HourView(true);

				new AlertDialog.Builder(SleepActivity.this)
						.setView(setAlarmLayout)
						.setTitle("Alarm time")
						.setPositiveButton("Confirm",
								new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog,
											int which)
									{
										disableClk();
										enableClk();
										if (togbtn_AlarmStyle.isChecked())
										{
											SleepActivity.setAlarmStyle(true);
										}
										else
										{
											SleepActivity.setAlarmStyle(false);
										}

										edit.putBoolean("style",
												togbtn_AlarmStyle.isChecked());
										btn_enClk.setChecked(true);
										Toast.makeText(SleepActivity.this,
												"Add alarm succeed", Toast.LENGTH_LONG)
												.show();// notice users
									}
								}).setNegativeButton("Cancel", null).show();
				break;

			case R.id.btn_enClk:
				if (btn_enClk.isChecked())
				{
					enableClk();
				}
				else
				{
					disableClk();
				}
				break;
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		SubMenu subMenu = menu.addSubMenu("Shake Mode");
		subMenu.add(1, 1, 1, "Soft Shake");
		subMenu.add(1, 2, 2, "Normal Shake");
		subMenu.add(1, 3, 3, "Tough Shake");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 1:
			shakeSenseValue = getResources().getString(
					R.string.shakeSenseValue_1);
			Toast.makeText(this, "Now in Soft Shake Mode", Toast.LENGTH_SHORT).show();
			break;

		case 2:
			shakeSenseValue = getResources().getString(
					R.string.shakeSenseValue_2);
			Toast.makeText(this, "Now in Normal Shake Mode", Toast.LENGTH_SHORT).show();
			break;

		case 3:
			shakeSenseValue = getResources().getString(
					R.string.shakeSenseValue_3);
			Toast.makeText(this, "Now in Tough Shake Mode", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_about:
			new AlertDialog.Builder(this).setTitle("About").setMessage("MultiAlarm")
					.setNegativeButton("Close", null).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
