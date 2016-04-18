package com.example.najib.speed;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class Speed extends Activity implements SensorEventListener{

    TextView myTextvitesse;
    TextView myTextacceleration;
    private SensorManager sensorManager;
    double ax,ay,az;   // these are the acceleration in x,y and z axis
    double initialSpeed =0.0;
    double speed =0.0;
    Date timeStart;
    double currentA = 0.0;
    double lastA = 0.0;
    boolean toggle = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        myTextvitesse= (TextView) findViewById(R.id.vitesse);
        myTextacceleration= (TextView) findViewById(R.id.acceleration);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        timeStart = new Date(System.currentTimeMillis());

        initialSpeed =0.0;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double vitesse_x, vitesse_y,vitesse_z;
        Date timeNow = new Date(System.currentTimeMillis());

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            double time= (timeNow.getTime() - timeStart.getTime())/1000.0;
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];
            myTextvitesse.setText(" x= "+ax+" y= "+ay+" z= "+az);
//            if(Math.abs(ax)>= 9){ // enlever la garvite = 9.81
//                ax = (Math.abs(ax)- 9.81);
//            }
//            if(Math.abs(ay)>= 9){ // enlever la garvite = 9.81
//                ay = (Math.abs(ay) - 9.81);
//            }
//            if(Math.abs(az)>= 9){ // enlever la garvite = 9.81
//                az = (Math.abs(az) - 9.81);
//            }

            double a = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2)
                    + Math.pow(az, 2));
            if(a > 9){ // enlever la garvite = 9.81
                a = a- 9;
            }
            else if (a>0){
                a =0;
            }
//            a = a - 9.81;
            if(toggle){
                currentA = a;
                toggle = !toggle;
            } else {
                lastA = a;
                toggle = !toggle;
            }

//            myTextvitesse.setText(Double.toString(Math.floor(currentA))+":V"); //
//            myTextacceleration.setText(Double.toString(Math.floor(lastA))+":A"); //
            double acceleration= 0.0;
            if(currentA>lastA){
                acceleration = a;

            } else {
                acceleration = -a;
            }
            speed = initialSpeed + acceleration * time; // v= v0 + a*t (km par seconde)

            if(speed<0) {
                speed =0;
                initialSpeed =0;
            }

            myTextvitesse.setText(Double.toString(Math.floor(speed*3.6))+"Km/h"); //
            myTextacceleration.setText(Double.toString(Math.floor(a))+":m/s2"); //
            initialSpeed = speed;
            timeStart.setTime(System.currentTimeMillis());
        }


    }
}
