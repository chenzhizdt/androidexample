package org.instorm.example;

import org.instorm.example.compass.CompassActivity;
import org.instorm.example.contactpicker.ContactPickerActivity;
import org.instorm.example.earchquake.EarthquakeActivity;
import org.instorm.example.lifecycle.LifeCycleActivity;
import org.instorm.example.pmtool.PmtoolLoginActivity;
import org.instorm.example.todolist.TodoListActivity;
import org.instorm.example.touchevent.TouchActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
	
	public void openDownload(View v){
		String serviceString = Context.DOWNLOAD_SERVICE;
		DownloadManager downloadManager = (DownloadManager) getSystemService(serviceString);
		Uri uri = Uri.parse("http://developer.android.com/shareables/icon_templates-v4.0-3.zip");
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE);
		long id = downloadManager.enqueue(request);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.prompt);
		dialog.setMessage(getString(R.string.begin_download) + ": " + id);
		dialog.setNegativeButton(R.string.confirm, null);
		dialog.show();
	}
	
	public void openLifeCycle(View v){
		Intent intent = new Intent(this, LifeCycleActivity.class);
		startActivity(intent);
	}
	
	public void openTouchEvent(View v){
		Intent intent = new Intent(this, TouchActivity.class);
		startActivity(intent);
	}
}
