package org.instorm.example.slide;

import org.instorm.example.R;
import org.instorm.example.slide.SlideMenuFrameLayout.MenuState;
import org.instorm.example.slide.SlideMenuFrameLayout.MoveDirection;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class SlideMenuActivity extends Activity{
	
	private SlideMenuFrameLayout slmfContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_menu);
		slmfContainer = (SlideMenuFrameLayout) findViewById(R.id.slmf_container);
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
