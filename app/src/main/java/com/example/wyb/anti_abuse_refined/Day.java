package com.example.wyb.anti_abuse_refined;

import java.io.Serializable;

public class Day implements Serializable {
    /**年**/
    public int year;
    /**月**/
    public int month;
    /**日**/
    public int date;
    /**周几**/
    public int week;
    /**贡献次数，默认0**/
    public int contribution = 0;
    /**默认颜色,根据提交次数改变**/
    public int colour = 0xFFEEEEEE;

    /**方格坐标，左上点，右下点，确定矩形范围**/
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    @Override
    public String toString() {
        //这里直接在弹出框中显示
        return ""+year+"年"+month+"月"+date+"日周"+week+","+contribution+"次";
    }
}