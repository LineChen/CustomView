package com.beiing.customview.widget2;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 * InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        // offset first element so that we can scroll to the left
        setCurrentItem(0);
    }

    public void setAdapter(PagerAdapter adapter, int lastPosition) {
        super.setAdapter(adapter);
        super.setCurrentItem(lastPosition, false);
    }

    @Override
    public void setCurrentItem(int item) {
        // offset the current item to ensure there is space to scroll
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (getAdapter().getCount() == 0) {
            super.setCurrentItem(item, smoothScroll);
            return;
        }
        item = getOffsetAmount() + (item % getAdapter().getCount());
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        if (getAdapter().getCount() == 0) {
            return super.getCurrentItem();
        }
        int position = super.getCurrentItem();
        if (getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
            // Return the actual item position in the data backing InfinitePagerAdapter
            return (position % infAdapter.getRealCount());
        } else {
            return super.getCurrentItem();
        }
    }

    public int getOffsetAmount() {
        if (getAdapter().getCount() == 0) {
            return 0;
        }
        if (getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
            // allow for 100 back cycles from the beginning
            // should be enough to create an illusion of infinity
            // warning: scrolling to very high values (1,000,000+) results in
            // strange drawing behaviour
            return infAdapter.getRealCount() * 100;
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

//    @Override
//    public void scrollTo(int x, int y) {
//        if(isCanScroll){
//            super.scrollTo(x, y);
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanScroll) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }

    }


}