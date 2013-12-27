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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

public class EarthquakeActivity extends Activity {

	public int minimumMagnitude = 3;
	public boolean autoUpdateChecked = false;
	public int updateFreq = 60;

	private static final int SHOW_PREFERENCES = 1;
	private static String ACTION_BAR_INDEX = "ACTION_BAR_INDEX";

	private TabListener<EarthquakeListFragment> listTabListener;
	private TabListener<EarthquakeMapFragment> mapTabListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_earchquake);
		updateFromPreferences();

		ActionBar actionBar = getActionBar();

		View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer);

		boolean tabletLayout = fragmentContainer == null;

		if (!tabletLayout) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			Tab listTab = actionBar.newTab();
			listTabListener = new TabListener<EarthquakeListFragment>(this,
					R.id.EarthquakeFragmentContainer,
					EarthquakeListFragment.class);

			listTab.setText("List")
					.setContentDescription("List of earthquakes")
					.setTabListener(listTabListener);
			actionBar.addTab(listTab);

			Tab mapTab = actionBar.newTab();
			mapTabListener = new TabListener<EarthquakeMapFragment>(this,
					R.id.EarthquakeFragmentContainer,
					EarthquakeMapFragment.class);

			mapTab.setText("MAP")
					.setContentDescription("Map of earthquakes")
					.setTabListener(mapTabListener);
			actionBar.addTab(mapTab);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.eq_menu, menu);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo searchableInfo = searchManager
				.getSearchableInfo(getComponentName());
		
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchableInfo);
		int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(id);
		textView.setTextColor(Color.BLACK);
		
		

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (R.id.menu_preferences):
			Class<?> c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? PreferencesActivity.class
					: FragmentPreferences.class;
			Intent intent = new Intent(this, c);
			startActivityForResult(intent, SHOW_PREFERENCES);
			return true;
		case (R.id.menu_update):
			startService(new Intent(this, EarthquakeUpdateService.class));
			return true;
		}

		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer);
		boolean tabletLayout = fragmentContainer == null;
		
		if(!tabletLayout){
			SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
			int actionIndex = sp.getInt(ACTION_BAR_INDEX, 0);
			getActionBar().setSelectedNavigationItem(actionIndex);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer);
		boolean tabletLayout = fragmentContainer == null;
		if(!tabletLayout){
			int actionBarIndex = getActionBar().getSelectedTab().getPosition();
			SharedPreferences.Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
			editor.putInt(ACTION_BAR_INDEX, actionBarIndex);
			editor.apply();
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			if(mapTabListener.fragment != null){
				ft.detach(mapTabListener.fragment);
			}
			if(listTabListener.fragment != null){
				ft.detach(listTabListener.fragment);
			}
			ft.commit();
		}
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		View fragmentContainer = findViewById(R.id.EarthquakeFragmentContainer);
		boolean tabletLayout = fragmentContainer == null;
		
		if(!tabletLayout){
			listTabListener.fragment = getFragmentManager().findFragmentByTag(EarthquakeListFragment.class.getName());
			mapTabListener.fragment = getFragmentManager().findFragmentByTag(EarthquakeMapFragment.class.getName());
			
			SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
			int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
			getActionBar().setSelectedNavigationItem(actionBarIndex);
		}
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
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment fragment;
		private Activity activity;
		private Class<T> fragmentClass;
		private int fragmentContainer;

		public TabListener(Activity activity, int fragmentContainer,
				Class<T> fragmentClass) {
			this.activity = activity;
			this.fragmentClass = fragmentClass;
			this.fragmentContainer = fragmentContainer;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			if (fragment != null)
				ft.attach(fragment);
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (fragment == null) {
				String fragmentName = fragmentClass.getName();
				fragment = Fragment.instantiate(activity, fragmentName);
				ft.add(fragmentContainer, fragment, fragmentName);
			} else {
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (fragment != null)
				ft.detach(fragment);
		}
	}
}
