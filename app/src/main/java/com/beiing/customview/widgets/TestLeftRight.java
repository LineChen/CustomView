package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenliu on 2016/8/28.<br/>
 * 描述：左右不停移动
 * </br>
 */
public class TestLeftRight extends View implements Runnable{

    private Paint bgPaint;

    private float left;

    public TestLeftRight(Context context) {
        super(context);
    }

    public TestLeftRight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestLeftRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.RED);

//        new Thread(this).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(left, 0, left + 50, 50, bgPaint);

    }


    @Override
    public void run() {
        while (true){
            left += 20;
            if(left + 50 >= getMeasuredWidth()){
                left = 0;
            }
            postInvalidate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}











