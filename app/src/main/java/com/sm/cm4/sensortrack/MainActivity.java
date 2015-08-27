package com.sm.cm4.sensortrack;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    //accelerometer
    private Sensor mAccelerometer;
    private float[] gravity = {0.0f, 0.0f, 0.0f};
    private float[] linear_acceleration = {0.0f, 0.0f, 0.0f};
    private float[] last_linear_acceleration = {0.0f, 0.0f, 0.0f};
    private float[] delta = {0.0f, 0.0f, 0.0f};
    private float[] curDelta = {0.0f, 0.0f, 0.0f};
    private TextView textX, textY, textZ;
    //orientation
    OrientationEventListener mOrientationEventListener;
    private TextView textAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textX = (TextView) findViewById(R.id.x_axis);
        this.textY = (TextView) findViewById(R.id.y_axis);
        this.textZ = (TextView) findViewById(R.id.z_axis);
        this.textAngle = (TextView) this.findViewById(R.id.angle);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //**** acelerometro *******

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.textX.setText("0.0");
        this.textY.setText("0.0");
        this.textZ.setText("0.0");

        //**** orientation *******

        this.textAngle.setText("NULL");

        mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int arg0) {
                textAngle.setText("Orientation: " + String.valueOf(arg0));
            }
        };

        if (mOrientationEventListener.canDetectOrientation()) {
            Toast.makeText(this, "Can DetectOrientation", Toast.LENGTH_LONG).show();
            mOrientationEventListener.enable();
        } else {
            Toast.makeText(this, "Can't DetectOrientation", Toast.LENGTH_LONG).show();
        }

        //**** Detecta sensores do dispositivo *******
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            Log.d("Sensors", "" + sensor.getName());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mOrientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mOrientationEventListener.disable();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    public void onSensorChanged(SensorEvent event) {


        //************* Google Code *********************

        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.


        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        //***************************************************

        delta[0] = Math.abs(last_linear_acceleration[0] - linear_acceleration[0]);
        delta[1] = Math.abs(last_linear_acceleration[1] - linear_acceleration[1]);
        delta[2] = Math.abs(last_linear_acceleration[2] - linear_acceleration[2]);


        if (delta[0] > curDelta[0]) {
            curDelta[0] = delta[0];
            this.textX.setText(Float.toString(curDelta[0]));
        }

        if (delta[1] > curDelta[1]) {
            curDelta[1] = delta[1];
            this.textY.setText(Float.toString(curDelta[1]));
        }

        if (delta[2] > curDelta[2]) {
            curDelta[2] = delta[2];
            this.textZ.setText(Float.toString(curDelta[2]));
        }

        this.textX.setText(Float.toString(curDelta[0]));
        this.textY.setText(Float.toString(curDelta[1]));
        this.textZ.setText(Float.toString(curDelta[2]));

        last_linear_acceleration[0] = linear_acceleration[0];
        last_linear_acceleration[1] = linear_acceleration[1];
        last_linear_acceleration[2] = linear_acceleration[2];

    }


    public void onClickResetAccelerometer(View v) {
        curDelta[0] = 0.0f;
        curDelta[1] = 0.0f;
        curDelta[2] = 0.0f;

        this.textX.setText(Float.toString(curDelta[0]));
        this.textY.setText(Float.toString(curDelta[1]));
        this.textZ.setText(Float.toString(curDelta[2]));
    }

    public void onClickResetRotation(View v) {
        this.textAngle.setText("NULL");

    }

    /*
        //******* RESET TIME em 5 segundos ************

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 10000) {
            lastUpdate = curTime;

            curDelta[0] = 0.0f;
            curDelta[1] = 0.0f;
            curDelta[2] = 0.0f;

            this.textX.setText(Float.toString(curDelta[0]));
            this.textY.setText(Float.toString(curDelta[1]));
            this.textZ.setText(Float.toString(curDelta[2]));

        }

        //*******************************

    */

    /*
         if (delta[0] > curDelta[0]){
            curDelta[0] = delta[0];
            curDelta[1] = delta[1];
            curDelta[2] = delta[2];

            this.textX.setText(Float.toString(curDelta[0]));
            this.textY.setText(Float.toString(curDelta[1]));
            this.textZ.setText(Float.toString(curDelta[2]));
        }
     */

}
