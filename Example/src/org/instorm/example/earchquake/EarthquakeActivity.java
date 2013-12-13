package org.instorm.example.earchquake;

import org.instorm.example.R;
import org.instorm.example.earchquake.view.EarthquakeListFragment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class EarthquakeActivity extends FragmentActivity {

	public int minimumMagnitude = 3;
	public boolean autoUpdateChecked = false;
	public int updateFreq = 60;

	private static final int MENU_PREFERENCES = Menu.FIRST + 1;
	private static final int MENU_UPDATE = Menu.FIRST + 2;
	private static final int SHOW_PREFERENCES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_earchquake);
		updateFromPreferences();
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		
		SearchView searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setSearchableInfo(searchableInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
		// menu.add(1, MENU_UPDATE, Menu.NONE, R.string.menu_update);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (MENU_PREFERENCES):
			Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
					PreferencesActivity.class : FragmentPreferences.class;
			Intent intent = new Intent(this, c);
			startActivityForResult(intent, SHOW_PREFERENCES);
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SHOW_PREFERENCES) {
			updateFromPreferences();
			FragmentManager fragmentManager = getSupportFragmentManager();
			final EarthquakeListFragment earthquakeListFragment = (EarthquakeListFragment) fragmentManager
					.findFragmentById(R.id.fm_earthquakelist);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					earthquakeListFragment.refreshEarthquakes();
				}
			});
			t.start();
		}
	}

	private void updateFromPreferences() {
		Context context = getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		autoUpdateChecked = prefs.getBoolean(
				PreferencesActivity.PREF_AUTO_UPDATE, false);
		minimumMagnitude = Integer.valueOf(prefs.getString(
				PreferencesActivity.PREF_MIN_MAG, "3"));
		updateFreq = Integer.valueOf(prefs.getString(
				PreferencesActivity.PREF_UPDATE_FREQ, "60"));
	}
}
