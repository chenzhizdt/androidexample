package org.instorm.example;

import org.instorm.example.compass.CompassActivity;
import org.instorm.example.contactpicker.ContactPickerActivity;
import org.instorm.example.earchquake.EarthquakeActivity;
import org.instorm.example.pmtool.PmtoolLoginActivity;
import org.instorm.example.todolist.TodoListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AppListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applist);
	}
	
	public void openPmtool(View v){
		Intent intent = new Intent(this, PmtoolLoginActivity.class);
		startActivity(intent);
	}
	
	public void openCompass(View v){
		Intent intent = new Intent(this, CompassActivity.class);
		startActivity(intent);
	}
	
	public void openContactPicker(View v){
		Intent intent = new Intent(this, ContactPickerActivity.class);
		startActivity(intent);
	}
	
	public void openTodoList(View v){
		Intent intent = new Intent(this, TodoListActivity.class);
		startActivity(intent);
	}
	
	public void openEarthquake(View v){
		Intent intent = new Intent(this, EarthquakeActivity.class);
		startActivity(intent);
	}
}
