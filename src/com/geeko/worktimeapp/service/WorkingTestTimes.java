package com.geeko.worktimeapp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkingTestTimes {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	
	public Date getStartWorkTime(){
		String workTimeString = "2013.1.4 8:8";
		return getDateFromString(workTimeString);
	}
	
	public Date getStopWorkTime(){
		String workTimeString = "2013.1.4 18:8";
		return getDateFromString(workTimeString);
	}
	
	public Date getStartBreakTime(){	
		String workTimeString = "2013.01.04 12:00";
		return getDateFromString(workTimeString);
	}
	
	public Date getStopBreakTime(){	
		String workTimeString = "2013.01.04 12:30";
		return getDateFromString(workTimeString);
	}
	
	private Date getDateFromString(String workTimeString){
		Date workTime = null;
		try{
        workTime = sdf.parse(workTimeString);
        
		} catch( ParseException e){
			e.printStackTrace();
		}
		return workTime;
	}
}
