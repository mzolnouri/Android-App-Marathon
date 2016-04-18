package com.example.najib.vitesse;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    TextView myTextView;
    float appliedAcceleration = 0;
    float currentAcceleration = 0;
    float velocity = 0;
    Date lastUpdate;
    Handler handler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myTextView = (TextView) findViewById(R.id.myTextView);
        lastUpdate = new Date(System.currentTimeMillis());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_FASTEST);
        Timer updateTimer = new Timer("velocityUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
    }

    private void updateGUI() {
        // Convert from m/s to mph
        final double mph = (Math.round(100 * velocity / 1.6 * 3.6)) / 100;
        // Update the GUI
        handler.post(new Runnable() {
            public void run() {
                myTextView.setText(String.valueOf(mph) + "mph");
            }
        });
    }

    private void updateVelocity() {
        // Calculate how long this acceleration has been applied.
        Date timeNow = new Date(System.currentTimeMillis());
        long timeDelta = timeNow.getTime() - lastUpdate.getTime();
        lastUpdate.setTime(timeNow.getTime());
        // Calculate the change in velocity at the
        // current acceleration since the last update.
        float deltaVelocity = appliedAcceleration * (timeDelta / 1000);
        appliedAcceleration = currentAcceleration;
        // Add the velocity change to the current velocity.
        velocity += deltaVelocity;
        // Convert from meters per second to miles per hour.
        double mph = (Math.round(velocity / 1.6 * 3.6));
        myTextView.setText(String.valueOf(mph) + "mph");
    }

    private final SensorListener sensorListener = new SensorListener() {
        double calibration;
        public void onSensorChanged(int sensor, float[] values) {
            double x = values[SensorManager.DATA_X];
            double y = values[SensorManager.DATA_Y];
            double z = values[SensorManager.DATA_Z];
            double a = -1
                    * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
                    + Math.pow(z, 2));
            if (calibration == Double.NaN)
                calibration = a;
            else {
                updateVelocity();
                currentAcceleration = (float) a;
            }
        }

        public void onAccuracyChanged(int sensor, int accuracy) {
        }

};
}
