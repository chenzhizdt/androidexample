package org.instorm.example.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class SlideListView extends ListView{
	
	private static final String TAG = "SlideListView";
	
	/**
	 * 滑动类
	 */
	private Scroller mScroller;
	
	/**
	 * 速度追踪类
	 */
	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 600;
	
	/**
	 * 移动事件监听器
	 */
	private RemoveListener mRemoveListener;
	
	/**
	 * 移动方向
	 */
	private RemoveDirection mRemoveDirection;
	
	/**
	 * 手指按下时的x坐标
	 */
	private int mDownX;
	private int mCurMoveX;
	
	/**
	 * 手指按下时的y坐标
	 */
	private int mDownY;
	private int mCurMoveY;
	
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	
	/**
	 * 触碰的ListItem
	 */
	private View mTargetItem;
	
	/**
	 * 滑动Item的位置
	 */
	private int mSlidePosition;
	
	/**
	 * 标记是否响应滑动默认为false
	 */
	private boolean isSlide = false;
	
	/**
	 * 触发remove事件的滑动距离
	 */
	private float mMinSlide = 1.0f/2.0f;
	
	/**
	 * 用户滑动的最小距离
	 */
	private int mTouchSlop;
	
	public SlideListView(Context context){
		this(context, null);
	}
	
	public SlideListView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	public void setRemoveListener(RemoveListener removeListener){
		this.mRemoveListener = removeListener;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			addVelocityTracker(ev);
			
			//如果滑动还没完成，则直接返回
			if(!mScroller.isFinished()){
				return super.dispatchTouchEvent(ev);
			}
			
			mDownX = (int) ev.getX();
			mCurMoveX = mDownX;
			mDownY = (int) ev.getY();
			mCurMoveY = mDownY;
			
			mSlidePosition = pointToPosition(mDownX, mDownY);
			
			//如果触碰点不在任何一个item范围内，则直接返回
			if(mSlidePosition == AdapterView.INVALID_POSITION){
				break;
			}
			
			//获取触碰到的item
			mTargetItem = getChildAt(mSlidePosition - getFirstVisiblePosition());
			break;
		case MotionEvent.ACTION_MOVE:
			//当触控操作大于一定速度SNAP_VELOCITY或超过最小响应距离时，响应拖动事件
			if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY  
                    || (Math.abs(ev.getX() - mDownX) > mTouchSlop && Math  
                            .abs(ev.getY() - mDownY) < mTouchSlop)) {  
                isSlide = true;  
            }
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isSlide && mSlidePosition != AdapterView.INVALID_POSITION){
			addVelocityTracker(ev);
			int x = (int) ev.getX();
			switch(ev.getAction()){
			case MotionEvent.ACTION_MOVE:
				int deltaX = mCurMoveX - x;
				mCurMoveX = x;
				mTargetItem.scrollBy(deltaX, 0);
				break;
			case MotionEvent.ACTION_UP:
				int velocityX = getScrollVelocity(); 
				if (velocityX > SNAP_VELOCITY) {  
                    scrollRight();  
                } else if (velocityX < -SNAP_VELOCITY) {  
                    scrollLeft();  
                } else {  
                    scrollByDistanceX();  
                }
				recycleVelocityTracker();  
                // 手指离开的时候就不响应左右滚动  
                isSlide = false;  
                break;
			default:
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	/** 
     * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值 
     */  
    private void scrollRight() {  
        mRemoveDirection = RemoveDirection.RIGHT;  
        final int delta = (mScreenWidth + mTargetItem.getScrollX());  
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item  
        mScroller.startScroll(mTargetItem.getScrollX(), 0, -delta, 0,  
                Math.abs(delta));
        postInvalidate(); // 刷新itemView  
    }
    
    /** 
     * 向左滑动，根据上面我们知道向左滑动为正值 
     */  
    private void scrollLeft() {  
    	mRemoveDirection = RemoveDirection.LEFT;  
        final int delta = (mScreenWidth - mTargetItem.getScrollX());  
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item  
        mScroller.startScroll(mTargetItem.getScrollX(), 0, delta, 0,  
                Math.abs(delta));  
        postInvalidate(); // 刷新itemView  
    }
    
    /** 
     * 根据手指滚动mTargetItem的距离来判断是滚动到开始位置还是向左或者向右滚动 
     */  
    private void scrollByDistanceX() {  
        // 如果向左滚动的距离大于屏幕的三分之一，就让其删除  
        if (mTargetItem.getScrollX() >= mScreenWidth / 3) {  
            scrollLeft();
        } else if (mTargetItem.getScrollX() <= -mScreenWidth / 3) {  
            scrollRight();  
        } else {  
            // 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动  
        	mTargetItem.scrollTo(0, 0);  
        }  
  
    }
	
    @Override  
    public void computeScroll() {  
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，  
        if (mScroller.computeScrollOffset()) {  
            // 让ListView item根据当前的滚动偏移量进行滚动  
            mTargetItem.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
              
            postInvalidate();  
  
            // 滚动动画结束的时候调用回调接口  
            if (mScroller.isFinished()) {  
                if (mRemoveListener == null) {  
                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");  
                }  
                  
                mTargetItem.scrollTo(0, 0);  
                mRemoveListener.onRemove(mRemoveDirection, mSlidePosition);  
            }  
        }  
    }
    
	/**
	 * 添加滑动速度追踪器
	 * @param ev
	 */
	private void addVelocityTracker(MotionEvent ev){
		if (mVelocityTracker == null) {  
			mVelocityTracker = VelocityTracker.obtain();  
        }
		mVelocityTracker.addMovement(ev);
	}
	
	/**
	 * 移除滑动速度追踪器
	 */
	private void recycleVelocityTracker(){
		if (mVelocityTracker != null){
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	
	/** 
     * 获取X方向的滑动速度,大于0向右滑动，反之向左 
     * @return 
     */  
    private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();  
        Log.v(TAG, "当前速度为：" + velocity);
        return velocity;  
    }
	
	public interface RemoveListener{
		public void onRemove(RemoveDirection direction, int position);
	}
	
	public enum RemoveDirection{
		LEFT, RIGHT
	}
}
