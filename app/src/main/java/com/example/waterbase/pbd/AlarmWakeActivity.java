package com.example.waterbase.pbd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmWakeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private final float SHAKE_THRESHOLD = 500;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_wake);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TextView timeView = findViewById(R.id.wake_time);
        timeView.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        last_x = last_y = last_z = 0;
        playRingtone();
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void playRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

//    public void playRingtone() {
//        Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);; // initialize Uri here
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.setDataSource(getApplicationContext(), myUri);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (Exception e) {
//
//        }
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Toast.makeText(getApplicationContext(), Float.toString(event.values[0]), Toast.LENGTH_SHORT).show();
        long curTime = System.currentTimeMillis();
        // only allow one update every 100ms.
        if ((curTime - lastUpdate) > 500) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                this.finish();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
