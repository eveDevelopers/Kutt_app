package com.example.root.kutt_app_i;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setPageTransformer(true, new VerticalViewPager.PageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public class PageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {
                page.setVisibility(View.INVISIBLE);

            } else if (position <= 1) {
                page.setVisibility(View.VISIBLE);
                page.setTranslationX(page.getWidth() * -position);
                float y = position * page.getHeight();
                page.setTranslationY(y);

            } else {
                page.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean interceped = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return interceped;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }
}
