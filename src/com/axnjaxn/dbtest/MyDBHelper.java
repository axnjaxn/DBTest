package com.axnjaxn.dbtest;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
	public static final String TABLE_NAME = "data";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_AGE = "age";
	private String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_CONTENT, COLUMN_AGE};

	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = "create table " + TABLE_NAME 
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_CONTENT + " text,"
			+ COLUMN_AGE + " integer);";

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
		if (oldVersion == 1) {
			int defaultAge = 20;
			db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_AGE + " INTEGER DEFAULT " + defaultAge);
		}
	}
	
	public ContentClass insert(String name, String content, int age) {
		ContentValues values = new ContentValues();
	    values.put(COLUMN_NAME, name);
	    values.put(COLUMN_CONTENT, content);
	    values.put(COLUMN_AGE, age);
	    long id = database.insert(TABLE_NAME, null, values);
	    return new ContentClass(name, content, id, age);
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
	    	int age = cursor.getInt(3);
	    	displayList.add(new ContentClass(name, title, id, age));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return displayList;
	}
}
