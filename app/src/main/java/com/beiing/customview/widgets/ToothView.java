package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenliu on 2016/9/19.<br/>
 * 描述：
 * </br>
 */
public class ToothView extends View {

    private Path circlepath;

    private Paint paint;

    private PathMeasure pathMeasure;

    private float[] currentPosition = new float[2];

    private float smallRadius;

    private float bigRadius;

    public ToothView(Context context) {
        this(context, null, 0);
    }

    public ToothView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToothView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        initPath();

        initRadius();
    }

    private void initPath() {
        circlepath = new Path();
        circlepath.addCircle((getMeasuredWidth() - 50) / 2, (getMeasuredHeight() - 50)/ 2, bigRadius, Path.Direction.CW);

        pathMeasure = new PathMeasure(circlepath, true);
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawPath(circlepath, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        for (int i = 0; i < 32; i++) {
            pathMeasure.getPosTan(i * stepArc, currentPosition, null);

            canvas.drawCircle(currentPosition[0], currentPosition[1], smallRadius, paint);
        }


    }

    private float stepArc;
    private void initRadius(){
        bigRadius = (getMeasuredWidth() - 150f) / 2f;
        smallRadius = (float) (Math.sin(Math.PI / 32) * bigRadius);
        stepArc = (float) (2 * Math.PI * bigRadius / 32 + 0.1);
    }


}
























