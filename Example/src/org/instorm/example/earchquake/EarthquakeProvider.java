package org.instorm.example.earchquake;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class EarthquakeProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://org.instorm.example.earthquakeprovider/earthquakes");
	
	public static final String KEY_ID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_DETAILS = "detailes";
	public static final String KEY_SUMMARY = "summary";
	public static final String KEY_LOCATION_LAT = "latitude";
	public static final String KEY_LOCATION_LNG = "longitude";
	public static final String KEY_MAGNITUDE = "magnitude";
	public static final String KEY_LINK = "link";
	
	public static final int QUAKES = 1;
	public static final int QUAKE_ID = 2;
	
	private EarthquakeDatabaseHelper dbHelper;
	
	private static final UriMatcher uriMatcher;
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("org.instorm.example.earthquakeprovider", "earthquakes", QUAKES);
		uriMatcher.addURI("org.instorm.example.earthquakeprovider", "earthquakes/#", QUAKE_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deleteCount;
		
		switch(uriMatcher.match(uri)){
		case QUAKE_ID:
			String rowId = uri.getPathSegments().get(1);
			deleteCount = db.delete(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, 
					KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " and (" + selection + ")" : ""), 
					selectionArgs);
			break;
		case QUAKES:
			deleteCount = db.delete(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, selection, selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		notifyChange(uri);
		
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)){
		case QUAKES: return "vnd.android.cursor.dir/vnd.instorm.earthquake";
		case QUAKE_ID: return "vnd.android.cursor.item/vnd.instorm.earthquake";
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		long rowId = db.insert(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, "quake", values);
		
		if(rowId > 0){
			Uri uri = ContentUris.withAppendedId(_uri, rowId);
			notifyChange(uri);
			return uri;
		}
		
		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new EarthquakeDatabaseHelper(context, EarthquakeDatabaseHelper.DATABASE_NAME, null, EarthquakeDatabaseHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE);
		
		switch(uriMatcher.match(uri)){
		case QUAKE_ID:
			qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
		default: break;
		}
		
		String groupBy = null;
		String having = null;
		String orderBy = null;
		
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = KEY_DATE;
		} else {
			orderBy = sortOrder;
		}
		
		Cursor cursor = qb.query(db, projection, selection, selectionArgs, groupBy, having, orderBy);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		int updateCount;
		
		switch(uriMatcher.match(uri)){
		case QUAKE_ID:
			String rowId = uri.getPathSegments().get(1);
			updateCount = db.update(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, values,
					KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " and (" + selection + ")" : ""), 
					selectionArgs);
			break;
		case QUAKES:
			updateCount = db.update(EarthquakeDatabaseHelper.EARTHQUAKE_TABLE, values, selection, selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		notifyChange(uri);
		
		return updateCount;
	}
	
	private void notifyChange(Uri uri){
		getContext().getContentResolver().notifyChange(uri, null);
	}
	
	private static class EarthquakeDatabaseHelper extends SQLiteOpenHelper{
		
		private static final String TAG = "EarthquakeDatabaseHelper";
		
		private static final String DATABASE_NAME = "earthquakes.db";
		private static final int DATABASE_VERSION = 1;
		private static final String EARTHQUAKE_TABLE = "earthquakes";
		
		private static final String DATABASE_CREATE = 
				"create table " + EARTHQUAKE_TABLE + " ("
				+ KEY_ID + " integer primary key autoincrement, "
				+ KEY_DATE + " integer, "
				+ KEY_DETAILS + " text, "
				+ KEY_SUMMARY + " text, "
				+ KEY_LOCATION_LAT + " float, "
				+ KEY_LOCATION_LNG + " float, "
				+ KEY_MAGNITUDE + " float, "
				+ KEY_LINK + " text);";
		
//		private SQLiteDatabase earthquakeDB;

		public EarthquakeDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to version " + newVersion + ", which will destory all old data!");
			db.execSQL("drop table if exists " + EARTHQUAKE_TABLE);
			onCreate(db);
		}
		
	}

}
