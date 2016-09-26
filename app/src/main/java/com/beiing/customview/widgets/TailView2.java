package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/9/26.<br/>
 * 描述： 绘制一条逐渐变粗的路径
 * </br>
 */
public class TailView2 extends View{
    private Paint paint;
    private Path mFingerPath;
    private float mOriginX;
    private float mOriginY;

    private List<PathElement> pathElements;

    private class PathElement{
        Path path;
        float width;
        int alpha;

        public PathElement(Path path) {
            this.path = path;
        }

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

    }


    public TailView2(Context context) {
        this(context, null, 0);
    }

    public TailView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TailView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        //-------------------------------------------------

        mFingerPath = new Path();

        pathElements = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (PathElement p: pathElements) {
            paint.setAlpha(p.getAlpha());
            paint.setStrokeWidth(p.getWidth());
            canvas.drawPath(p.getPath(), paint);
        }
//        canvas.drawPath(mFingerPath, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOriginX = x;
                mOriginY = y;
                mFingerPath.reset();
                mFingerPath.moveTo(mOriginX, mOriginY);
                break;

            case MotionEvent.ACTION_MOVE:
                getPaths(mFingerPath);
                mFingerPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                Log.e("===", pathElements.size() + "...");
                break;
        }
        invalidate();
        return true;
    }

    /**
     * path分段：一段的长度
     */
    private static final float PATH_SEGMENT_LENGTH = 100f;
    private static final float DEFAULT_WIDTH = 5;
    private static final float MAX_WIDTH = 35;

    private void getPaths(Path path){
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        int size = (int) Math.ceil(length / PATH_SEGMENT_LENGTH);
        PathElement pe = null;
        Path ps = null;
        pathElements.clear();

        if(size == 1){
            ps = new Path();
            pm.getSegment(0  , length, ps,  true);
            pe = new PathElement(ps);
            pe.setWidth(DEFAULT_WIDTH);
            pathElements.add(pe);
        } else {
            for (int i = 1; i < size; i++) {
                ps = new Path();
                pm.getSegment((i - 1) * 100f - 0.5f  , i * 100f, ps,  true);
                pe = new PathElement(ps);
                pe.setAlpha(200);
                pe.setWidth(Math.min(i * 1.2f + DEFAULT_WIDTH, MAX_WIDTH));
                pathElements.add(pe);
            }
        }

    }





}
