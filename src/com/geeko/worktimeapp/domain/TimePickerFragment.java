package com.geeko.worktimeapp.domain;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimePickerFragment extends DialogFragment {

	OnTimeSetListener onTimeSetListener;

	public TimePickerFragment(OnTimeSetListener onTimeSetListener) {
		this.onTimeSetListener = onTimeSetListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), onTimeSetListener, hour,
				minute, DateFormat.is24HourFormat(getActivity()));
	}

}
