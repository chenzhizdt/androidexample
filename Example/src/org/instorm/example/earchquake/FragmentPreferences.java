package org.instorm.example.earchquake;

import java.util.List;

import org.instorm.example.R;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;

public class FragmentPreferences extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected boolean isValidFragment(String fragmentName) {
		return true;
	}
}
