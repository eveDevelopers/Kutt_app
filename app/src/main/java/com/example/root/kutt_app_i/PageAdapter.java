package com.example.root.kutt_app_i;

import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PageAdapter extends FragmentPagerAdapter {
    private Context context;
    int[] data;
    public PageAdapter(FragmentManager fm, int[] data) {
        super(fm);
        this.data = data;
    }
    @Override
    public Fragment getItem(int position) {
        return PagerFragment.newInstance(data[position], position);
    }
    @Override
    public int getCount() {
        return data.length;
    }
}