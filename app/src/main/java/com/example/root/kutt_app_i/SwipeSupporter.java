package com.example.root.kutt_app_i;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeSupporter extends ItemTouchHelper.SimpleCallback{


    MyAdapter adapter;

    public SwipeSupporter(int dragDirs, int swipeDirs){
        super(dragDirs,swipeDirs);
    }

    public SwipeSupporter(MyAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        //adapter.diss(viewHolder.getAdapterPosition());

    }
}