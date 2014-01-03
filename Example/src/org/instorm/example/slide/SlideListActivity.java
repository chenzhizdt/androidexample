package org.instorm.example.slide;

import java.util.ArrayList;

import org.instorm.example.R;
import org.instorm.example.slide.SlideListView.RemoveDirection;
import org.instorm.example.slide.SlideListView.RemoveListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SlideListActivity extends Activity implements RemoveListener{
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> data;
	private SlideListView slideList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidelist);
		slideList = (SlideListView) findViewById(R.id.slideListView);
		data = new ArrayList<String>();
		for(int i = 0; i < 20; i++){
			data.add("Item " + i);
		}
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data);
		slideList.setAdapter(adapter);
		slideList.setRemoveListener(this);
	}

	@Override
	public void onRemove(RemoveDirection direction, int position) {
		adapter.remove(data.get(position));
	}
}
