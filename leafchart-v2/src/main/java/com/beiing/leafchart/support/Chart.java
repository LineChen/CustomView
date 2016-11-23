package com.beiing.leafchart.support;

import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.ChartData;

import java.util.List;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public interface Chart {

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    Axis getAxisX();

    Axis getAxisY();
}
