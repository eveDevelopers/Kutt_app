package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Account extends AppCompatActivity {
    Button sign_in;
    Switch notification;
    TextView name;
    ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_account);
        sign_in = findViewById(R.id.sign_in);
        notification = findViewById(R.id.notification);
        close = findViewById(R.id.close);
        name = findViewById(R.id.name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat_Light.ttf");
        name.setTypeface(custom_font1);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account.this,Login.class);
                startActivity(i);
            }
        });
        SharedPreferences not = getSharedPreferences("notif",MODE_PRIVATE);
        final SharedPreferences.Editor editor = not.edit();
        if(isMyServiceRunning(TheService.class) || isMyServiceRunning(SensorService.class) || not.getInt("enable",1)==1) {
            notification.setChecked(true);
        }else {
            notification.setChecked(false);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   editor.putInt("enable",1);
                   editor.apply();
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       getApplicationContext().startForegroundService(new Intent(getApplicationContext(), TheService.class));
                   }else {
                       getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
                   }
               }else {
                   editor.putInt("enable",0);
                   editor.apply();
                   //if(isMyServiceRunning(SensorService.class)) {
                       getApplicationContext().stopService(new Intent(getApplicationContext(), SensorService.class));
                   //}

                   getApplicationContext().stopService(new Intent(getApplicationContext(), TheService.class));
                   Toast.makeText(Account.this,"Service Stopped!",Toast.LENGTH_SHORT).show();
               }
           }
       });

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
