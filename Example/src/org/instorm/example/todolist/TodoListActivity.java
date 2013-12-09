package org.instorm.example.todolist;

import org.instorm.example.R;
import org.instorm.example.todolist.view.TodoListFragment;
import org.instorm.example.todolist.view.AddTodoFragment;
import org.instorm.example.todolist.view.OnAddTodoListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TodoListActivity extends FragmentActivity{
	
	private TodoListFragment itemsFragment;
	private AddTodoFragment newItemFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todolist);
		itemsFragment = (TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.itemsFragment);
		newItemFragment = (AddTodoFragment) getSupportFragmentManager().findFragmentById(R.id.newItemFragment);
		newItemFragment.setOnNewItemListener(new OnAddTodoListener() {
			
			@Override
			public void onNewItemAdded(String newItem) {
				itemsFragment.addNewItem(newItem);
			}
		});
	}
}
