package org.instorm.example.compass;

import org.instorm.example.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

public class CompassView extends View {
	
	private float bearing;
	private Paint circlePaint;
	private Paint textPaint;
	private Paint markerPaint;
	private String northString;
	private String southString;
	private String westString;
	private String eastString;
	private int textHeight;
	
	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
	}

	public CompassView(Context context) {
		super(context);
		initCompassView();
	}
	
	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initCompassView();
	}
	
	protected void initCompassView(){
		setFocusable(true);
		
		Resources r = getResources();
		
		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.background_color));
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		northString = r.getString(R.string.cardinal_north);
		eastString = r.getString(R.string.cardinal_east);
		southString = r.getString(R.string.cardinal_south);
		westString = r.getString(R.string.cardinal_west);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(R.color.text_color));
		
		textHeight = (int)textPaint.measureText("yY");
		
		markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markerPaint.setColor(r.getColor(R.color.marker_color));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = measure(widthMeasureSpec);
		int measureHeight = measure(heightMeasureSpec);
		
		int d = Math.min(measuredWidth, measureHeight);
		
		setMeasuredDimension(d, d);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		int mMeasuredWidth = getMeasuredWidth();
		int mMeasuredHeight = getMeasuredHeight();
		
		int px = mMeasuredWidth / 2;
		int py = mMeasuredHeight / 2;
		
		int radius = Math.min(px, py);
		
		canvas.drawCircle(px, py, radius, circlePaint);
		
		canvas.save();
		canvas.rotate(-bearing, px, py);
		
		int textWidth = (int) textPaint.measureText("W");
		int cardinalX = px - textWidth / 2;
		int cardinalY = py - radius + textHeight;
		
		for (int i = 0; i < 24; i++){
			canvas.drawLine(px, py-radius, px, py-radius+10, markerPaint);
			
			canvas.save();
			canvas.translate(0, textHeight);
			
			if(i % 6 == 0){
				String dirString = "";
				switch(i){
				case(0):{
					dirString = northString;
					int arrowY = 2*textHeight;
					canvas.drawLine(px, arrowY, px-5, 3*textHeight, markerPaint);
					canvas.drawLine(px, arrowY, px+5, 3*textHeight, markerPaint);
					canvas.drawLine(px-5, 3*textHeight, px+5, 3*textHeight, markerPaint);
					canvas.drawLine(px, py - textHeight, px, 3*textHeight, markerPaint);
					break;
				}
				case(6): dirString = eastString; break;
				case(12): dirString = southString; break;
				case(18): dirString = westString; break;
				}
				canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
			} else if(i % 3 == 0){
				String angle = String.valueOf(i*15);
				float angleTextWidth = textPaint.measureText(angle);
				
				int angleTextX = (int) (px - angleTextWidth / 2);
				int angleTextY = py - radius + textHeight;
				canvas.drawText(angle, angleTextX, angleTextY, textPaint);
			}
			canvas.restore();
			
			canvas.rotate(15, px, py);
		}
		canvas.restore();
	}
	
	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		super.dispatchPopulateAccessibilityEvent(event);
		if(isShown()){
			String bearingStr = String.valueOf(bearing);
			if(bearingStr.length() > AccessibilityEvent.MAX_TEXT_LENGTH){
				bearingStr = bearingStr.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH);
				event.getText().add(bearingStr);
			}
			return true;
		}
		return false;
	}
	
	private int measure(int measureSpec){
		int result = 0;
		
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		if(specMode == MeasureSpec.UNSPECIFIED){
			result = 200;
		} else {
			result = specSize;
		}
		return result;
	}
}
