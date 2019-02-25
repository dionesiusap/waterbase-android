package com.example.waterbase.pbd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.LinkedList;

import static android.content.Context.ALARM_SERVICE;


public class Alarm extends Fragment {
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    final LinkedList<View> alarmEntry = new LinkedList<>();
    private Alarm inst;
    private TextView alarmTextView;

    public Alarm() {
        // Required empty public constructor
    }
    public Alarm instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton add_alarm = view.findViewById(R.id.add_alarm);
        this.alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAlarm(v);
            }
        });
    }

    public void addNewAlarm(View v) {
        Calendar mCurrentTime = Calendar.getInstance();

        LayoutInflater inflater =  LayoutInflater.from(getActivity());
        final View newAlarmEntry = inflater.inflate(R.layout.alarm_card, null, false);
        newAlarmEntry.setId(alarmEntry.size());
        alarmEntry.add(newAlarmEntry);
        ScrollView mainView = (ScrollView) getView();
        mainView.addView(newAlarmEntry);

        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                TextView timeView = newAlarmEntry.findViewById(R.id.time);
                timeView.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
                addNewAlarmService(hour, minute);
            }
        }, hour, minute, true);
    }

    public void addNewAlarmService(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}
