package com.beiing.customview.widget2;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by chenliu on 2016/11/16.<br/>
 * 描述：
 * </br>
 */
public class MatrixTestView extends View {

    String TAG = getClass().getSimpleName();


    Paint paint;

    Matrix matrixCanvas;

    Camera camera;

    private int centerX;
    private int centerY;

    private float canvasRotateX = 0;
    private float canvasRotateY = 0;

    private float canvasMaxRotateDegree = 60;

    /**
     * z轴方向偏移，不能为0，否则控件不可见,近大远小
     */
    float zDepthScaleRing = 180;

    private ValueAnimator steadyAnim;

    public MatrixTestView(Context context) {
        this(context, null);
    }

    public MatrixTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(25);

        matrixCanvas = new Matrix();
        camera = new Camera();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#99237ead"));
        rotateCanvas(canvas);
        drawContent(canvas);
    }

    private void drawContent(Canvas canvas) {
        canvas.save();
        translateCanvas(canvas, 0f, 0f, zDepthScaleRing);
        canvas.drawCircle(centerX, centerY, getWidth() / 2 , paint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                cancelSteadyAnimIfNeed();
                rotateCanvasWhenMove(x, y);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                rotateCanvasWhenMove(x, y);
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                cancelSteadyAnimIfNeed();
                startNewSteadyAnim();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void rotateCanvasWhenMove(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;

        float percentX = dx / (getWidth() / 2);
        float percentY = dy / (getHeight() / 2);

        if (percentX > 1f) {
            percentX = 1f;
        } else if (percentX < -1f) {
            percentX = -1f;
        }
        if (percentY > 1f) {
            percentY = 1f;
        } else if (percentY < -1f) {
            percentY = -1f;
        }

        canvasRotateY = canvasMaxRotateDegree * percentX;
        canvasRotateX = -(canvasMaxRotateDegree * percentY);
    }

    private void rotateCanvas(Canvas canvas) {
        matrixCanvas.reset();

        camera.save();
        camera.rotateX(canvasRotateX);
        camera.rotateY(canvasRotateY);
        camera.getMatrix(matrixCanvas);
        camera.restore();

        int matrixCenterX = centerX;
        int matrixCenterY = centerY;
        // This moves the center of the view into the upper left corner (0,0)
        // which is necessary because Matrix always uses 0,0, as it's transform point
        //移动到中心，只调用post方法时，控件会出现在右下方
        matrixCanvas.preTranslate(-matrixCenterX, -matrixCenterY);
        // This happens after the camera rotations are applied, moving the view
        // back to where it belongs, allowing us to rotate around the center or
        // any point we choose
        //移动到中心，只调用pre方法时，控件会出现在左上方
        //如果这两行都不调用，效果出错
        matrixCanvas.postTranslate(matrixCenterX, matrixCenterY);

        canvas.concat(matrixCanvas);
    }

    private void translateCanvas(Canvas canvas, float x, float y, float z) {
        matrixCanvas.reset();
        camera.save();
        camera.translate(x, y, z);
        camera.getMatrix(matrixCanvas);
        camera.restore();

        int matrixCenterX = centerX;
        int matrixCenterY = centerY;
        matrixCanvas.preTranslate(-matrixCenterX, -matrixCenterY);
        matrixCanvas.postTranslate(matrixCenterX, matrixCenterY);

        canvas.concat(matrixCanvas);
    }

    private void startNewSteadyAnim() {
        final String propertyNameRotateX = "canvasRotateX";
        final String propertyNameRotateY = "canvasRotateY";

        PropertyValuesHolder holderRotateX = PropertyValuesHolder.ofFloat(propertyNameRotateX, canvasRotateX, 0);
        PropertyValuesHolder holderRotateY = PropertyValuesHolder.ofFloat(propertyNameRotateY, canvasRotateY, 0);
        steadyAnim = ValueAnimator.ofPropertyValuesHolder(holderRotateX, holderRotateY);
        steadyAnim.setDuration(1000);
        steadyAnim.setInterpolator(new BounceInterpolator());
        steadyAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                canvasRotateX = (float) animation.getAnimatedValue(propertyNameRotateX);
                canvasRotateY = (float) animation.getAnimatedValue(propertyNameRotateY);
                invalidate();
            }
        });
        steadyAnim.start();
    }


    private void cancelSteadyAnimIfNeed() {
        if (steadyAnim != null && (steadyAnim.isStarted() || steadyAnim.isRunning())) {
            steadyAnim.cancel();
        }
    }


    /**
     * 获得当前的缩放比例
     *
     * @return
     */
    public final float getScale(){
        matrixCanvas.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];

    float scale = 1;
    float MIN_SCALE = 0.4f;
    float MAX_SCALE = 1.4f;

    public void scaleUp(){
        scale += 0.1;
        matrixCanvas.postScale(scale / getScale(), scale / getScale(), getWidth() / 2, getHeight() / 2);
        invalidate();
    }

    public void scaleDown(){
        scale -= 0.1;
        matrixCanvas.postScale(scale / getScale(), scale / getScale(), getWidth() / 2, getHeight() / 2);
        invalidate();
    }

    public void traslateLeft(){
        matrixCanvas.preTranslate(-20, 0);
        invalidate();
    }

    public void traslateRight(){
        matrixCanvas.preTranslate(20, 0);
        invalidate();
    }



}






