package com.geeko.worktimeapp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import com.geeko.worktimeapp.R;
import com.geeko.worktimeapp.domain.TimeStampType;
import com.geeko.worktimeapp.domain.WorkingDay;
import com.geeko.worktimeapp.domain.WorkingStatus;

public class InMemoryWorkingTimeService implements WorkingTimeService {

	private WorkingDay actualWorkingDay;
    private SharedPreferences preferences;
    private Editor editor;
    private Resources resources;
    private TextView worktimeText;
    private DatabaseHandler dbHandler;
    private boolean testRun = false; 

    private WorkingStatus workingStatus = WorkingStatus.OUT_OF_OFFICE;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    
    public void initService(SharedPreferences pref, Resources res, TextView worktimeText, DatabaseHandler dbHandler){
    	this.preferences = pref;
    	this.editor = pref.edit();
    	this.resources = res;
    	this.worktimeText = worktimeText;
    	this.dbHandler = dbHandler;
    	dbHandler.createTable();
    	//preferences.edit().clear().commit();

    }

    
    public void initWorkingDay(){
    	actualWorkingDay = new WorkingDay();
    }

    /* (non-Javadoc)
     * @see com.acme.workinghours.service.WorkingTimeService#handleTimeStamp(com.acme.workinghours.domain.TimeStamp)
     */
    public void handleTimeStamp(int hour, int minute, TimeStampType timeStampType) {
    	Date timeStamp = getTimeStampFromHourAndMinute(hour, minute);
        switch (timeStampType) {

        case START_WORK:
        	Log.i("info1", timeStamp.toString());
        	
            actualWorkingDay.setStartWorkingTimeStamp(timeStamp);
            workingStatus = WorkingStatus.WORK;
            break;
        case START_BREAK:
        	actualWorkingDay.setStartBreak(timeStamp);
            workingStatus = WorkingStatus.BREAK;
            break;
        case STOP_BREAK:
        	actualWorkingDay.setAllBreak(getBreakTime(timeStamp)+actualWorkingDay.getAllBreak());
        	actualWorkingDay.setStartBreak(null);
            workingStatus = WorkingStatus.WORK;
            break;
        case STOP_WORK:
        	actualWorkingDay.setStopWorkingTimeStamp(timeStamp);
            workingStatus = WorkingStatus.OUT_OF_OFFICE;
            Log.i("WORK TIME",getLastShiftWorkingTimeString());
            worktimeText.setText("End work, working time: "
					+ getLastShiftWorkingTimeString());
            if(!testRun){
            	dbHandler.addWorkingDay(actualWorkingDay);
            	dbHandler.getWorkingDay(1);
            }
            initWorkingDay();
            break;
        }
    }

    private Date getTimeStampFromHourAndMinute(int hour, int minute) {
    	Date timeStamp = new Date();
    	timeStamp.setHours(hour);
    	timeStamp.setMinutes(minute);
    	timeStamp.setSeconds(0);
		return timeStamp;
	}

	/* (non-Javadoc)
     * @see com.acme.workinghours.service.WorkingTimeService#getActualWorkingTimeInWork(java.util.Date)
     */
    public long getActualWorkingTimeInWork(Date actualDate) {
        long workingTimeInMinutes = 0;
        workingTimeInMinutes = getShiftTimeInMinutes(actualDate) - actualWorkingDay.getAllBreak();
        return workingTimeInMinutes;
    }

    private long getShiftTimeInMinutes(Date actualDate) {
        if (actualWorkingDay.getStartWorkingTimeStamp() != null) {
            return (actualDate.getTime() - actualWorkingDay.getStartWorkingTimeStamp().getTime()) / 60000;
        } else {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see com.acme.workinghours.service.WorkingTimeService#getLastShiftWorkingTime()
    */
    public long getLastShiftWorkingTime() {
        long workingTimeInMinutes = 0;
        workingTimeInMinutes = getFullShiftTimeInMinutes() - actualWorkingDay.getAllBreak();
        return workingTimeInMinutes;
    }

    private long getFullShiftTimeInMinutes() {
        return (actualWorkingDay.getStopWorkingTimeStamp().getTime() - actualWorkingDay.getStartWorkingTimeStamp().getTime()) / 60000;
    }

	public WorkingStatus getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(WorkingStatus workingStatus) {
		this.workingStatus = workingStatus;
	}

	@Override
	public String getActualWorkingTimeInWorkString(Date actualTime) {
		long minutes = getActualWorkingTimeInWork(actualTime);
		return getWorkingTimeStringFromNumber(minutes);
	}
	
	public Boolean isWorkingDayExists(){
		return actualWorkingDay != null;
	}

	private String getWorkingTimeStringFromNumber(long minutes) {
		
		return makeTimeString(minutes/60)+":"+makeTimeString(minutes%60);
	}
	
	private String makeTimeString(long time){
		String timeString;
		if(time < 10){
			timeString = "0"+time;
		}
		else{
			timeString = "" + time;
		}
		return timeString;
	}

	@Override
	public String getLastShiftWorkingTimeString() {
		long minutes = getLastShiftWorkingTime();
		return getWorkingTimeStringFromNumber(minutes);
	}
	
	private int getBreakTime(Date stopBreak){
		return (int)((stopBreak.getTime() -actualWorkingDay.getStartBreak().getTime()) / 60000);
	}

	@Override
	public void savePreferences() {
		saveTimeStamp(actualWorkingDay.getTimeStampString(actualWorkingDay.getStartWorkingTimeStamp()), resources.getString(R.string.start_work));
		saveTimeStamp(actualWorkingDay.getTimeStampString(actualWorkingDay.getStopWorkingTimeStamp()), resources.getString(R.string.stop_work));
		saveTimeStamp(actualWorkingDay.getTimeStampString(actualWorkingDay.getStartBreak()), resources.getString(R.string.start_break));
		editor.putInt(resources.getString(R.string.full_break_time), actualWorkingDay.getAllBreak());
		editor.putInt(resources.getString(R.string.working_status), workingStatus.getStatus());
		actualWorkingDay.logData("Save Data", workingStatus);
		if(editor.commit()){
			Log.i("SAVE DATA","sikeres mentés");
		}else{
			Log.i("SAVE DATA","baszhatod");
		}
	}

	private void saveTimeStamp(String timeStampString, String id) {
		if(timeStampString != null){
			editor.putString(id, timeStampString);
		}		
	}

	@Override
	public void loadPreferences() {
		Log.i("LOAD_DATA", "LOAD STARTING");
		Date startWorkTimeStamp;
		Date stopWorkTimeStamp; 
		Date startBreakTimeStamp;
		int allBreak = 0;
		Log.i("LOAD_DATA", "Load Start Work Time Stamp");
		startWorkTimeStamp = loadTimeStamp(resources.getString(R.string.start_work));
		Log.i("LOAD_DATA", "Load Stop Work Time Stamp");
		stopWorkTimeStamp = loadTimeStamp(resources.getString(R.string.stop_work));
		Log.i("LOAD_DATA", "Load Start Break Time Stamp");
		startBreakTimeStamp = loadTimeStamp(resources.getString(R.string.start_break));
		setTimeStamps(startWorkTimeStamp, stopWorkTimeStamp, startBreakTimeStamp);
		allBreak = preferences.getInt(resources.getString(R.string.full_break_time), 0);
		actualWorkingDay.setAllBreak(allBreak);
		int status = preferences.getInt(resources.getString(R.string.working_status), 0);
		workingStatus = WorkingStatus.values()[status];
		Log.i("LOAD_DATA", "status: " + workingStatus.toString());
		actualWorkingDay.logData("LOAD_DATA", workingStatus);
		preferences.edit().clear().commit();
	}

	@Override
	public void clearSavedData() {
		this.preferences.edit().remove(resources.getString(R.string.start_work));
		this.preferences.edit().remove(resources.getString(R.string.stop_work));
		this.preferences.edit().remove(resources.getString(R.string.start_break));
		this.preferences.edit().remove(resources.getString(R.string.full_break_time));
		this.preferences.edit().remove(resources.getString(R.string.working_status));
		this.preferences.edit().commit();	
	}

	private void setTimeStamps(Date startWorkTimeStamp, Date stopWorkTimeStamp,
			Date startBreakTimeStamp) {
		//if(startWorkTimeStamp != null){
			actualWorkingDay.setStartWorkingTimeStamp(startWorkTimeStamp);
		//}
		//if(stopWorkTimeStamp != null){
			actualWorkingDay.setStopWorkingTimeStamp(stopWorkTimeStamp);
		//}
		//if(startBreakTimeStamp != null){
			actualWorkingDay.setStartBreak(startBreakTimeStamp);
		//}		
	}

	private Date loadTimeStamp( String resString) {
		Date timeStamp = null;
		String dateString = preferences.getString(resString, null);
		if(dateString != null && dateString != ""){
			Log.i("LOAD_TIMESTAMP", dateString);
			try {
				timeStamp = sdf.parse(dateString);
				Log.i("LOAD_TIMESTAMP","Date: " + timeStamp);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else{
			Log.i("LOAD_TIMESTAMP", "null");
			timeStamp = null;
		}
		return timeStamp;
		
	}


	@Override
	public void getDataFromDatabase(int id) {
		dbHandler.getWorkingDay(id);
		
	}


	@Override
	public List<WorkingDay> getAllWorkingDay() {
		return  dbHandler.getAllWorkingDay();
	}
    
}
