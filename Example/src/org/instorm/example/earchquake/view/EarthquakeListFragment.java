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
import org.instorm.example.earchquake.model.Quake;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class EarthquakeListFragment extends ListFragment {
	
	private ArrayAdapter<Quake> arrayAdapter;
	private ArrayList<Quake> earthquakes = new ArrayList<Quake>();
	private static final String TAG = "EarthquakeListFragment";
	private Handler handler = new Handler();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		int layoutId = android.R.layout.simple_list_item_1;
		arrayAdapter = new ArrayAdapter<Quake>(getActivity(), layoutId, earthquakes);
		setListAdapter(arrayAdapter);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				refreshEarthquakes();
			}
		});
		t.start();
	}
	
	public void refreshEarthquakes(){
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
		earthquakes.add(quake);
		arrayAdapter.notifyDataSetChanged();
	}
}
