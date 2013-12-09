package org.instorm.example.todolist;

import org.instorm.example.R;
import org.instorm.example.todolist.view.ItemsFragment;
import org.instorm.example.todolist.view.NewItemFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ToDoListActivity extends FragmentActivity{
	
	private ItemsFragment itemsFragment;
	private NewItemFragment newItemFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todolist);
		itemsFragment = (ItemsFragment) getSupportFragmentManager().findFragmentById(R.id.itemsFragment);
		newItemFragment = (NewItemFragment) getSupportFragmentManager().findFragmentById(R.id.newItemFragment);
		newItemFragment.setOnNewItemListener(new OnNewItemListener() {
			
			@Override
			public void onNewItemAdded(String newItem) {
				itemsFragment.addNewItem(newItem);
			}
		});
	}
}
