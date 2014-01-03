package org.instorm.example.slide;

import java.util.ArrayList;

import org.instorm.example.R;
import org.instorm.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

public class SlideMenuFrameLayout extends FrameLayout{
	
	/**
	 * 滑动器
	 */
	private Scroller mScroller;
	/**
	 * 屏幕大小
	 */
	private int mScreenWidth;
	/**
	 * 滑动完成后的边界
	 */
	private int mEdge = 50;
	/**
	 * 滑动时间
	 */
	private int mTime = 500;
	/**
	 * 需要滑动的视图
	 */
	private LinearLayout mScrollView;
	/**
	 * 菜单栏状态
	 */
	private MenuState mState = MenuState.CLOSED;
	/**
	 * 菜单
	 */
	private ListView mMenuContainer;
	/**
	 * 菜单项
	 */
	private ArrayList<String> mMenus;
	/**
	 * 菜单项适配器
	 */
	private ArrayAdapter<String> mAdapter;
	
	private Context mContext;
	
	public SlideMenuFrameLayout(Context context) {
		this(context, null);
	}
	
	public SlideMenuFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressWarnings("deprecation")
	public SlideMenuFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		mScreenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		mContext = context;
	}
	
	public void setMenus(ArrayList<String> menus){
		boolean flag = mMenus == null ? true : false;
		this.mMenus = menus;
		if(flag){
			initMenuContainer();
		}
	}
	
	private void initMenuContainer(){
		mScrollView = (LinearLayout)findViewById(R.id.ll_move_view);
		mMenuContainer = (ListView)findViewById(R.id.lv_slide_menu);
		mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mMenus);
		mMenuContainer.setAdapter(mAdapter);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()){
			mScrollView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			if (mScroller.isFinished()) {
				if(mState == MenuState.OPENING){
					mState = MenuState.OPENED;
				}
				if(mState == MenuState.CLOSING){
					mState = MenuState.CLOSED;
				}
			}
		}
	}
	
	public void openMenu(MoveDirection openDirection){
		if(mState == MenuState.CLOSED){
			mState = MenuState.OPENING;
			int scrollX = mScreenWidth - Utils.dip2px(mContext, mEdge);
			if(openDirection == MoveDirection.RIGHT){
				scrollX = -scrollX;
			}
			mScroller.startScroll(0, 0, scrollX, 0, mTime);
			postInvalidate();
		}
	}
	
	public void closeMenu(){
		if(mState == MenuState.OPENED){
			mState = MenuState.CLOSING;
			mScroller.startScroll(mScrollView.getScrollX(), 0, -mScrollView.getScrollX(), 0, mTime);
			postInvalidate();
		}
	}
	
	public MenuState getMenuState(){
		return mState;
	}
	
	public enum MoveDirection{
		LEFT, RIGHT
	}
	
	public enum MenuState{
		OPENED, OPENING, CLOSED, CLOSING
	}
}
