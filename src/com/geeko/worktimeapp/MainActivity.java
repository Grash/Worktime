package com.geeko.worktimeapp;

import java.util.Date;

import com.geeko.worktimeapp.domain.TimePickerFragment;
import com.geeko.worktimeapp.domain.TimeStamp;
import com.geeko.worktimeapp.domain.TimeStampType;
import com.geeko.worktimeapp.domain.WorkingStatus;
import com.geeko.worktimeapp.service.DatabaseHandler;
import com.geeko.worktimeapp.service.InMemoryWorkingTimeService;
import com.geeko.worktimeapp.service.WorkingTestTimes;
import com.geeko.worktimeapp.service.WorkingTimeService;
import com.geeko.worktimeapp.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {

	WorkingTimeService workingTimeService = new InMemoryWorkingTimeService();
	
	boolean isTimeFragmentActive = false;
	Boolean isTest = false;
	WorkingTestTimes testTimes = new WorkingTestTimes();
	TextView worktimeText;
	TimeStampType actualTimeStampType;
	DialogFragment newFragment;
	Button inOfficeButton, outOfficeButton, startBreakButton, stopBreakButton, clearDataButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initElements();
		workingTimeService.initService(this.getPreferences(Context.MODE_PRIVATE), getResources(), worktimeText, new DatabaseHandler(this));
		setButtonStateAccourdingTheWorkingStatus(WorkingStatus.OUT_OF_OFFICE);
		workingTimeService.initWorkingDay();
		Log.i("SYSTEM", "OnCreate");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.i("SYSTEM", "OnResume");
		if(!workingTimeService.isWorkingDayExists()){
			workingTimeService.initWorkingDay();
		}
		workingTimeService.loadPreferences();
		if(workingTimeService.getWorkingStatus() != WorkingStatus.OUT_OF_OFFICE){
			worktimeText.setText(workingTimeService.getActualWorkingTimeInWorkString(new Date()));
		}
		setButtonStateAccourdingTheWorkingStatus(workingTimeService.getWorkingStatus());
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		Log.i("SYSTEM", "OnPause");
		workingTimeService.savePreferences();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	    Log.i("SYSTEM", "onStop");		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	    Log.i("SYSTEM", "onStart");
	}

	private void initElements() {
		worktimeText = (TextView) findViewById(R.id.worktime_text);
		inOfficeButton = (Button) findViewById(R.id.button_in);
		outOfficeButton = (Button) findViewById(R.id.button_out);
		startBreakButton = (Button) findViewById(R.id.button_start_break);
		stopBreakButton = (Button) findViewById(R.id.button_stop_break);
		clearDataButton = (Button) findViewById(R.id.button_clear);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			workingTimeService.handleTimeStamp(selectedHour, selectedMinute, actualTimeStampType);
			actualTimeStampType = null;
			setButtonStateAccourdingTheWorkingStatus(workingTimeService
					.getWorkingStatus());
		}

	};

	public void clearData(View view){
		//workingTimeService.getDataFromDatabase(1);
		workingTimeService.getAllWorkingDay();
		Log.i("SYSTEM", "Data clear");
	}

	public void inWork(View view) {
		actualTimeStampType = TimeStampType.START_WORK;
		newFragment = new TimePickerFragment(timePickerListener);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void startBreak(View view) {
		actualTimeStampType = TimeStampType.START_BREAK;
		newFragment = new TimePickerFragment(timePickerListener);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void stopBreak(View view) {
		actualTimeStampType = TimeStampType.STOP_BREAK;
		newFragment = new TimePickerFragment(timePickerListener);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void outWork(View view) {
		actualTimeStampType = TimeStampType.STOP_WORK;
		newFragment = new TimePickerFragment(timePickerListener);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void setButtonStateAccourdingTheWorkingStatus(WorkingStatus status) {
		switch (status) {

		case OUT_OF_OFFICE:
			setButtonState(true, false, false, false);
			break;
		case WORK:
			setButtonState(false, true, false, true);
			break;
		case BREAK:
			setButtonState(false, false, true, true);
			break;
		}
	}

	public void setButtonState(Boolean startWorkButtonFlag, Boolean startBreakButtonFlag, Boolean stopBreakButtonFlag,
			Boolean stopWorkButtonFlag) {
		inOfficeButton.setEnabled(startWorkButtonFlag);		
		startBreakButton.setEnabled(startBreakButtonFlag);
		stopBreakButton.setEnabled(stopBreakButtonFlag);
		outOfficeButton.setEnabled(stopWorkButtonFlag);
	}

}
