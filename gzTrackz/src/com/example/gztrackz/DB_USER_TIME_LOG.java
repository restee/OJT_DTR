package com.example.gztrackz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_USER_TIME_LOG {

	// debugging tags
	public static final String TAG = "DB_User_Time_Log";

	public static final String KEY_EMAILADD = "email";
	public static final String KEY_TIMEIN = "time_in";
	public static final String KEY_TIMEOUT = "time_out";

	public static final int COL_EMAILADD = 0;
	public static final int COL_TIMEIN = 1;
	public static final int COL_TIMEOUT = 2;

	public static final String[] ALL_KEYS = { KEY_EMAILADD, KEY_TIMEIN,
			KEY_TIMEOUT };

	public static final String TABLE_NAME = "user_time_logs";
	public static final int DB_VERSION = 1;

	private static final String DATABASE_CREATE_SQL = "create table "
			+ TABLE_NAME + " (" + KEY_EMAILADD + " varchar primary key, "

			+ KEY_TIMEIN + " timestamp not null, " + KEY_TIMEOUT
			+ " timestamp not null);";

	private Context appContext;

	private DB_User_Time_Log_Helper user_time_log_helper;
	private SQLiteDatabase sql_db;

	public DB_USER_TIME_LOG(Context context) {

		appContext = context;
		user_time_log_helper = new DB_User_Time_Log_Helper(appContext);
	}

	// Open the database connection.
	public DB_USER_TIME_LOG open() {
		sql_db = user_time_log_helper.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		user_time_log_helper.close();
	}

	// Add a new set of values to the database.
	public long insertRow(String emailAdd, String timein, String timeout) {

		ContentValues contentValues = getContentValues(emailAdd, timein,
				timeout);

		// Insert it into the database.
		return sql_db.insert(TABLE_NAME, null, contentValues);
	}

	// Change an existing row to be equal to new data.
	public boolean updateRow(String emailAdd, String timein, String timeout) {

		String where = KEY_EMAILADD + "=" + emailAdd;

		ContentValues contentValues = getContentValues(emailAdd, timein,
				timeout);

		// Insert it into the database.
		return sql_db.update(TABLE_NAME, contentValues, where, null) != 0;
	}

	public ContentValues getContentValues(String emailAdd, String timein,
			String timeout) {

		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_EMAILADD, emailAdd);
		contentValues.put(KEY_TIMEIN, timein);
		contentValues.put(KEY_TIMEOUT, timeout);

		return contentValues;
	}

	// Delete a row from the database, by email (primary key)
	public boolean deleteRow(String emailAdd) {
		String where = KEY_EMAILADD + "=" + emailAdd;
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

	// Get a specific row (by email)
	public Cursor getRow(String emailAdd) {

		String where = KEY_EMAILADD + "=" + emailAdd;

		Cursor cursor = sql_db.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	private static class DB_User_Time_Log_Helper extends SQLiteOpenHelper {

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;

		DB_User_Time_Log_Helper(Context context) {
			super(context, TABLE_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sql_db) {
			sql_db.execSQL(DATABASE_CREATE_SQL);
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
