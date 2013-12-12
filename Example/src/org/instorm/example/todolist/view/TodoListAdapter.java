package org.instorm.example.todolist.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.instorm.example.R;
import org.instorm.example.todolist.model.TodoItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {
	
	private int resource;
	
	public TodoListAdapter(Context context, int resource, List<TodoItem> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout todoView;
		
		TodoItem item = getItem(position);
		
		String taskString = item.getTask();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINESE);
		String dateString = sdf.format(item.getCreated());
		
		if(convertView == null){
			todoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, todoView, true);
		} else {
			todoView = (LinearLayout) convertView;
		}
		TextView rowDate = (TextView) todoView.findViewById(R.id.rowDate);
		TextView row = (TextView) todoView.findViewById(R.id.row);
		
		rowDate.setText(dateString);
		row.setText(taskString);
		
		return todoView;
	}
	
}
