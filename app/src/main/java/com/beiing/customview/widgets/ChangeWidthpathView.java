package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by chenliu on 2016/9/21.<br/>
 * 描述：
 * </br>
 */
public class ChangeWidthpathView extends View{
    private static final float STROKE_WIDTH = 5f;
    private Paint paint = new Paint();
    private Path mPath = new Path();
    ArrayList<Path> mPaths = new ArrayList<Path>();
    ArrayList<Integer> mStrokes = new ArrayList<Integer>();

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    private int lastStroke = -1;
    int variableWidthDelta = 0;

    private static final float STROKE_DELTA = 0.0001f; // for float comparison
    private static final float STROKE_INCREMENT = 0.01f; // amount to interpolate
    private float currentStroke = STROKE_WIDTH;
    private float targetStroke = STROKE_WIDTH;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;



    public ChangeWidthpathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeWidthpathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    public void clear() {
        mPath.reset();
        // Repaints the entire view.
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)  {
        for(int i=0; i<mPaths.size();i++) {
            paint.setStrokeWidth(mStrokes.get(i));
            canvas.drawPath(mPaths.get(i), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        int historySize = event.getHistorySize();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                resetDirtyRect(eventX, eventY);
//                  mPath.reset();
                mPath.moveTo(eventX, eventY);
                mX = eventX;
                mY = eventY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (event.getPressure()>=0.00 && event.getPressure()<0.05) {
                    variableWidthDelta = -2;
                } else if (event.getPressure()>=0.05 && event.getPressure()<0.10) {
                    variableWidthDelta = -2;
                } else if (event.getPressure()>=0.10 && event.getPressure()<0.15) {
                    variableWidthDelta = -2;
                } else if (event.getPressure()>=0.15 && event.getPressure()<0.20) {
                    variableWidthDelta = -2;
                } else if (event.getPressure()>=0.20 && event.getPressure()<0.25) {
                    variableWidthDelta = -2;
                } else if (event.getPressure() >= 0.25 && event.getPressure()<0.30) {
                    variableWidthDelta = 1;
                } else if (event.getPressure() >= 0.30 && event.getPressure()<0.35) {
                    variableWidthDelta = 2;
                } else if (event.getPressure() >= 0.35 && event.getPressure()<0.40) {
                    variableWidthDelta = 3;
                } else if (event.getPressure() >= 0.40 && event.getPressure()<0.45) {
                    variableWidthDelta = 4;
                } else if (event.getPressure() >= 0.45 && event.getPressure()<0.60) {
                    variableWidthDelta = 5;
                }

                // if current not roughly equal to target
                if( Math.abs(targetStroke - currentStroke) > STROKE_DELTA )
                {
                    // move towards target by the increment
                    if( targetStroke > currentStroke)
                    {
                        currentStroke = Math.min(targetStroke, currentStroke + STROKE_INCREMENT);
                    }
                    else
                    {
                        currentStroke = Math.max(targetStroke, currentStroke - STROKE_INCREMENT);
                    }

                }
                mStrokes.add((int) currentStroke);

                targetStroke = variableWidthDelta;

                float dx = Math.abs(eventX - mX);
                float dy = Math.abs(eventY - mY);

                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    if(lastStroke != variableWidthDelta) {
                        mPath.lineTo(mX, mY);

                        mPath = new Path();
                        mPath.moveTo(mX,mY);
                        mPaths.add(mPath);
                    }

                    mPath.quadTo(mX, mY, (eventX + mX)/2, (eventY + mY)/2);
                    mX = eventX;
                    mY = eventY;
                }

                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                }
                mPath.lineTo(mX, mY);
                break;
            }
        }

        // Include half the stroke width to avoid clipping.
        invalidate();

        lastTouchX = eventX;
        lastTouchY = eventY;
        lastStroke = variableWidthDelta;

        return true;
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        }  else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {
        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

}
