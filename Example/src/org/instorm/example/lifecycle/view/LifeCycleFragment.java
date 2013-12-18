package org.instorm.example.lifecycle.view;

import org.instorm.example.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LifeCycleFragment extends Fragment{
	
	private static final String TAG = "LifeCycleFragment";
	private Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		log("Attach", "Create");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		log("Create", "CreateView");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_lifecycle, container, false);
		Button btn1 = (Button) view.findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle(R.string.prompt);
				dialog.setMessage(R.string.lifecycle_test);
				dialog.setNegativeButton(R.string.confirm, null);
				dialog.show();
			}
		});
		log("CreateView", "ActivityCreated");
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		log("ActivityCreated", "Start");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		log("Start", "Resume");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		log("Resume", "Pause");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("Pause", "Stop");
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("Stop", "DestroyView");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		log("DestroyView", "Destroy", "CreateView");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		log("Destroy", "Detach");
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		log("Detach", "Fragment is destoryed");
	}
	
	private void log(String current, String next, String probable){
		String out = "Fragment " + current;
		if(next != null && !next.equals("")){
			out = out + ", 下一步为 " + next;
		}
		if(probable != null && !probable.equals("")){
			out = out + ", 或进入 " + probable;
		}
		Log.v(TAG, out);
	}
	
	private void log(String current, String next){
		log(current, next, null);
	}
}
