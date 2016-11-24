package com.beiing.leafchart;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：坐标轴上点的值
 * </br>
 */
public class AxisValue {
    /**刻度值**/
    private String label;

    /**x坐标**/
    private float pointX;

    /**y坐标**/
    private float pointY;

    public AxisValue(){

    }

    public AxisValue(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }

    @Override
    public String toString() {
        return "AxisValue{" +
                "label='" + label + '\'' +
                ", pointX=" + pointX +
                ", pointY=" + pointY +
                '}';
    }
}
