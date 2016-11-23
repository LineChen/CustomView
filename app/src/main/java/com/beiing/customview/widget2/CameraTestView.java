package com.beiing.customview.widget2;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by chenliu on 2016/11/17.<br/>
 * 描述：
 * </br>
 */
public class CameraTestView extends View {

    String TAG = getClass().getSimpleName();

    Paint paint;

    Camera camera;

    Matrix matrixCanvas;

    private int centerX;
    private int centerY;

    private float canvasRotateX = 0;
    private float canvasRotateY = 0;

    public CameraTestView(Context context) {
        this(context, null, 0);
    }

    public CameraTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(25);

        camera = new Camera();
        matrixCanvas = new Matrix();
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
        translateCanvas(canvas, 0f, 0f, 180f);
        canvas.drawCircle(centerX, centerY, getWidth() / 3 , paint);
        canvas.restore();
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

    public void upRotateX() {
        canvasRotateX += 10;
        invalidate();
    }

    public void upRotateY() {
        canvasRotateY += 10;
        invalidate();
    }

    public void downRotateX(){
        canvasRotateX -= 10;
        invalidate();
    }

    public void downRotateY(){
        canvasRotateY -= 10;
        invalidate();
    }


    public float dp2px(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    public float sp2px(float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }



}





























