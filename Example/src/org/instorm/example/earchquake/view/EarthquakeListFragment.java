package org.instorm.example.earchquake.view;

import org.instorm.example.earchquake.EarthquakeActivity;
import org.instorm.example.earchquake.EarthquakeProvider;
import org.instorm.example.earchquake.EarthquakeUpdateService;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class EarthquakeListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	
	private SimpleCursorAdapter adapter;
	@SuppressWarnings("unused")
	private static final String TAG = "EarthquakeListFragment";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		int layoutId = android.R.layout.simple_list_item_1;
		adapter = new SimpleCursorAdapter(getActivity(), layoutId, null, new String[]{EarthquakeProvider.KEY_SUMMARY}, new int[]{android.R.id.text1}, 0);
		
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(0, null, this);
		
		refreshEarthquakes();
	}
	
	public void refreshEarthquakes(){
		getLoaderManager().restartLoader(0, null, this);
		getActivity().startService(new Intent(getActivity(), EarthquakeUpdateService.class));
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[]{
				EarthquakeProvider.KEY_ID,
				EarthquakeProvider.KEY_SUMMARY
		};
		EarthquakeActivity ea = (EarthquakeActivity) getActivity();
		String selection = EarthquakeProvider.KEY_MAGNITUDE + " > " +ea.minimumMagnitude;
		CursorLoader loader  = new CursorLoader(getActivity(),
				EarthquakeProvider.CONTENT_URI, projection, selection, null, null);
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
}
