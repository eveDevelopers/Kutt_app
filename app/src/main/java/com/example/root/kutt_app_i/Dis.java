package com.example.root.kutt_app_i;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Dis extends AppCompatActivity{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    int pos;
    int value =-1;
    PageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis);
        Intent i = getIntent();
        pos = i.getIntExtra("link",0);
        SharedPreferences p = getSharedPreferences("pos",MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt("pos",pos);
        ed.commit();
       // PlaceholderFragment.getData(pos);
       // PlaceholderFragment.getAdapter(viewPageAdapter);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        //viewPager = findViewById(R.id.vertical_viewPager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(1);


        //Selected(pos);
        }


    @Override
    public  void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        //Dis dis;
         int position;
        private int[] data;
        ViewPager viewPager;
        private List<ListenItem> listenItems;
        DatabaseHelper myDb;
        PageAdapter viewPageadapter;

       /* public  static void getData(int pos){
            position = pos;
        }
        public  static void getAdapter(PageAdapter pa){
            viewPageadapter = pa;
        }*/
       public PlaceholderFragment(){

       }





        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main5, container, false);
            final Context t=getContext();
            listenItems = new ArrayList<>();
            SharedPreferences p = t.getSharedPreferences("pos",MODE_PRIVATE);
            position = p.getInt("pos",0);
            viewPager = rootView.findViewById(R.id.vertical_viewPager);
            CardView left = rootView.findViewById(R.id.left);
            CardView right = rootView.findViewById(R.id.right);
            myDb = new DatabaseHelper(t);
            Cursor res = myDb.getAllData();
            while (res.moveToNext()) {
                ListenItem item = new ListenItem(res.getInt(0),res.getString(1),res.getInt(2),res.getString(3),res.getBlob(4));
                listenItems.add(item);
            }
           /* data = new int[listenItems.size()];
                // this.data1 = new String[1];
            for (int i = 0; i < listenItems.size(); i++) {
                data[i]=listenItems.get(i).getPos_in_db();
            }*/

                //viewPager.setAdapter(viewPageAdapter);
                // viewPager.setCurrentItem(pos);
            viewPageadapter = new PageAdapter(getChildFragmentManager(),listenItems);
            viewPager.setAdapter(viewPageadapter);
           viewPager.setCurrentItem(position);
           viewPager.setOffscreenPageLimit(1);




            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                viewPager.setVisibility(View.GONE);
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.GONE);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                viewPager.setVisibility(View.VISIBLE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
             //  Toast.makeText(t,position,Toast.LENGTH_SHORT).show();

            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                viewPager.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.VISIBLE);

            }

            return rootView;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}


