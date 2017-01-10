package com.beiing.customview.widget2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenliu on 2016/11/11.<br/>
 * 描述：
 * </br>
 */
public class WrapLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        int widthSize = View.MeasureSpec.getSize(widthSpec);
        int heightSize = View.MeasureSpec.getSize(heightSpec);

        int height = measureScrapChildren(recycler, state.getItemCount(),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        if (height == 0 || height >= heightSize) {
            height = heightSize;
        }
        setMeasuredDimension(widthSize, height);
    }

    private int measureScrapChildren(RecyclerView.Recycler recycler, int size,
                                     int widthSpec, int heightSpec) {
        int height = 0;
        for (int i = 0; i < size; i++) {
            View view = recycler.getViewForPosition(i);
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            recycler.recycleView(view);
            height += view.getMeasuredHeight();
        }
        return height;
    }
}
