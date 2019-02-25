package com.example.waterbase.pbd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static android.content.Context.ALARM_SERVICE;


public class Alarm extends Fragment {
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    SharedPreferences alarmPreference;
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

        alarmPreference = getContext().getSharedPreferences("alarmPref", Context.MODE_PRIVATE);
        Set<String> alarmIdSet = alarmPreference.getStringSet("alarmList", new HashSet<String>());

        if(!alarmIdSet.isEmpty()){
            for(String id : alarmIdSet) {
                appendNewAlarmView(getOldAlarmView((Integer.parseInt(id))));
            }
        }
    }

    public void addNewAlarm(View v) {
        Calendar mCurrentTime = Calendar.getInstance();

        final int alarmId = alarmEntry.size();
        final View newAlarmEntry = getNewAlarmView(alarmId);
        appendNewAlarmView(newAlarmEntry);

        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                TextView timeView = newAlarmEntry.findViewById(R.id.text_time);
                timeView.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
                addNewAlarmService(hour, minute, alarmId);
            }
        }, hour, minute, true);
        mTimePicker.show();
    }

    public View getNewAlarmView(int id) {
        LayoutInflater inflater =  LayoutInflater.from(getContext());
        final View newAlarmEntry = inflater.inflate(R.layout.alarm_card, null, false);
        newAlarmEntry.setId(id);
        alarmEntry.add(newAlarmEntry);

        return newAlarmEntry;
    }

    public View getOldAlarmView(int id) {
        View alarmView = getNewAlarmView(id);
        TextView alarmTime = alarmView.findViewById(R.id.text_time);
        String alarmString = alarmPreference.getString(Integer.toString(id), null);
        alarmTime.setText(alarmString);

        return alarmView;
    }

    public void appendNewAlarmView(View newAlarmEntry) {
        LinearLayout alarmView = getView().findViewById(R.id.alarm_main_layout);
        alarmView.addView(newAlarmEntry);
    }

    public void addNewAlarmService(int hour, int minute, int id) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();

        SharedPreferences.Editor alarmEditor = alarmPreference.edit();
        Set<String> alarmIdSet = new HashSet<>();
        Set<String> alarmStringSet = new HashSet<>();

        for(View alarmView : alarmEntry) {
            alarmIdSet.add(Integer.toString(alarmView.getId()));
            TextView alarmTime = alarmView.findViewById(R.id.text_time);
            alarmEditor.putString(Integer.toString(alarmView.getId()), alarmTime.getText().toString());
        }

        alarmEditor.putStringSet("alarmList", alarmIdSet);
        alarmEditor.commit();
    }
}
