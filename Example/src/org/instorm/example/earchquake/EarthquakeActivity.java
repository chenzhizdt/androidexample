package org.instorm.example.earchquake;

import org.instorm.example.R;
import org.instorm.example.earchquake.view.EarthquakeListFragment;
import org.instorm.example.earchquake.view.EarthquakeMapFragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class EarthquakeActivity extends Activity {

	public int minimumMagnitude = 3;
	public boolean autoUpdateChecked = false;
	public int updateFreq = 60;

	private static final int MENU_PREFERENCES = Menu.FIRST + 1;
//	private static final int MENU_UPDATE = Menu.FIRST + 2;
	private static final int SHOW_PREFERENCES = 1;
	
	private TabListener<EarthquakeListFragment> listTabListener;
	private TabListener<EarthquakeMapFragment> mapTabListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_earchquake);
		updateFromPreferences();
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		
		SearchView searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setSearchableInfo(searchableInfo);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		
		Tab listTab = actionBar.newTab();
		
		listTabListener = new TabListener<EarthquakeListFragment>(this, fragmentContainer, fragmentClass)
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
			Class<?> c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
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
			startService(new Intent(this, EarthquakeUpdateService.class));
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
	
	@SuppressLint("NewApi")
	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		
		private Fragment fragment;
		private Activity activity;
		private Class<T> fragmentClass;
		private int fragmentContainer;
		
		public TabListener(Activity activity, int fragmentContainer, Class<T> fragmentClass){
			this.activity = activity;
			this.fragmentClass = fragmentClass;
			this.fragmentContainer = fragmentContainer;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			if(fragment != null)
				ft.attach(fragment);
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(fragment == null){
				String fragmentName = fragmentClass.getName();
				fragment = Fragment.instantiate(activity, fragmentName);
				ft.add(fragmentContainer, fragment, fragmentName);
			} else {
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if(fragment != null)
				ft.detach(fragment);
		}
	}
}
