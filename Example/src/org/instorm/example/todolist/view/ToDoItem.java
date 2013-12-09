package org.instorm.example.todolist.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoItem {
	
	String task;
	Date created;
	
	public ToDoItem(String task){
		this(task, new Date(java.lang.System.currentTimeMillis()));
	}
	
	public ToDoItem(String task, Date created){
		this.task = task;
		this.created = created;
	}
	
	public String getTask() {
		return task;
	}
	
	public Date getCreated() {
		return created;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String dateString = sdf.format(created);
		return  task + " [" + dateString + "]";
	}
}
