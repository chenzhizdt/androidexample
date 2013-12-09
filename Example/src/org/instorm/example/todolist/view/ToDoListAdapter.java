package org.instorm.example.todolist.view;

import java.text.SimpleDateFormat;
import java.util.List;

import org.instorm.example.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToDoListAdapter extends ArrayAdapter<ToDoItem> {
	
	private int resource;
	
	public ToDoListAdapter(Context context, int resource, List<ToDoItem> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout todoView;
		
		ToDoItem item = getItem(position);
		
		String taskString = item.getTask();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
