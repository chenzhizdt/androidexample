package org.instorm.example;

import java.util.ArrayList;

import org.instorm.example.R;
import org.instorm.view.BugFragment;
import org.instorm.view.DocumentFragment;
import org.instorm.view.MainFragmentPagerAdapter;
import org.instorm.view.MemberFragment;
import org.instorm.view.TodoFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private TextView todo;
	private TextView bug;
	private TextView member;
	private TextView document;
	
	private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        Log.v(TAG, "-----username " + username + "-----");
		Log.v(TAG, "-----password " + password + "-----");
		todo = (TextView) findViewById(R.id.tv_todo);
		bug = (TextView) findViewById(R.id.tv_bug);
		member = (TextView) findViewById(R.id.tv_member);
		document = (TextView) findViewById(R.id.tv_document);
		mViewPager = (ViewPager) findViewById(R.id.vPager);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		TodoFragment todoFragment = new TodoFragment();
		DocumentFragment documentFragment = new DocumentFragment();
		MemberFragment memberFragment = new MemberFragment();
		BugFragment bugFragment = new BugFragment();
		fragments.add(todoFragment);
		fragments.add(memberFragment);
		fragments.add(documentFragment);
		fragments.add(bugFragment);
		MainFragmentPagerAdapter mainFragmentPaperAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mainFragmentPaperAdapter);
		mViewPager.setCurrentItem(0);
		todo.setOnClickListener(new ButtonClickListener(mViewPager, 0));
		member.setOnClickListener(new ButtonClickListener(mViewPager, 1));
		document.setOnClickListener(new ButtonClickListener(mViewPager, 2));
		bug.setOnClickListener(new ButtonClickListener(mViewPager, 3));
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    class ButtonClickListener implements OnClickListener{
    	
    	private ViewPager mViewPager;
    	private int index;
    	
    	public ButtonClickListener(ViewPager mViewPager, int index){
    		this.mViewPager = mViewPager;
    		this.index = index;
    	}
    	
		@Override
		public void onClick(View arg0) {
			mViewPager.setCurrentItem(index);
		}
    }
}
