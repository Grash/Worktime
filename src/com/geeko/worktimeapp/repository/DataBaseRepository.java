package com.geeko.worktimeapp.repository;

public interface DataBaseRepository {

	public abstract void createTable(String tableName, String tableSetup);

	public abstract void openDatabase();

	public abstract void insertToTable(String table, String column, String data);
	
	public void closeDatabase();

}