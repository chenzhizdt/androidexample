package org.instorm.example.todolist.view;

import java.util.ArrayList;
import java.util.Date;

import org.instorm.example.R;
import org.instorm.example.todolist.TodoContentProvider;
import org.instorm.example.todolist.model.TodoItem;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TodoListFragment extends Fragment implements LoaderCallbacks<Cursor>{
	
	private static final String TAG = "TodoListFragment";
	
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
		FragmentActivity fa = (FragmentActivity) activity;
		fa.getSupportLoaderManager().initLoader(0, null, this);
		return view;
	}
	
	public void addNewItem(String newItem){
		ContentResolver cr = activity.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(TodoContentProvider.KEY_TASK, newItem);
		values.put(TodoContentProvider.KEY_CREATION_DATE, java.lang.System.currentTimeMillis());
		
		cr.insert(TodoContentProvider.CONTENT_URI, values);
		FragmentActivity fa = (FragmentActivity) activity;
		fa.getSupportLoaderManager ().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(activity,
				TodoContentProvider.CONTENT_URI, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int keyTaskIndex = cursor.getColumnIndexOrThrow(TodoContentProvider.KEY_TASK);
		int keyCreateDateIndex = cursor.getColumnIndexOrThrow(TodoContentProvider.KEY_CREATION_DATE);
		todoItems.clear();
		
		while(cursor.moveToNext()){
			TodoItem newItem = new TodoItem(cursor.getString(keyTaskIndex), new Date(cursor.getLong(keyCreateDateIndex)));
			Log.v(TAG, "任务名：" + newItem.getTask() + " 创建时间：" + newItem.getCreated());
			todoItems.add(newItem);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		
	}
}
