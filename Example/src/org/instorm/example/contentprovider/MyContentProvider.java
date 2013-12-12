package org.instorm.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://org.instorm.example.contentprovider/elements");
	
	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;
	
	private static final UriMatcher URI_MATCHER;
	
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI("org.instorm.example.contentprovider", "elements", ALL_ROWS);
		URI_MATCHER.addURI("org.instorm.example.contentprovider", "elements/#", SINGLE_ROW);
	}
	
	public static final String KEY_ID = "_id";
	
	public static final String KEY_COLUMN_1_NAME = "KEY_COLUMN_1_NAME";

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
