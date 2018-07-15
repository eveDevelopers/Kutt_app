package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by atul on 11/7/18.
 */

public class SensorService extends Service implements SensorEventListener {
    BroadcastReceiver mReceiver;
    SharedPreferences not;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        not = getSharedPreferences("notif",MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


            }
        };

        Log.d("Registered", "onstart service");
        SensorManager sManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL); // or other delay
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
             if(!isMyServiceRunning(TheService.class) && not.getInt("enable",1) == 1){
                 Log.e("Main service","not running");
                 Intent intent=new Intent(this,TheService.class);
                 startService(intent);
             }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
