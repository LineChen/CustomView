package com.beiing.customview.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.beiing.customview.R;

/**
 * Created by chenliu on 2016/8/24 0024<br/>.
 * 描述：
 */
public class ClearFrogView extends View {

    private Paint mFingerPaint;

    private Path mFingerPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private float mOriginX;
    private float mOriginY;


    private Bitmap mBgBitmap;

    private boolean mFinish;

    public ClearFrogView(Context context) {
        this(context, null, 0);
    }

    public ClearFrogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearFrogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //设置绘制手指Path画笔属性
        initOutterPaint();

        mCanvas.drawColor(Color.parseColor("#c0c0c0"));

    }

    //设置绘制手指Path画笔属性
    private void initOutterPaint() {
        mFingerPaint.setColor(Color.RED);
        mFingerPaint.setAntiAlias(true);
        mFingerPaint.setStrokeCap(Paint.Cap.ROUND);
        mFingerPaint.setStrokeJoin(Paint.Join.ROUND);
        mFingerPaint.setDither(true);
        mFingerPaint.setStyle(Paint.Style.STROKE);
        mFingerPaint.setStrokeWidth(20);
    }

    private void init() {
        mFingerPaint = new Paint();
        mFingerPath = new Path();

        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.t2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        if(!mFinish) {
            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;
            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w * h];

            // 获得Bitmap上所有的像素信息
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (mPixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }
            if (wipeArea > 0 && totalArea > 0) {
                int percent = (int) (wipeArea * 100 / totalArea);
                if (percent > 60) {
                    // 清除掉图层区域
                    mFinish = true;
                    postInvalidate();
                }
            }
        }
    };

    private void drawPath() {
        mFingerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mFingerPath, mFingerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOriginX = x;
                mOriginY = y;
                mFingerPath.moveTo(mOriginX, mOriginY);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mOriginX);
                float dy = Math.abs(y - mOriginY);
                if(dx > 3 || dy > 3){
                    mFingerPath.lineTo(x, y);
                }

                mOriginX = x;
                mOriginY = y;
                break;

            case MotionEvent.ACTION_UP:
                if(!mFinish)
                    new Thread(mRunnable).start();
                break;
        }
        if(!mFinish)
            invalidate();
        return true;
    }
}

















