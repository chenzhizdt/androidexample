package org.instorm.example.earchquake.view;

import org.instorm.example.R;
import org.instorm.example.earchquake.EarthquakeProvider;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EarthquakeSearchResultsFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	public static String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
	
	private ListView lvSearchResults;
	private SimpleCursorAdapter adapter;
	private Activity fa;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_eq_search_results,
				container, false);
		lvSearchResults = (ListView) view.findViewById(R.id.lv_search_results);
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null,
				new String[] { EarthquakeProvider.KEY_SUMMARY },
				new int[] { android.R.id.text1 }, 0);
		lvSearchResults.setAdapter(adapter);
		fa = getActivity();
		fa.getLoaderManager().initLoader(0, null, this);
		return view;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String query = "0";
		if(args != null){
			query = args.getString(QUERY_EXTRA_KEY);
		}
		
		String[] projection = {EarthquakeProvider.KEY_ID,
				EarthquakeProvider.KEY_SUMMARY};
		String where = EarthquakeProvider.KEY_SUMMARY
				+ " like \"%" + query + "%\"";
		String[] whereArgs = null;
		String sortOrder = EarthquakeProvider.KEY_SUMMARY + " collate localized asc";
		
		CursorLoader loader = new CursorLoader(getActivity(),
				EarthquakeProvider.CONTENT_URI, projection, where, whereArgs, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	public void restartLoader(Bundle args){
		fa.getLoaderManager().restartLoader(0, args, this);
	}
}
