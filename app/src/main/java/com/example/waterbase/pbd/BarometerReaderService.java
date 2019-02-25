package com.example.waterbase.pbd;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class BarometerReaderService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "BaroLoggerService";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private static float sensorValue = (float) 0.0;

    public static void setSensorValue(float value) {
        sensorValue = value;
    }

    public static float getSensorValue() {
        return sensorValue;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // mandatory method that must be implemented for this service
        // in our case do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // grab the values and timestamp
        new SensorEventLoggerTask().execute(event);
        // stop the sensor and service
        stopSelf();
    }

    private class SensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            float value = event.values[0];
            // TODO: send request to turn lights on or off when certain value is reached
            Log.d("BaroLoggerService", "Baro = " + Float.toString(value));
            BarometerReaderService.setSensorValue(value);

            return (null);
        }
    }
}
