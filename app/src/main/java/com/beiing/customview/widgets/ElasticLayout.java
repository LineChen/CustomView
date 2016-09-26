package com.beiing.customview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by chenliu on 2016/9/14.<br/>
 * 描述：弹性Layout
 * </br>
 */
public class ElasticLayout extends LinearLayout{

    private Scroller mScroller; //滑动控制器

    private int mMaxScrollY;//最大移动距离

    public ElasticLayout(Context context) {
        this(context, null, 0);
    }

    public ElasticLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxScrollY = getMeasuredHeight() * 3 / 5;
    }

    protected int mMoveY;
    protected int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int yPosition = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastY = yPosition;
                mMoveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (mLastY - yPosition);
                smoothScrollBy(0, mMoveY / 2);
                mLastY = yPosition;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回弹
                smoothScrollBy(0, -mScroller.getFinalY());
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
    }


    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        if(dy > 0)
            dy = Math.min(dy, mMaxScrollY);
        else
            dy = Math.max(dy, -mMaxScrollY);
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, Math.abs(dy));
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }


}





















