package com.beiing.customview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chenliu on 2016/10/14.<br/>
 * 描述：控制子控件是否可点击
 * </br>
 */
public class EnableLinearLayout extends LinearLayout {

    //子控件是否可以接受点击事件
    private boolean isChildEnable = true;

    public EnableLinearLayout(Context context) {
        this(context, null, 0);
    }

    public EnableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //返回true则拦截子控件所有点击事件，如果isChildEnable为true，则需返回false
        return !isChildEnable;
    }

    public void setChildEnable(boolean childEnable) {
        isChildEnable = childEnable;
        setAlpha(childEnable ? 1f : 0.4f);
        setDescendantFocusability(childEnable ? FOCUS_AFTER_DESCENDANTS : FOCUS_BLOCK_DESCENDANTS);
    }


}
