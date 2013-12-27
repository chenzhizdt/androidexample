package org.instorm.example.earchquake;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class EarthquakeUpdateService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;

	private static final String TAG = "EarthquakeUpdateService";
	
	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;
	private Notification.Builder earthquakeNotificationBuilder;
	
	
	public EarthquakeUpdateService() {
		super(TAG);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		String ALARM_ACTION = EarthquakeAlarmReceiver.ACTION_REFRESH_EARTHQUAKE_ALARM;
		Intent intentToFire = new Intent(ALARM_ACTION);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
		
		earthquakeNotificationBuilder = new Notification.Builder(this);
		earthquakeNotificationBuilder.setAutoCancel(true).setTicker("Earthquake detected").setSmallIcon(android.R.drawable.ic_search_category_default);
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
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
						
						addNewQuake(quake);
					}
				}
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
		} finally {
		}
	}
	
	@SuppressWarnings("deprecation")
	private void broadcastNotification(Quake quake) {
		Intent startActivityIntent = new Intent(this, EarthquakeActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(this, 0, startActivityIntent, 0);
		
		earthquakeNotificationBuilder.setContentIntent(launchIntent).setWhen(quake.getDate().getTime()).setContentTitle("M: " + quake.getMagnitude()).setContentText(quake.getDetails());
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if(quake.getMagnitude() > 6){
			Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			earthquakeNotificationBuilder.setSound(ringURI);
		}
		
		notificationManager.notify(NOTIFICATION_ID, earthquakeNotificationBuilder.getNotification());
	}

	private void addNewQuake(Quake quake) {
		ContentResolver cr = getContentResolver();
		
		String w = EarthquakeProvider.KEY_DATE + "="
				+ quake.getDate().getTime();

		Cursor query = cr.query(EarthquakeProvider.CONTENT_URI, null, w, null,
				null);

		if (query.getCount() == 0) {
			ContentValues values = new ContentValues();

			values.put(EarthquakeProvider.KEY_DATE, quake.getDate().getTime());
			values.put(EarthquakeProvider.KEY_DETAILS, quake.getDetails());
			values.put(EarthquakeProvider.KEY_LINK, quake.getLink());
			values.put(EarthquakeProvider.KEY_LOCATION_LAT, quake.getLocation()
					.getLatitude());
			values.put(EarthquakeProvider.KEY_LOCATION_LNG, quake.getLocation()
					.getLongitude());
			values.put(EarthquakeProvider.KEY_MAGNITUDE, quake.getMagnitude());
			values.put(EarthquakeProvider.KEY_SUMMARY, quake.toString());

			cr.insert(EarthquakeProvider.CONTENT_URI, values);
			broadcastNotification(quake);
		}
		query.close();
	}
	/**
	 * IntentService类会自动启动一个线程去调用这个方法
	 * 并且IntentService的实现会将收到的Intent放到队列中，并逐个处理它，所有Intent处理完毕后，会自动关闭Service
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Context context = getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		int updateFeq = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));
		
		boolean autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);
		
		if(autoUpdateChecked){
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timeToRefresh = SystemClock.elapsedRealtime() + updateFeq*60*1000;
			alarmManager.setInexactRepeating(alarmType, timeToRefresh, updateFeq*60*1000, alarmIntent);
		} else {
			alarmManager.cancel(alarmIntent);
			refreshEarthquakes();
		}
	}
}
