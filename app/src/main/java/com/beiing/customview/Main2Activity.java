package com.beiing.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.beiing.customview.R;
import com.beiing.customview.widget2.CameraTestView;
import com.beiing.customview.widget2.MatrixTestView;
import com.beiing.customview.widget2.Rotate3dAnimation;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Main2Activity extends AppCompatActivity {

    MatrixTestView matrixTestView;

    CropImageView cropImageView;

    CameraTestView cameraTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        matrixTestView = (MatrixTestView) findViewById(R.id.matrix_view);

        cameraTestView = (CameraTestView) findViewById(R.id.cameraView);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        cropImageView.setImageResource(R.drawable.w03);



        cameraTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 计算中心点（这里是使用view的中心作为旋转的中心点）
                final float centerX = v.getWidth() / 2.0f;
                final float centerY = v.getHeight() / 2.0f;

                //括号内参数分别为（上下文，开始角度，结束角度，x轴中心点，y轴中心点，深度，是否扭曲）
                final Rotate3dAnimation rotation = new Rotate3dAnimation(Main2Activity.this, 0, 180, centerX, centerY, 10, true);

                rotation.setDuration(3000);                         //设置动画时长
                rotation.setFillAfter(true);                        //保持旋转后效果
                rotation.setInterpolator(new LinearInterpolator());	//设置插值器
                v.startAnimation(rotation);
            }
        });

    }

//////////////////////////////////////////////////////////////////////////////////////////////
    public void scaleUp(View view) {
        matrixTestView.scaleUp();
    }

    public void scaleDown(View view) {
        matrixTestView.scaleDown();
    }

    public void traslateLeft(View view) {
        matrixTestView.traslateLeft();
    }

    public void traslateRight(View view) {
        matrixTestView.traslateRight();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

    public void upX(View view) {
        cameraTestView.upRotateX();
    }

    public void downX(View view) {
        cameraTestView.downRotateX();
    }

    public void upY(View view) {
        cameraTestView.upRotateY();
    }

    public void downY(View view) {
        cameraTestView.downRotateY();
    }


}
















