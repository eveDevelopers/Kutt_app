package com.example.root.kutt_app_i;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
