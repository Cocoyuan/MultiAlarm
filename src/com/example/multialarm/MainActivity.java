package com.example.multialarm;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button bt_sleep, bt_run, bt_cook;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bt_sleep = (Button) this.findViewById(R.id.button_sleep);
		bt_run = (Button) this.findViewById(R.id.button_run);
		bt_cook = (Button) this.findViewById(R.id.button_cook);
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
	}

	
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
