package org.instorm.example.todolist.view;

import java.util.ArrayList;

import org.instorm.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TodoListFragment extends Fragment {
	
	private ArrayList<TodoItem> todoItems;
	private TodoListAdapter adapter;
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
		View view = inflater.inflate(R.layout.fragment_todolist, container, false);
		myListView = (ListView) view.findViewById(R.id.myListView);
		todoItems = new ArrayList<TodoItem>();
		adapter = new TodoListAdapter(activity, R.layout.list_item_todolist, todoItems);
		myListView.setAdapter(adapter);
		return view;
	}
	
	public void addNewItem(String newItem){
		todoItems.add(0, new TodoItem(newItem));
		adapter.notifyDataSetChanged();
	}
}
