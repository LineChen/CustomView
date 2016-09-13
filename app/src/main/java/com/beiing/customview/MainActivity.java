package com.beiing.customview;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.beiing.customview.widgets.ColorTrackView;
import com.beiing.customview.widgets.DynamicHeartView;
import com.beiing.customview.widgets.PtRefreshListView;

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

        initPtrListView();
    }

    private void initPtrListView() {
        PtRefreshListView refreshListView = (PtRefreshListView) findViewById(R.id.ptr_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_listview, new String[]{"asdf", "ASD", "jdjlk", "sdfiou", "65asd"});
        refreshListView.setAdapter(adapter);
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
