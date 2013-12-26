package org.instorm.example.earchquake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EarthquakeAlarmReceiver extends BroadcastReceiver{
	
	public static final String ACTION_REFRESH_EARTHQUAKE_ALARM = "org.instorm.earthquake.ACTION_REFRESH_EARTHQUAKE_ALARM";

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent intent = new Intent(context, EarthquakeUpdateService.class);
		context.startService(intent);
	}
}
