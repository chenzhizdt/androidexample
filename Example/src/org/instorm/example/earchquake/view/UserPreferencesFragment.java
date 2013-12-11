package org.instorm.example.earchquake.view;

import org.instorm.example.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class UserPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}
}
