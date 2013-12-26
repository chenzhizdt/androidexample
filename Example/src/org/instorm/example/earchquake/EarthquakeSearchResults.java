package org.instorm.example.earchquake;

import org.instorm.example.R;
import org.instorm.example.earchquake.view.EarthquakeSearchResultsFragment;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class EarthquakeSearchResults extends FragmentActivity{
	
	private EarthquakeSearchResultsFragment esrf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_search_results);
		esrf = (EarthquakeSearchResultsFragment) getFragmentManager().findFragmentById(R.id.searchResultsFragment);
		parseIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		parseIntent(intent);
	}
	
	private void parseIntent(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String searchQuery = intent.getStringExtra(SearchManager.QUERY);
			Bundle args = new Bundle();
			args.putString(EarthquakeSearchResultsFragment.QUERY_EXTRA_KEY, searchQuery);
			esrf.restartLoader(args);
		}
	}
}
