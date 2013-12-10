package org.instorm.example.earchquake;

import org.instorm.example.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class PreferencesActivity extends Activity {
	
	public static final String USER_PREFERENCES = "USER_PREFERENCES";
	public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
	public static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
	public static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";
	
	private CheckBox autoUpdate;
	private Spinner updateFregSpinner;
	private Spinner magnitudeSpinner;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eq_preferences);
		
		this.autoUpdate = (CheckBox) findViewById(R.id.checkbox_auto_update);
		this.updateFregSpinner = (Spinner) findViewById(R.id.spinner_update_freq);
		this.magnitudeSpinner = (Spinner) findViewById(R.id.spinner_quake_mag);
		
		populateSpinners();
		
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		updateUIFromPreferences();
		
		Button okButton = (Button) findViewById(R.id.btn_ok);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				savePreferences();
				PreferencesActivity.this.setResult(RESULT_OK);
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.btn_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PreferencesActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	private void populateSpinners() {
		ArrayAdapter<CharSequence> updatefreqAdapter;
		updatefreqAdapter = ArrayAdapter.createFromResource(this, R.array.update_freq_options, android.R.layout.simple_spinner_item);
		int spinner_dd_item = android.R.layout.simple_spinner_dropdown_item;
		updatefreqAdapter.setDropDownViewResource(spinner_dd_item);
		updateFregSpinner.setAdapter(updatefreqAdapter);
		
		ArrayAdapter<CharSequence> magnitudeAdapter;
		magnitudeAdapter = ArrayAdapter.createFromResource(this, R.array.magnitude_options, android.R.layout.simple_spinner_item);
		magnitudeAdapter.setDropDownViewResource(spinner_dd_item);
		magnitudeSpinner.setAdapter(magnitudeAdapter);
	}
	
	private void updateUIFromPreferences(){
		boolean autoUpChecked = prefs.getBoolean(PREF_AUTO_UPDATE, false);
		int updateFreqIndex = prefs.getInt(PREF_UPDATE_FREQ_INDEX, 2);
		int minMagIndex = prefs.getInt(PREF_MIN_MAG_INDEX, 0);
		updateFregSpinner.setSelection(updateFreqIndex);
		magnitudeSpinner.setSelection(minMagIndex);
		autoUpdate.setChecked(autoUpChecked);
	}
	
	private void savePreferences(){
		boolean autoUpChecked = autoUpdate.isChecked();
		int updateFreqIndex = updateFregSpinner.getSelectedItemPosition();
		int minMagIndex = magnitudeSpinner.getSelectedItemPosition();
		Editor editor = prefs.edit();
		editor.putBoolean(PREF_AUTO_UPDATE, autoUpChecked);
		editor.putInt(PREF_UPDATE_FREQ_INDEX, updateFreqIndex);
		editor.putInt(PREF_MIN_MAG_INDEX, minMagIndex);
		editor.commit();
	}
}
