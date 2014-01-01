package org.instorm.example.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TouchTextView extends TextView{
	
	private static final String TAG = "TouchTextView";
	
	public TouchTextView(Context context) {
		super(context);
	}
	
	public TouchTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		addLog("TouchTextView onTouchEvent");
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		addLog("TouchTextView dispatchTouchEvent");
		return super.dispatchTouchEvent(event);
	}
	
	private void addLog(String msg){
		TouchActivity.addLog(msg);
	}
}
