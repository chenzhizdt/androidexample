package org.instorm.example.todolist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class TodoContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://org.instorm.example.todocontentprovider/todoitems");
	
	public static final String KEY_ID = "_id";
	public static final String KEY_TASK = "task";
	public static final String KEY_CREATION_DATE = "creation_date";
	
	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;
	
	private static final UriMatcher URI_MATCHER;
	
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI("org.instorm.example.todocontentprovider", "todoitems", ALL_ROWS);
		URI_MATCHER.addURI("org.instorm.example.todocontentprovider", "todoitems/#", SINGLE_ROW);
	}
	
	private MySQLiteOpenHelper mySQLiteOpenHelper;
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
		
		switch(URI_MATCHER.match(uri)){
		case SINGLE_ROW:
			String rowId = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			default: break;
		}
		
		if(selection == null){
			selection = "1";
		}
		
		int deleteCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE, selection, selectionArgs);
		
		notifyChange(uri);
		
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		switch(URI_MATCHER.match(uri)){
		case ALL_ROWS: return "vnd.android.cursor.dir/vnd.instorm.todos";
		case SINGLE_ROW: return "vnd.android.cursor.item/vnd.instorm.todos";
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
		
		String nullColumnHack = null;
		
		long id = db.insert(MySQLiteOpenHelper.DATABASE_TABLE, nullColumnHack, values);
		
		if(id > -1){
			Uri insertId = ContentUris.withAppendedId(uri, id);
			notifyChange(insertId);
			return insertId;
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		
		mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(),
				MySQLiteOpenHelper.DATABASE_NAME,
				null,
				MySQLiteOpenHelper.DATABASE_VERSION);
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
		
		String groupBy = null;
		String having = null;
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);
		
		switch(URI_MATCHER.match(uri)){
		case SINGLE_ROW:
			String rowId = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(KEY_ID + "=" + rowId);
			default: break;
		}
		
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
		
		switch(URI_MATCHER.match(uri)){
		case SINGLE_ROW:
			String rowId = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			default: break;
		}
		
		int updateCount = db.update(MySQLiteOpenHelper.DATABASE_TABLE, values, selection, selectionArgs);
		
		notifyChange(uri);
		
		return updateCount;
	}
	
	//通知所有观察者，数据集已改变
	private void notifyChange(Uri uri){
		getContext().getContentResolver().notifyChange(uri, null);
	}
	
	private static class MySQLiteOpenHelper extends SQLiteOpenHelper{
		
		public static final String KEY_ID = "_id";
		public static final String KEY_TASK = "task";
		public static final String KEY_CREATION_DATE = "creation_date";
		
		public static final String DATABASE_NAME = "todoDatabase.db";
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_TABLE = "todoItemTable";
		
		private static final String DATABASE_CREATE = "create table " +
				DATABASE_TABLE + " (" + KEY_ID +
				" integer primary key autoincrement, " +
				KEY_TASK + " text not null, " +
				KEY_CREATION_DATE + " long);";

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TaskDBAdapter",
					"Upgrading from version " + oldVersion + " to " + newVersion + ", which will destory all old data");
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
}
