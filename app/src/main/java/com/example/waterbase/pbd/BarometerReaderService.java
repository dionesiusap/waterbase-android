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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class BarometerReaderService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "BaroLoggerService";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private static float sensorValue = (float) 0.0;

    private int currentFloor = 0;
    private int previousFloor = 0;

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
            Log.d("BaroLoggerService", "Baro = " + Float.toString(value));
            BarometerReaderService.setSensorValue(value);

            previousFloor = currentFloor;
            currentFloor = getFloorLevel();

            if (isFloorChanged()) {
                onFloorChanged();
            }

            return (null);
        }
    }

    public int getFloorLevel() {
        return (1 + (int)((FragmentActivity.getGroundFloorBaseline() - getSensorValue()) / 0.25));
    }

    public boolean isFloorChanged() {
        return (currentFloor != previousFloor);
    }

    public void onFloorChanged() {
        String url = FragmentActivity.getServerUrl() + "/floor/" + Integer.toString(currentFloor);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    //Success callback
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Failure Callback
                }
            }
        );
        HttpRequestApplication.getInstance().addToRequestQueue(jsonObjReq, "OnOffRequest");
    }
}
