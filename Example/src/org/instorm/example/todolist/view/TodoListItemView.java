package org.instorm.example.todolist.view;

import org.instorm.example.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TodoListItemView extends TextView {
	
	private  static final String TAG = "ToDoListItemView";
	
	private Paint marginPaint;
	private Paint linePaint;
	private int paperColor;
	private float margin;

	public TodoListItemView(Context context) {
		super(context);
		init();
	}
	
	public TodoListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TodoListItemView(Context context, AttributeSet attrs, int ds) {
		super(context, attrs, ds);
		init();
	}
	private void init(){
		Resources myResources = getResources();
		
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		marginPaint.setColor(myResources.getColor(R.color.notepad_margin));
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(myResources.getColor(R.color.notepad_lines));
		
		paperColor = myResources.getColor(R.color.notepad_paper);
		margin = myResources.getDimension(R.dimen.notepad_margin);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(paperColor);
		//绘制边缘
		canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
		canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);
		
		canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);
		
		canvas.save();
		canvas.translate(margin, 0);
		Log.v(TAG, "Height: " + getMeasuredHeight());
		super.onDraw(canvas);
		canvas.restore();
	}
}
