package com.example.root.kutt_app_i;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by atul on 11/7/18.
 */

public class StartMyServiceAtBootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())||"android.intent.action.SCREEN_OFF".equals(intent.getAction())||"android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            Log.e("a","a");

            Intent serviceIntent = new Intent(context,SensorService.class);

            context.startService(serviceIntent);
        }
    }

}
