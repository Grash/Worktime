package com.geeko.worktimeapp.domain;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class TimeStamp {

    private TimeStampType timeStampType;
    private Date date;

    /*public TimeStamp(){
        
    }*/

    public TimeStamp(Date date, TimeStampType timeStampType) {
        this.date = date;
        this.timeStampType = timeStampType;
    }
    
    public TimeStamp(TimeStampType timeStampType){
    	this.timeStampType = timeStampType;
    }

    public TimeStampType getTimeStampType() {
        return timeStampType;
    }

    public Date getDate() {
        return date;
    }
    
    @SuppressWarnings("deprecation")
	public String getDateString(){
    	String dateString = date.getYear()+1900+"."+(date.getMonth()+1)+"."+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    	Log.i("TIME STAMP", "dateString: " + dateString);
    	return dateString;
    }
}
