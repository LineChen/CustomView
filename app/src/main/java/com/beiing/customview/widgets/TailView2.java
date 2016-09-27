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
 * 描述：绘制一条宽度逐渐变大的路径
 * </br>
 */
public class TailView2 extends View{
    private Paint paint;
    private Path mFingerPath;
    private float mOriginX;
    private float mOriginY;

    private List<PathSegment> pathSegments;

    private class PathSegment{
        Path path;
        float width;
        int alpha;

        public PathSegment(Path path) {
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
        pathSegments = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (PathSegment p: pathSegments) {
            paint.setAlpha(p.getAlpha());
            paint.setStrokeWidth(p.getWidth());
            canvas.drawPath(p.getPath(), paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pathSegments.clear();
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
                break;
        }
        invalidate();
        return true;
    }


    /**
     * 越小，线条锯齿度越小
     */
    private static final float DEFAULT_SEGMENT_LENGTH = 10F;
    private static final float DEFAULT_WIDTH = 3F;
    private static final float MAX_WIDTH = 45F;

    /**
     * 截取path
     * @param path
     */
    private void getPaths(Path path){
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        int segmentSize = (int) Math.ceil(length / DEFAULT_SEGMENT_LENGTH);
        Path ps = null;
        PathSegment pe = null;
        int nowSize = pathSegments.size();//集合中已经有的
        if(nowSize == 0){
            ps = new Path();
            pm.getSegment(0, length, ps, true);
            pe = new PathSegment(ps);
            pe.setAlpha(255);
            pe.setWidth(DEFAULT_WIDTH);
            pathSegments.add(pe);
        } else{
            for (int i = nowSize; i < segmentSize; i++) {
                ps = new Path();
                pm.getSegment((i - 1) * DEFAULT_SEGMENT_LENGTH - 0.4f, Math.min(i * DEFAULT_SEGMENT_LENGTH, length), ps,  true);
                pe = new PathSegment(ps);
                pe.setAlpha(255);
                pe.setWidth((float) Math.min(MAX_WIDTH, i * 0.3 + DEFAULT_WIDTH));
                pathSegments.add(pe);
            }
        }
    }


}