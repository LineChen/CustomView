package com.beiing.customview.widget2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenliu on 2016/12/1.<br/>
 * 描述：
 * </br>
 */
public class LinearGradientView extends View{

    Paint paint;
    Path path;


    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 200);
        path.lineTo(200, 200);
        path.lineTo(200, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LinearGradient linearGradient = new LinearGradient(0, 0 ,200, 0, Color.RED, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);

        canvas.drawPath(path, paint);
//        canvas.drawRect(0, 0, 200, 200, paint);
    }


}






















