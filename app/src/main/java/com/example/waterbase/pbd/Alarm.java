package com.example.waterbase.pbd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Set;

import static android.content.Context.ALARM_SERVICE;


public class Alarm extends Fragment implements LocationListener {
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    SharedPreferences alarmPreference;
    private TimePicker alarmTimePicker;
    final LinkedList<View> alarmEntry = new LinkedList<>();
    private Alarm inst;
    private TextView alarmTextView;

    LocationManager locationManager;
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
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        getLocation();
    }

    public void addNewAlarm(View v) {
        Calendar mCurrentTime = Calendar.getInstance();

        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                final int alarmId = alarmEntry.size();
                final View newAlarmEntry = getNewAlarmView(alarmId);
                appendNewAlarmView(newAlarmEntry);

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
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Toast.makeText(getActivity(), addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2), Toast.LENGTH_SHORT).show();
            TextView locationText = getView().findViewById(R.id.alarm_location);
            locationText.setText(addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
