package com.beiing.customview;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beiing.customview.widgets.ColorTrackView;
import com.beiing.customview.widgets.DynamicHeartView;

public class MainActivity extends AppCompatActivity {

    ColorTrackView colorTrackView;

    DynamicHeartView dynamicHeartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        colorTrackView = (ColorTrackView) findViewById(R.id.color_tracker_view);

        dynamicHeartView = (DynamicHeartView) findViewById(R.id.dynamic_view);
        dynamicHeartView.startPathAnim(2000);
    }


    public void startLeftChange(View view) {
        colorTrackView.setDirection(0);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1).setDuration(2000)
                .start();
    }

    public void startRightChange(View view) {
        colorTrackView.setDirection(1);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1).setDuration(2000)
                .start();
    }
}
