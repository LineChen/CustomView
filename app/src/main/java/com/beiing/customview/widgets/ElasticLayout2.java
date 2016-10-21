package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by chenliu on 2016/9/30.<br/>
 * 描述：弹性Layout
 * </br>
 */
public class ElasticLayout2 extends LinearLayout{

    private Scroller mScroller; //滑动控制器

    private int mMaxScrollY;//最大移动距离

    /**
     * scrollView是否滚动到底部
     */
    private boolean isToBotttom;

    /**
     * scrollView是否滚动到顶部
     */
    private boolean isToTop = true;

    private ScrollEndScrollView scrollView;

    public ElasticLayout2(Context context) {
        this(context, null, 0);
    }

    public ElasticLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxScrollY = getMeasuredHeight() * 3 / 5;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View child = getChildAt(0);
        if (child != null) {
            if(child instanceof ScrollEndScrollView){
                scrollView = (ScrollEndScrollView) child;
                scrollView.setVerticalFadingEdgeEnabled(false);
                scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
            } else {
                throw new ClassFormatError("The child must instanceof ScrollEndScrollView.");
            }
        }

        if (scrollView != null) {
            scrollView.addOnScrollEndListener(new ScrollEndScrollView.OnScrollEndListener() {
                @Override
                public void scrollToBottom(View view) {
                    isToBotttom = true;
                }

                @Override
                public void scrollToTop(View view) {
                    isToTop = true;
                }

                @Override
                public void scrollToMiddle(View view) {
                    isToBotttom = false;
                    isToTop = false;
                }
            });
        }
    }

    protected int mMoveY;
    protected int mLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int yPosition = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastY = yPosition;
                mMoveY = 0;
               break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (mLastY - yPosition);
                mLastY = yPosition;
                if(isToBotttom){
                    if(mMoveY > 0){
                        //向上
                        smoothScrollBy(0, mMoveY / 2);
                        return true;
                    } else {
                        //向下
                        if(mScroller.getFinalY() != 0){
                            if(getScrollY() + mMoveY / 2 > 0){
                                smoothScrollBy(0, mMoveY / 2);
                                return true;
                            } else{
                                smoothScrollTo(0, 0);
                            }
                        }
                    }
                }
                else if(isToTop){
                    if(mMoveY < 0){
                        //向下
                        smoothScrollBy(0, mMoveY / 2);
                        return true;
                    } else {
                        //向上
                        if(mScroller.getFinalY() < 0){
                            smoothScrollBy(0, mMoveY / 2);
                            return true;
                        }else {
                            smoothScrollTo(0, 0);
                        }
                    }
                }
                smoothScrollTo(0, 0);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int yPosition = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastY = yPosition;
                mMoveY = 0;
               return true;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (mLastY - yPosition);
                mLastY = yPosition;
                if(isToBotttom){
                    if(mMoveY > 0){
                        //向上
                        smoothScrollBy(0, mMoveY / 2);
                        return true;
                    } else {
                        //向下
                        if(mScroller.getFinalY() != 0){
                            if(getScrollY() + mMoveY / 2 > 0){
                                smoothScrollBy(0, mMoveY / 2);
                                return true;
                            } else{
                                smoothScrollTo(0, 0);
                            }
                        }
                    }
                }
                else if(isToTop){
                    if(mMoveY < 0){
                        //向下
                        smoothScrollBy(0, mMoveY / 2);
                        return true;
                    } else {
                        //向上
                        if(mScroller.getFinalY() < 0){
                            smoothScrollBy(0, mMoveY / 2);
                            return true;
                        }else {
                            smoothScrollTo(0, 0);
                        }
                    }
                }
                smoothScrollTo(0, 0);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(isToBotttom || isToTop){
                    //回弹
                    smoothScrollTo(0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
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
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, Math.max(300, Math.abs(dy)));
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }


}





















