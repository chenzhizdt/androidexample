package org.instorm.example.todolist.view;

import org.instorm.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class AddTodoFragment extends Fragment {
	
	private OnAddTodoListener onNewItemListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_todo, container, false);
		final EditText et = (EditText) view.findViewById(R.id.myEditText);
			et.setOnKeyListener(new OnKeyListener() {
				
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					if((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) || (keyCode == KeyEvent.KEYCODE_ENTER)){
						if(onNewItemListener != null){
							onNewItemListener.onNewItemAdded(et.getText().toString());
						}
						et.setText("");
						return true;
					}
				}
				return false;
			}
		});
		return view;
	}
	public void setOnNewItemListener(OnAddTodoListener onNewItemListener){
		this.onNewItemListener = onNewItemListener;
	}
}
