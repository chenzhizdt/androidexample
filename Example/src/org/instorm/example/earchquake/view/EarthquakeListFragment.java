package org.instorm.example.earchquake.view;

import java.util.Date;

import org.instorm.example.earchquake.EarthquakeActivity;
import org.instorm.example.earchquake.EarthquakeProvider;
import org.instorm.example.earchquake.EarthquakeUpdateService;
import org.instorm.example.earchquake.model.Quake;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ContentResolver cr = getActivity().getContentResolver();
		
		Cursor result = cr.query(ContentUris.withAppendedId(EarthquakeProvider.CONTENT_URI, id), null, null, null, null);
		
		if(result.moveToFirst()){
			Date date = new Date(result.getColumnIndex(EarthquakeProvider.KEY_DATE));
			String details = result.getString(result.getColumnIndex(EarthquakeProvider.KEY_DETAILS));
			double magnitude = result.getDouble(result.getColumnIndex(EarthquakeProvider.KEY_MAGNITUDE));
			String linkString = result.getString(result.getColumnIndex(EarthquakeProvider.KEY_LINK));
			double lat = result.getDouble(result.getColumnIndex(EarthquakeProvider.KEY_LOCATION_LAT));
			double lng = result.getDouble(result.getColumnIndex(EarthquakeProvider.KEY_LOCATION_LNG));
			
			Location location = new Location("db");
			location.setLatitude(lat);
			location.setLongitude(lng);
			
			Quake quake = new Quake(date, details, location, magnitude, linkString);
			
			DialogFragment newFragment = EarthquakeDialog.newInstance(getActivity(), quake);
			newFragment.show(getFragmentManager(), "dialog");
		}
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
