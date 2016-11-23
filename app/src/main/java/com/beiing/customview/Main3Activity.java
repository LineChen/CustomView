package com.beiing.customview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.beiing.customview.widget2.PileLayout;
import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.roundimage.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    String[] urls = {"http://img2.imgtn.bdimg.com/it/u=1939271907,257307689&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1939271907,257307689&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg"
    };

    PileLayout pileLayout;

    LeafLineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        pileLayout = (PileLayout) findViewById(R.id.pile_layout);

        lineChart = (LeafLineChart) findViewById(R.id.leaf_chart);

        initPraises();

        initLineChart();
    }



    public void initPraises(){
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < urls.length; i++) {
            CircleImageView imageView = (CircleImageView) inflater.inflate(R.layout.item_praise, pileLayout, false);
            Glide.with(this).load(urls[i]).into(imageView);
            pileLayout.addView(imageView);
        }

    }


    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        lineChart.setAxisX(axisX);
        lineChart.setAxisY(axisY);

        List<Line> lines = new ArrayList<>();
        lines.add(getFoldLine());
        lines.add(getCompareLine());
        lineChart.setChartData(lines);

        lineChart.showWithAnimation(3000);

    }

    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "月");
            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10));
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getFoldLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            int var = (int) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5"))
                .setLineWidth(3)
                .setPointColor(Color.YELLOW)
                .setCubic(true)
                .setPointRadius(3)
                .setFill(true)
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#33B5E5"));
        return line;
    }

    private Line getCompareLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            int var = (int) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.MAGENTA)
                .setLineWidth(3)
                .setPointColor(Color.MAGENTA)
                .setCubic(true)
                .setPointRadius(3)
                .setFill(false)
                .setHasLabels(false);
        return line;
    }

}
