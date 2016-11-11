package com.beiing.customview.widget2;

import android.support.v7.widget.RecyclerView;

/**
 * Created by chenliu on 2016/11/11.<br/>
 * 描述：
 * </br>
 */
public class FoldLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }
}
