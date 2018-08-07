package com.example.root.kutt_app_i;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Dis extends AppCompatActivity{

    private ViewPager mViewPager;
    List<ListenItem> listenItems;



    String link;
    int value =-1;
    PageAdapter viewPageAdapter;
    ViewPager viewPager;
    TextView rLink;
    DatabaseHelper myDb;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis);
        Intent i = getIntent();
        link = i.getStringExtra("link");
        listenItems = new ArrayList<>();
        /*SharedPreferences p = getSharedPreferences("pos",MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString("link",link);
        ed.commit();*/
       // PlaceholderFragment.getData(pos);
       // PlaceholderFragment.getAdapter(viewPageAdapter);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        viewPager = findViewById(R.id.vertical_viewPager);
        myDb = new DatabaseHelper(Dis.this);
        Cursor res = myDb.getAllData();
        position = 0;
        int index = 0;
        while (res.moveToNext()) {
            ListenItem item = new ListenItem(res.getInt(0),res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
            listenItems.add(item);
            if(item.getLink().equals(link)){
                position = index;
            }
            index++;
        }
        viewPageAdapter = new PageAdapter(getSupportFragmentManager(),listenItems);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(1);

        }


    @Override
    public  void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);


    }



}


