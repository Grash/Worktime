package com.geeko.worktimeapp.repository;

import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DefaultDataBaseRepository implements DataBaseRepository {
	
	private SQLiteDatabase workDB;
	private static final String DATABASE_NAME = "worktime.db";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	
	public DefaultDataBaseRepository(Context context){
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see com.geeko.worktimeapp.repository.DataBaseRepository#createTable(java.lang.String, java.lang.String)
	 */
	@Override
	public void createTable(String tableName, String tableSetup){
		try{
			String query = "CREATE TABLE if not exists "+tableName+" ("+tableSetup+")";
			workDB.execSQL(query);
			
		}catch (Exception e){
			Log.e("SYSTEM", e.getStackTrace().toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.geeko.worktimeapp.repository.DataBaseRepository#openDatabase()
	 */
	@Override
	public void openDatabase(){		
		Log.i("SYSTEM", "opening database");
		workDB = context.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
		workDB.setVersion(1);
		workDB.setLocale(Locale.getDefault());
		workDB.setLockingEnabled(true);
		Log.i("SYSTEM","database is open");
	}
	
	/* (non-Javadoc)
	 * @see com.geeko.worktimeapp.repository.DataBaseRepository#insertToTable(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void insertToTable(String table, String column, String data){
		workDB.execSQL("INSERT INTO "+table+" ("+column+") VALUES(\""+data+"\")");
	}
	
	@Override
	public void closeDatabase(){
		workDB.close();
	}
	

}
