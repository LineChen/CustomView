package com.beiing.customview.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenliu on 2016/9/21.<br/>
 * 描述：
 * </br>
 */
public class TailView extends View {

    private final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private Paint paint;
    private Path mFingerPath;
    private Path clearPath;
    private float mOriginX;
    private float mOriginY;
    private PathMeasure measure;

    private Canvas mCanvas;
    private Bitmap mBitmap;

    public TailView(Context context) {
        this(context, null, 0);
    }

    public TailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        //-------------------------------------------------
        mFingerPath = new Path();
        clearPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        mCanvas.drawPath(mFingerPath, paint);

        if(isStartClear){
            paint.setXfermode(xfermode);
            paint.setStrokeWidth(10);
            mCanvas.drawPath(clearPath, paint);
            paint.setXfermode(null);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    private boolean isStartClear = false;

    public void setPhase(float phase) {
        paint.setPathEffect(createPathEffect(measure == null ? 0 : measure.getLength(), phase, 0.0f));
        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[] { phase*pathLength, pathLength}, 0);
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
                    clearPath.moveTo(mOriginX, mOriginY);
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(x - mOriginX);
                    float dy = Math.abs(y - mOriginY);
                    if(dx > 3 || dy > 3){
                        mFingerPath.lineTo(x, y);
                        clearPath.lineTo(x, y);
                    }

                    mOriginX = x;
                    mOriginY = y;
                    break;

                case MotionEvent.ACTION_UP:
                    isStartClear = true;
                    startDraw();
                    break;


            }
            invalidate();
            return true;
    }

    private void startDraw(){
        measure = new PathMeasure(clearPath, false);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "phase", 0f, 1f)
                .setDuration(1000);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStartClear = false;
                mFingerPath.reset();
                clearPath.reset();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
