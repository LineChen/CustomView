package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by chenliu on 2016/9/28.<br/>
 * 描述：淘宝详情页，上拉查看
 * </br>
 */
public class TwoPageScrollView extends LinearLayout {

    private ScrollEndScrollView scrollView1;

    private ScrollEndScrollView scrollView2;

    /**
     * scrollView1是否滚动到底部
     */
    private boolean isToBotttom;

    /**
     * scrollView2是否滚动到顶部
     */
    private boolean isToTop;

    private Scroller mScroller; //滑动控制器

    private int mMaxScrollY;//最大移动距离

    public TwoPageScrollView(Context context) {
        this(context, null, 0);
    }

    public TwoPageScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoPageScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if(getChildCount() == 2){
            View child1 = getChildAt(0);
            if (child1 instanceof ScrollEndScrollView){
                scrollView1 = (ScrollEndScrollView) child1;
            }
            View child2 = getChildAt(1);
            if(child2 instanceof ScrollEndScrollView){
                scrollView2 = (ScrollEndScrollView) child2;
            }
        }
        initEvent();
    }

    private void initEvent() {
        if (scrollView1 != null) {
            scrollView1.addOnScrollEndListener(scrollEndListener);

            scrollView1.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("------", "scrollView1:onTouch" );
                    return false;
                }
            });
        }
        if (scrollView2 != null) {
            scrollView2.addOnScrollEndListener(scrollEndListener);

            scrollView2.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("------", "scrollView1:onTouch" );
                    return false;
                }
            });

        }
    }

    private ScrollEndScrollView.OnScrollEndListener scrollEndListener = new ScrollEndScrollView.OnScrollEndListener() {
        @Override
        public void scrollToBottom(View view) {
            if(view == scrollView1){
                isToBotttom = true;
                view.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void scrollToTop(View view) {
            if(view == scrollView2){
                isToTop = true;
                view.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void scrollToMiddle(View view) {
            if(view == scrollView1){
                isToBotttom = false;
                view.setBackgroundColor(Color.DKGRAY);
            }
            if(view == scrollView2){
                isToTop = false;
                view.setBackgroundColor(Color.DKGRAY);
            }
        }
    };


    protected int mMoveY;
    protected int mLastY;


    /**
     * 1.scrollView1滑动到底部时,不松手继续往上拉,拉不动； 松手,再往上拉,可以拉动
     *
     * 2.scrollView2滑动到顶部时，不松手继续往下拉，拉不动； 松手，再往下拉，可以拉动
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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
                        smoothScrollBy(0, mMoveY);
                        return true;
                    } else {
                        //向下
                        if(mScroller.getStartY() != 0){
                            smoothScrollBy(0, mMoveY);
                            return true;
                        }
                        return super.dispatchTouchEvent(ev);
                    }
                }

               if(isToTop){
                    if(mMoveY > 0){
                        //向上
                        return super.dispatchTouchEvent(ev);
                    } else {
                        //向下
                        smoothScrollBy(0, mMoveY / 2);
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回弹
                if(isToBotttom && Math.abs(mScroller.getStartY()) > 200){
                    smoothScrollTo(0, scrollView2.getHeight());
                } else {
                    smoothScrollBy(0, -mScroller.getFinalY());
                }

                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
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
