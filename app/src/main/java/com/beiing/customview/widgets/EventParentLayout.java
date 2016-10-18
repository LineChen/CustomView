package com.beiing.customview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by chenliu on 2016/10/12.<br/>
 * 描述：
 * </br>
 */
public class EventParentLayout extends LinearLayout{

    private String TAG = getClass().getSimpleName();

    public EventParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private float mDownX;
    private float mDownY;

    private int moveY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent");

        int action = ev.getAction();
        if(action == MotionEvent.ACTION_MOVE && Math.abs(moveY) > 0){
            return true;
        }

//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mDownX = ev.getX();
//                mDownY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                return true;
//            case MotionEvent.ACTION_UP:
//                break;
//        }

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent");
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mDownX = ev.getX();
//                mDownY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(mDownY - ev.getY() > 0){
//                    //向上滑
//                    Log.e(TAG, "up");
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent");
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) (mDownY - ev.getY());
//                if(mDownY - ev.getY() > 0){
//                    //向上滑
//                    Log.e(TAG, "up");
//                    return true;
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
