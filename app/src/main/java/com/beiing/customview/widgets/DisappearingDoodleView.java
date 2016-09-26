package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/9/26.<br/>
 * 描述：
 * </br>
 */
public class DisappearingDoodleView extends View {
    final static String TAG = "DoodleView";
    class LineElement {
        static final public int ALPHA_STEP = 5;
        static final public int SUBPATH_DIMENSION = 8;
        public LineElement(){
            mPaint = new Paint();
            mPaint.setARGB(255, 255, 0, 0);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(16);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setStyle(Paint.Style.STROKE);
        }
        public LineElement(Paint paint){
            mPaint = paint;
        }

        public void setPaint(Paint paint){
            mPaint = paint;
        }

        public void setAlpha(int alpha){
            mPaint.setAlpha(alpha);
        }


        public float mStartX = -1;
        public float mStartY = -1;
        public float mEndX = -1;
        public float mEndY = -1;
        public Paint mPaint;
    }

    private LineElement mCurrentLine = null;
    private List<LineElement> mLines = null;

    private long mElapsed = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            DisappearingDoodleView.this.invalidate();
        }
    };

    public DisappearingDoodleView(Context context){
        super(context);
    }

    public DisappearingDoodleView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        mElapsed = SystemClock.elapsedRealtime();
        if(mLines != null) {
            for (LineElement e : mLines) {
                if(e.mStartX < 0 || e.mEndY < 0) continue;
                canvas.drawLine(e.mStartX, e.mStartY, e.mEndX, e.mEndY, e.mPaint);
            }
            compactPaths();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){// end one line after finger release
            mCurrentLine.mEndX = x;
            mCurrentLine.mEndY = y;
            mCurrentLine = null;
            invalidate();
            return true;
        }

        if(action == MotionEvent.ACTION_DOWN){
            mCurrentLine = new LineElement();
            addToPaths(mCurrentLine);

            mCurrentLine.mStartX = x;
            mCurrentLine.mStartY = y;
            return true;
        }

        if(action == MotionEvent.ACTION_MOVE) {
            mCurrentLine.mEndX = x;
            mCurrentLine.mEndY = y;
            mCurrentLine = new LineElement();
            addToPaths(mCurrentLine);

            mCurrentLine.mStartX = x;
            mCurrentLine.mStartY = y;
        }


        if(mHandler.hasMessages(1)){
            mHandler.removeMessages(1);
        }
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessageDelayed(msg, 0);

        return true;
    }

    private void addToPaths(LineElement element){
        if(mLines == null) {
            mLines = new ArrayList<LineElement>() ;
        }

        mLines.add(element);
}

    public void compactPaths(){

        int size = mLines.size();
        int index = size - 1;
        if(size == 0) return;
        int baseAlpha = 255 - LineElement.ALPHA_STEP;
        int itselfAlpha;
        LineElement line;
        for(; index >=0 ; index--, baseAlpha -= LineElement.ALPHA_STEP){
            line = mLines.get(index);
            itselfAlpha = line.mPaint.getAlpha();
            if(itselfAlpha == 255){
                if(baseAlpha <= 0){
                    ++index;
                    break;
                }
                line.setAlpha(baseAlpha);
            }else{
                itselfAlpha -= LineElement.ALPHA_STEP;
                if(itselfAlpha <= 0){
                    ++index;
                    break;
                }
                line.setAlpha(itselfAlpha);
            }
        }

        if(index >= size){
            // all sub-path should disappear
            mLines = null;
        }
        else if(index >= 0){
            //Log.i(TAG, "compactPaths from " + index + " to " + (size - 1));
            mLines = mLines.subList(index, size);
        }else{
            // no sub-path should disappear
        }

        long interval = 40 - SystemClock.elapsedRealtime() + mElapsed;
        if(interval < 0) interval = 0;
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessageDelayed(msg, interval);
    }
}
