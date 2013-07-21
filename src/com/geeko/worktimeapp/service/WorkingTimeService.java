package com.geeko.worktimeapp.service;

import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.widget.TextView;

import com.geeko.worktimeapp.domain.TimeStamp;
import com.geeko.worktimeapp.domain.TimeStampType;
import com.geeko.worktimeapp.domain.WorkingDay;
import com.geeko.worktimeapp.domain.WorkingStatus;

public interface WorkingTimeService {
	
	public void initService(SharedPreferences pref, Resources res, TextView testText, DatabaseHandler dbHandler);
	
	public abstract void initWorkingDay();

    public abstract void handleTimeStamp(int hour, int minute, TimeStampType timeStampType);

    public abstract long getActualWorkingTimeInWork(Date actualDate);

    public abstract long getLastShiftWorkingTime();
    
    public WorkingStatus getWorkingStatus();
    
    public void setWorkingStatus(WorkingStatus workingStatus);

	public abstract String getActualWorkingTimeInWorkString(Date startBreakTime);

	public abstract String getLastShiftWorkingTimeString();

	public abstract void savePreferences();

	public abstract void loadPreferences();
	
	public abstract Boolean isWorkingDayExists();

	public void clearSavedData();
	
	public void getDataFromDatabase(int id);
	
	public List<WorkingDay> getAllWorkingDay();

}