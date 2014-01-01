package org.instorm.example.touchevent;

import java.util.ArrayList;

import org.instorm.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TouchActivity extends Activity{
	
	private static final String TAG = "TouchActivity";
	
	private ListView lvLog;
	private static ArrayAdapter<String> adapter;
	private static ArrayList<String> logs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_event);
		lvLog = (ListView) findViewById(R.id.lv_log);
		logs = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logs);
		lvLog.setAdapter(adapter);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		addLog("TouchActivity onTouchEvent");
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		addLog("TouchActivity dispatchTouchEvent");
		return super.dispatchTouchEvent(ev);
	}
	
	public static void addLog(String msg){
		Log.v(TAG, msg);
		logs.add(msg);
		adapter.notifyDataSetChanged();
	}
}
