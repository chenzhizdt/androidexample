package org.instorm.test.view;

import java.util.ArrayList;

import org.instorm.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemsFragment extends Fragment {
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> adapter;
	private ListView myListView;
	private Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.items_fragment, container, false);
		myListView = (ListView) view.findViewById(R.id.myListView);
		todoItems = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(activity, R.layout.todolist_item, todoItems);
		myListView.setAdapter(adapter);
		return view;
	}
	
	public void addNewItem(String newItem){
		todoItems.add(0, newItem);
		adapter.notifyDataSetChanged();
	}
}
