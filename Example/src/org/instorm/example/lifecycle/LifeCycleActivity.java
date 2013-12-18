package org.instorm.example.lifecycle;

import org.instorm.example.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class LifeCycleActivity extends FragmentActivity{
	
	private static final String TAG = "LifeCycleActivity";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_lifecycle);
		log("Create", "Start");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		log("Start", "Resume");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		log("Resume", "Pause");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("Pause", "Resume", "Stop");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("Stop", "Restart", "Destory");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		log("Destory", "Activity is destoryed");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		log("Restart", "Start");
	}
	
	private void log(String current, String next, String probable){
		String out = "Activity " + current;
		if(next != null && !next.equals("")){
			out = out + ", 下一步为 " + next;
		}
		if(probable != null && !probable.equals("")){
			out = out + ", 或进入 " + probable;
		}
		Log.v(TAG, out);
	}
	
	private void log(String current, String next){
		log(current, next, null);
	}
}
