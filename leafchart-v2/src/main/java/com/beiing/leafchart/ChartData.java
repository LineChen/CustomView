package com.beiing.leafchart;

import android.graphics.Color;

import com.beiing.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/7/17 0017<br/>.
 * 描述：不同类型图表相同属性
 */
public class ChartData {

    protected List<PointValue> values = new ArrayList<>();

    protected boolean hasLabels = false;// 是否画标签

    protected int labelColor = Color.DKGRAY;//标签背景色

    protected float labelRadius = 3; //dp

    public List<PointValue> getValues() {
        return values;
    }

    public com.beiing.leafchart.bean.ChartData setValues(List<PointValue> values) {
        this.values = values;
        return this;
    }

    public boolean isHasLabels() {
        return hasLabels;
    }

    public com.beiing.leafchart.bean.ChartData setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        return this;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public com.beiing.leafchart.bean.ChartData setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    public float getLabelRadius() {
        return labelRadius;
    }

    public com.beiing.leafchart.bean.ChartData setLabelRadius(float labelRadius) {
        this.labelRadius = labelRadius;
        return this;
    }
}


