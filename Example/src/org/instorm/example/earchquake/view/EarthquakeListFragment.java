package org.instorm.example.earchquake.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.instorm.example.R;
import org.instorm.example.earchquake.EarthquakeActivity;
import org.instorm.example.earchquake.EarthquakeProvider;
import org.instorm.example.earchquake.model.Quake;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ArrayAdapter;

public class EarthquakeListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	
	private SimpleCursorAdapter adapter;
//	private ArrayAdapter<Quake> arrayAdapter;
	private ArrayList<Quake> earthquakes = new ArrayList<Quake>();
	private static final String TAG = "EarthquakeListFragment";
	private Handler handler = new Handler();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		int layoutId = android.R.layout.simple_list_item_1;
//		arrayAdapter = new ArrayAdapter<Quake>(getActivity(), layoutId, earthquakes);
		adapter = new SimpleCursorAdapter(getActivity(), layoutId, null, new String[]{EarthquakeProvider.KEY_SUMMARY}, new int[]{android.R.id.text1}, 0);
		
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(0, null, this);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				refreshEarthquakes();
			}
		});
		t.start();
	}
	
	public void refreshEarthquakes(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				getLoaderManager().restartLoader(0, null, EarthquakeListFragment.this);
			}
		});
		URL url;
		String quakeFeed = getString(R.string.quake_feed);
		try {
			url = new URL(quakeFeed);
			
			URLConnection connection;
			connection = url.openConnection();
			
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK){
				InputStream is = httpConnection.getInputStream();
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(is);
				Element docEle = dom.getDocumentElement();
				
				earthquakes.clear();
				
				NodeList nl = docEle.getElementsByTagName("entry");
				
				if(nl != null && nl .getLength() > 0){
					for(int i = 1; i < nl.getLength(); i++){
						Element entry = (Element) nl.item(i);
						Element title = (Element) entry.getElementsByTagName("title").item(0);
						Element when = (Element) entry.getElementsByTagName("updated").item(0);
						Element link = (Element) entry.getElementsByTagName("link").item(0);
						Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
						
						String details = title.getFirstChild().getNodeValue();
						String linkString = link.getAttribute("href");
						String point = g.getFirstChild().getNodeValue();
						String dt = when.getFirstChild().getNodeValue();
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
						Date date = sdf.parse(dt);
						
						String[] l = point.split(" ");
						Location location = new Location("dummyGPS");
						location.setLatitude(Double.parseDouble(l[0]));
						location.setLongitude(Double.parseDouble(l[1]));
						
						String magnitudeString = details.split(" ")[1];
						int end = magnitudeString.length() - 1;
						double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
						
						details = details.split(",")[1].trim();
						
						final Quake quake = new Quake(date, details, location, magnitude, linkString);
						
						handler.post(new Runnable() {
							@Override
							public void run() {
								addNewQuake(quake);
							}
						});
//						earthquakes.add(quake);
					}
				}
//				arrayAdapter.notifyDataSetChanged();
			}
		} catch (MalformedURLException e) {
			Log.v(TAG, getString(R.string.url_wrong));
		} catch (IOException e) {
			Log.v(TAG, getString(R.string.connection_wrong));
		} catch (ParserConfigurationException e) {
			Log.v(TAG, getString(R.string.parse_wrong));
		} catch (SAXException e) {
			Log.v(TAG, getString(R.string.parse_wrong));
		} catch (ParseException e) {
			Log.v(TAG, getString(R.string.parse_wrong));
		}
	}
	
	private void addNewQuake(Quake quake){
		ContentResolver cr = getActivity().getContentResolver();
		String w = EarthquakeProvider.KEY_DATE + "=" + quake.getDate().getTime();
		
		Cursor query = cr.query(EarthquakeProvider.CONTENT_URI, null, w, null, null);
		
		if(query.getCount() == 0){
			ContentValues values = new ContentValues();
			
			values.put(EarthquakeProvider.KEY_DATE, quake.getDate().getTime());
			values.put(EarthquakeProvider.KEY_DETAILS, quake.getDetails());
			values.put(EarthquakeProvider.KEY_LINK, quake.getLink());
			values.put(EarthquakeProvider.KEY_LOCATION_LAT, quake.getLocation().getLatitude());
			values.put(EarthquakeProvider.KEY_LOCATION_LNG, quake.getLocation().getLongitude());
			values.put(EarthquakeProvider.KEY_MAGNITUDE, quake.getMagnitude());
			values.put(EarthquakeProvider.KEY_SUMMARY, quake.toString());
			
			cr.insert(EarthquakeProvider.CONTENT_URI, values);
		}
		query.close();
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
