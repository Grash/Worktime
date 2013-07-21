package com.geeko.worktimeapp.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class WorkingDay {
	
	int id;
	Date startWorkingTimeStamp;
	Date stopWorkingTimeStamp;
	Date startBreak;
	int allBreak = 0;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	
	public WorkingDay(){
		super();
		Log.i("BREAK TIME", Integer.toString(allBreak));
	}
	
	public WorkingDay(int id, Date startWork, Date stopWork, Date startBreak, int allBreak ){
		this.id = id;
		this.startWorkingTimeStamp = startWork;
		this.stopWorkingTimeStamp = stopWork;
		this.startBreak = startBreak;
		this.allBreak = allBreak;
	}
	
	public WorkingDay(int id, String startWorkString, String stopWorkString, String startBreakString, int allBreak ){
		this.id = id;
		this.startWorkingTimeStamp = convertStringToDate(startWorkString);
		this.stopWorkingTimeStamp = convertStringToDate(stopWorkString);
		this.startBreak = convertStringToDate(startBreakString);
		this.allBreak = allBreak;
	}
	
	public WorkingDay(Date startWorkingTimeStamp) {
		super();
		this.startWorkingTimeStamp = startWorkingTimeStamp;
	}
	public Date getStartWorkingTimeStamp() {
		return startWorkingTimeStamp;
	}
	public void setStartWorkingTimeStamp(Date startWorkingTimeStamp) {
		//Log.i("info2", startWorkingTimeStamp.toString());
		if(startWorkingTimeStamp != null){
			this.startWorkingTimeStamp = startWorkingTimeStamp;
		}
		else{
			this.startWorkingTimeStamp = null;
		}
	}
	public Date getStopWorkingTimeStamp() {
		return stopWorkingTimeStamp;
	}
	public void setStopWorkingTimeStamp(Date stopWorkingTimeStamp) {
		this.stopWorkingTimeStamp = stopWorkingTimeStamp;
	}
	public Date getStartBreak() {
		return startBreak;
	}
	public void setStartBreak(Date startBreak) {
		this.startBreak = startBreak;
	}
	public int getAllBreak() {
		return allBreak;
	}
	public void setAllBreak(int allBreak) {
		this.allBreak = allBreak;
	}
	
	@SuppressWarnings("deprecation")
	public String getTimeStampString(Date date) {
		String dateString = "";
		if(date != null){
			dateString = date.getYear()+1900+"."+(date.getMonth()+1)+"."+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
		}
    	return dateString;
    	
	}
	
	public void logData(String message, WorkingStatus status){
		Log.i(message, "break: " + allBreak + ", status: " + status + ", start work: " + getTimeStampString(startWorkingTimeStamp) + ", stop work: " + getTimeStampString(stopWorkingTimeStamp) + ", start break: " + getTimeStampString(startBreak));
	}	
	
	private Date convertStringToDate( String original) {
		Date timeStamp = null;
		Log.i("DB", "Try to convert this: " + original);
		if(original != "" && original!= null && original.length() > 0){
			try {
				timeStamp = sdf.parse(original);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else{
			Log.i("info", "null");
			timeStamp = null;
		}
		return timeStamp;
		
	}

}
