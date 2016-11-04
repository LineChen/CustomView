package com.beiing.customview;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beiing.customview.widgets.ColorTrackView;
import com.beiing.customview.widgets.DiffuseView;
import com.beiing.customview.widgets.DynamicHeartView;
import com.beiing.customview.widgets.ElasticTouchListener;
import com.beiing.customview.widgets.PtRefreshListView;
import com.beiing.customview.widgets.calendar.MonthAdapter;
import com.beiing.customview.widgets.calendar.MonthView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ColorTrackView colorTrackView;

    DynamicHeartView dynamicHeartView;

    MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dynamicHeartView = (DynamicHeartView) findViewById(R.id.dynamic_view);
        dynamicHeartView.startPathAnim(2000);

        initPtrListView();

        DiffuseView diffuseView = (DiffuseView) findViewById(R.id.diff_view);
        diffuseView.start();

        monthView = (MonthView) findViewById(R.id.monthView1);
        monthView.setAdapter(new MonthAdapter() {
            @Override
            public View createCellView(ViewGroup viewGroup, int position) {
                TextView textView  = new TextView(MainActivity.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
            @Override
            public void bindCellView(ViewGroup viewGroup, View child, int position, Calendar calendar) {
                TextView textView  = (TextView) child;
                textView.setText(""+calendar.get(Calendar.DAY_OF_MONTH));
            }
        });

    }

    private void initPtrListView() {
        ListView refreshListView = (ListView) findViewById(R.id.ptr_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_listview,
                new String[]{"asdf", "ASD", "jdjlk", "sdfiou", "65asd", "asdf", "ASD", "jdjlk", "sdfiou", "65asd"});
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

    public void btnClick(View view) {
        Toast.makeText(MainActivity.this, "click not canceled", Toast.LENGTH_SHORT).show();
    }

    public void click(View view) {
        Toast.makeText(MainActivity.this, "click not canceled", Toast.LENGTH_SHORT).show();
    }

    public void noCancel(View view) {
        Toast.makeText(MainActivity.this, "click not canceled", Toast.LENGTH_SHORT).show();
    }
}
