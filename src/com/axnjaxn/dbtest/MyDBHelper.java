package com.axnjaxn.dbtest;

import java.util.ArrayList;

import org.w3c.dom.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
	public static final String TABLE_NAME = "data";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CONTENT = "content";
	private String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_CONTENT};

	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_NAME 
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_CONTENT + " text);";

	private SQLiteDatabase database;
	
	public MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MyDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public ContentClass insert(String name, String content) {
		ContentValues values = new ContentValues();
	    values.put(COLUMN_NAME, name);
	    values.put(COLUMN_CONTENT, content);
	    long id = database.insert(TABLE_NAME, null, values);
	    return new ContentClass(name, content, id);
	}
	
	public void delete(long id) {
		database.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
	}
	
	public ArrayList<ContentClass> getList() {
		ArrayList<ContentClass> displayList = new ArrayList<ContentClass>();

	    Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_CONTENT);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	long id = cursor.getLong(0);
	    	String name = cursor.getString(1);
	    	String title = cursor.getString(2);
	    	displayList.add(new ContentClass(name, title, id));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return displayList;
	}
}
