package com.example.root.kutt_app_i;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat_Light.ttf");
        toolbar_title.setTypeface(custom_font1);
    }
}
