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
 * 描述：
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
        paint.setStrokeWidth(5);
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
//                if(getPathlength(mFingerPath) > 20){
////                    mFingerPath.rMoveTo(x, y);
////                    PathElement e = new PathElement(mFingerPath);
////                    pathElements.add(e);
//                    Log.e("====", "moveTo:" + x + "," + y);
//                } else {
//
//                    Log.e("====", "lineTo:" + x + "," + y);
//                }

                mFingerPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                Log.e("===", pathElements.size() + "...");
                break;
        }
        invalidate();
        return true;
    }

    private void getPaths(Path path){
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        int size = (int) (length / 50);
        Path ps = null;
        PathElement pe = null;
        pathElements.clear();
        for (int i = 1; i < size; i++) {
            ps = new Path();
            pm.getSegment((i - 1) * 50f , i * 50f, ps,  true);
            pe = new PathElement(ps);
            pe.setAlpha(255);
            pe.setWidth(i * 2);
            pathElements.add(pe);
        }
    }





}
