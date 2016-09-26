package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenliu on 2016/9/19.<br/>
 * 描述：
 * </br>
 */
public class ToothPositionView extends View {

    private Path circlepath;

    private Paint paint;

    private Path tooth;

    public ToothPositionView(Context context) {
        this(context, null, 0);
    }

    public ToothPositionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToothPositionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initToothPath();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        circlepath = new Path();
        circlepath.addOval(new RectF(radius, radius, getMeasuredWidth() - radius, getMeasuredHeight() - radius), Path.Direction.CW);

        PathEffect pe = new PathDashPathEffect(tooth, radius * 2, 0, PathDashPathEffect.Style.TRANSLATE);
        paint.setPathEffect(pe);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(circlepath, paint);
    }


    float radius;
    private void initToothPath(){
        tooth = new Path();
        radius = (float) (Math.PI *(getMeasuredWidth() - 100 - 20)) / 32 / 2;
        tooth.addCircle(0, 0, radius, Path.Direction.CW);
    }


}





