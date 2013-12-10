package org.instorm.example.earchquake;

import org.instorm.example.R;
import org.instorm.example.earchquake.view.EarthquakeListFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class EarthquakeActivity extends FragmentActivity {
	
	public int minimumMagnitude = 0;
	public boolean autoUpdateChecked = false;
	public int updateFreq = 0;
	
	private static final int MENU_PREFERENCES = Menu.FIRST + 1;
	private static final int MENU_UPDATE = Menu.FIRST + 2;
	private static final int SHOW_PREFERENCES = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_earchquake);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		updateFromPreferences();
		
		menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
//		menu.add(1, MENU_UPDATE, Menu.NONE, R.string.menu_update);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()){
		case (MENU_PREFERENCES):
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivityForResult(intent, SHOW_PREFERENCES);
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == SHOW_PREFERENCES){
			if(resultCode == RESULT_OK){
				updateFromPreferences();
				FragmentManager fragmentManager = getSupportFragmentManager();
				final EarthquakeListFragment earthquakeListFragment = (EarthquakeListFragment) fragmentManager.findFragmentById(R.id.fm_earthquakelist);
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						earthquakeListFragment.refreshEarthquakes();
					}
				});
				t.start();
			}
		}
	}
	
	private void updateFromPreferences(){
		Context context = getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);
		
		int minMagIndex = prefs.getInt(PreferencesActivity.PREF_MIN_MAG_INDEX, 0);
		if(minMagIndex < 0) minMagIndex = 0;
		
		int freqIndex = prefs.getInt(PreferencesActivity.PREF_UPDATE_FREQ_INDEX, 0);
		if(freqIndex < 0) freqIndex = 0;
		
		Resources r = getResources();
		
		String[] minMagValues = r.getStringArray(R.array.magnitude);
		String[] freqValues = r.getStringArray(R.array.update_freq_values);
		
		minimumMagnitude = Integer.valueOf(minMagValues[minMagIndex]);
		updateFreq = Integer.valueOf(freqValues[freqIndex]);
	}
}
