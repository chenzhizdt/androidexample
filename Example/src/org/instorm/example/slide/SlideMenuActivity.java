package org.instorm.example.slide;

import java.util.ArrayList;

import org.instorm.example.R;
import org.instorm.example.slide.SlideMenuFrameLayout.MenuState;
import org.instorm.example.slide.SlideMenuFrameLayout.MoveDirection;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class SlideMenuActivity extends Activity{
	
	private SlideMenuFrameLayout slmfContainer;
	
	private ArrayList<String> mMenus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_menu);
		slmfContainer = (SlideMenuFrameLayout) findViewById(R.id.slmf_container);
		mMenus = new ArrayList<String>();
		for(int i = 0; i < 20; i++){
			mMenus.add("Menu " + (i + 1));
		}
		slmfContainer.setMenus(mMenus);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			if(slmfContainer.getMenuState() == MenuState.CLOSED){
				slmfContainer.openMenu(MoveDirection.RIGHT);
			}
			if(slmfContainer.getMenuState() == MenuState.OPENED){
				slmfContainer.closeMenu();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
