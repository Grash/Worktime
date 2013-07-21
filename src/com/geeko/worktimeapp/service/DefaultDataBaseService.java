package com.geeko.worktimeapp.service;

import android.content.Context;

import com.geeko.worktimeapp.repository.DataBaseRepository;
import com.geeko.worktimeapp.repository.DefaultDataBaseRepository;

public class DefaultDataBaseService {
	
	private DataBaseRepository dbRepository;
	
	public DefaultDataBaseService(Context ctx){
		dbRepository = new DefaultDataBaseRepository(ctx);
	}
	
	public void openDatabase(){
		dbRepository.openDatabase();
	}
	
	public void saveWorkingDay(String startWork, String stopWork, String startBreak, int allBreak, int status){
		String saveData = "\"" + getDataFromString(startWork)+"\"" + "\"" + getDataFromString(stopWork)+"\"";
	}
	
	private String getDataFromString(String data){
		return data == "" ? null : data;
	}

}
