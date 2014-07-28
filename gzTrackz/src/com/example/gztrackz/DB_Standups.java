package com.example.gztrackz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Standups {

	// debugging tags
	public static final String TAG = "DB_Standups";

	public static final String KEY_EMAILADD = "email";
	public static final String KEY_DATE = "date";
	public static final String KEY_STANDUPS_YESTERDAY = "standups_yesterday";
	public static final String KEY_STANDUPS_TODO = "standups_todo";
	public static final String KEY_STANDUPS_HINDRANCE = "standups_hindrance";

	public static final int COL_EMAILADD = 0;
	public static final int COL_DATE = 1;
	public static final int COL_STANDUPS_YESTERDAY = 2;
	public static final int COL_STANDUPS_TODO = 3;
	public static final int COL_STANDUPS_HINDRANCE = 4;

	public static final String[] ALL_KEYS = { KEY_EMAILADD, KEY_DATE,
			KEY_STANDUPS_YESTERDAY, KEY_STANDUPS_TODO, KEY_STANDUPS_HINDRANCE };

	public static final String TABLE_NAME = "user_standups";
	public static final int DB_VERSION = 1;

	private static final String DATABASE_CREATE_SQL = "create table "
			+ TABLE_NAME + " (" + KEY_EMAILADD + " varchar, "

			+ KEY_DATE + " timestamp not null, " + KEY_STANDUPS_YESTERDAY
			+ " text not null, " + KEY_STANDUPS_TODO + " text not null, "
			+ KEY_STANDUPS_HINDRANCE + " text" + " ,PRIMARY KEY ("
			+ KEY_EMAILADD + "," + KEY_DATE + "));";

	private char quote = '"';
	private Context appContext;

	private DB_Standups_Helper standups_DBHelper;
	private SQLiteDatabase sql_db;
	
	private int search_standups_by_email = 1;
	
	public DB_Standups(Context context) {

		appContext = context;
		standups_DBHelper = new DB_Standups_Helper(appContext);
	}

	// Open the database connection.
	public DB_Standups open() {
		sql_db = standups_DBHelper.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		standups_DBHelper.close();
	}

	// Add a new set of values to the database.
	public long insertRow(String emailAdd, String date,
			String standups_yesterday, String standups_todo,
			String standups_hindrance) {

		ContentValues contentValues = getContentValues(emailAdd, date,
				standups_yesterday, standups_todo, standups_hindrance);

		// Insert it into the database.
		return sql_db.insert(TABLE_NAME, null, contentValues);
	}
	
	public List<Standup> getAllDay(String email,String date1, String date2){
		List<Standup> flag = new ArrayList();
		String where = KEY_EMAILADD + " = " + quote + email + quote + " AND "
				+ KEY_DATE + " >= " + quote + date1 + quote + " AND " + KEY_DATE + "< " + quote + date2 + quote;
		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);

		if (cursor != null) {		
			cursor.moveToFirst();			
			if(cursor.getCount()>0){	
				do{						
					flag.add(new Standup(cursor.getString(COL_EMAILADD),cursor.getString(COL_DATE),cursor.getString(COL_STANDUPS_YESTERDAY),cursor.getString(COL_STANDUPS_TODO),cursor.getString(COL_STANDUPS_HINDRANCE)));
				}while(cursor.moveToNext());
			}
		}
						
		return flag;		
	}
	// Change an existing row to be equal to new data.
	public boolean updateRow(String emailAdd, String date,
			String standups_yesterday, String standups_todo,
			String standups_hindrance) {

		String where = KEY_EMAILADD + "=" +quote+ emailAdd + quote + " AND " + KEY_DATE + "=" + quote + date + quote;

		ContentValues contentValues = getContentValues(emailAdd, date,
				standups_yesterday, standups_todo, standups_hindrance);

		// Insert it into the database.
		return sql_db.update(TABLE_NAME, contentValues, where, null) != 0;
	}
	
	
	public boolean putRow(String emailAdd, String date,
			String standups_yesterday, String standups_todo,
			String standups_hindrance) {
		boolean flag = false;
		if(!updateRow(emailAdd,date,standups_yesterday,standups_todo,standups_hindrance)){
			insertRow(emailAdd, date, standups_yesterday, standups_todo, standups_hindrance);
			flag = true;
		}
		return flag;
	}
	public ContentValues getContentValues(String emailAdd, String date,
			String standups_yesterday, String standups_todo,
			String standups_hindrance) {

		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_EMAILADD, emailAdd);
		contentValues.put(KEY_DATE, date);
		contentValues.put(KEY_STANDUPS_YESTERDAY, standups_yesterday);
		contentValues.put(KEY_STANDUPS_TODO, standups_todo);
		contentValues.put(KEY_STANDUPS_HINDRANCE, standups_hindrance);

		return contentValues;
	}

	// Delete a row from the database, by date (primary key)
	public boolean deleteRow(String date) {
		String where = KEY_DATE + "=" + date;
		return sql_db.delete(TABLE_NAME, where, null) != 0;
	}

	public void deleteAll() {
		Cursor cursor = getAllRows();

		int rowId = cursor.getColumnIndexOrThrow(KEY_EMAILADD);

		if (cursor.moveToFirst()) {
			do {
				deleteRow(cursor.getString(rowId));
			} while (cursor.moveToNext());
		}

		cursor.close();
	}

	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	public void removeAll()
	{
		sql_db.delete(TABLE_NAME, null, null);
	}
	public List<Standup> getAllRowOf(String email){
		List<Standup> flag = new ArrayList();
		String where = KEY_EMAILADD + " = " + quote + email + quote;
		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);

		if (cursor != null) {		
			cursor.moveToFirst();			
			if(cursor.getCount()>0){	
				do{						
						flag.add(new Standup(cursor.getString(COL_EMAILADD),cursor.getString(COL_DATE),cursor.getString(COL_STANDUPS_YESTERDAY),cursor.getString(COL_STANDUPS_TODO),cursor.getString(COL_STANDUPS_HINDRANCE)));
				}while(cursor.moveToNext());
			}
		}
						
		return flag;
	}
	

	public Standup getLatestRowOf(String email){
		Standup flag = new Standup();
		String where = KEY_EMAILADD + " = " + quote + email + quote;
		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, "2 DESC", null);
		if (cursor != null) {
			cursor.moveToFirst();
			if(cursor.getCount()>0){	
				flag.setEmail(email);
				flag.setDate(cursor.getString(COL_DATE));
				flag.setStandup_y(cursor.getString(COL_STANDUPS_YESTERDAY));
				flag.setStandup_todo(cursor.getString(COL_STANDUPS_TODO));
				flag.setProblem(cursor.getString(COL_STANDUPS_HINDRANCE));
			}
		}
						
		return flag;
	}
	
	// Get a specific row (by email or date)
	public Cursor getRow(String search_entry, int search_flag) {
		
		String where = "";

		if(search_standups_by_email == search_flag) {
			where = KEY_EMAILADD + "=" + search_entry;
		} else {
			where = KEY_DATE + "=" + search_entry;
		}
		
		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	private static class DB_Standups_Helper extends SQLiteOpenHelper {

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;

		DB_Standups_Helper(Context context) {
			super(context, TABLE_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sql_db, int oldVersion,
				int newVersion) {
			Log.w(TAG, "Upgrading application's database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data!");

			// Destroy old database:
			sql_db.execSQL(DROP_TABLE);

			// Recreate new database
			onCreate(sql_db);
		}
	}

}
