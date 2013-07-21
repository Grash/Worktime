package  com.geeko.worktimeapp.service;

import java.util.ArrayList;
import java.util.List;

import com.geeko.worktimeapp.domain.WorkingDay;
import com.geeko.worktimeapp.domain.WorkingStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "worktime";

	// Contacts table name
	private static final String TABLE_WORKDAYS = "workdays";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_START_WORK = "start_work";
	private static final String KEY_STOP_WORK = "stop_work";
	private static final String KEY_START_BREAK = "start_break";
	private static final String KEY_ALL_BREAK = "all_break";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("DB", "Create");
		String CREATE_WORKDAY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WORKDAYS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_START_WORK + " TEXT,"
				 + KEY_STOP_WORK + " TEXT,"  + KEY_START_BREAK + " TEXT,"
				+ KEY_ALL_BREAK + " INTEGER" + ")";
		db.execSQL(CREATE_WORKDAY_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DB", "Upgrade");
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKDAYS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	
	public void createTable(){
		Log.i("DB", "Create");
		SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_WORKDAY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WORKDAYS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_START_WORK + " TEXT,"
				 + KEY_STOP_WORK + " TEXT,"  + KEY_START_BREAK + " TEXT,"
				+ KEY_ALL_BREAK + " INTEGER" + ")";
		db.execSQL(CREATE_WORKDAY_TABLE);
	}

	// Adding new work day
	void addWorkingDay(WorkingDay day) {
		Log.i("DB", "Add woring day");
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_START_WORK,day.getTimeStampString(day.getStartWorkingTimeStamp()));
		values.put(KEY_STOP_WORK, day.getTimeStampString(day.getStopWorkingTimeStamp()));
		values.put(KEY_START_BREAK, day.getTimeStampString(day.getStartBreak()));
		values.put(KEY_ALL_BREAK, day.getAllBreak());

		// Inserting Row
		db.insert(TABLE_WORKDAYS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single working day
	WorkingDay getWorkingDay(int id) {
		Log.i("DB", "Get working day " + id);
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_WORKDAYS, new String[] { KEY_ID,
				KEY_START_WORK, KEY_STOP_WORK,  KEY_START_BREAK, KEY_ALL_BREAK}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		WorkingDay workingDay = new WorkingDay(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)) );
		workingDay.logData("DB", WorkingStatus.OUT_OF_OFFICE);
		// return contact
		return workingDay;
	}
	
	void dropTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKDAYS);
	}
	
	// Getting All WorkingDay
	public List<WorkingDay> getAllWorkingDay() {
		List<WorkingDay> workingdayList = new ArrayList<WorkingDay>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WORKDAYS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				WorkingDay workingDay = new WorkingDay(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)) );
				workingDay.logData("DB", WorkingStatus.OUT_OF_OFFICE);
				// Adding contact to list
				workingdayList.add(workingDay);
			} while (cursor.moveToNext());
		}

		// return contact list
		return workingdayList;
	}
/*
	// Updating single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	*/
}
