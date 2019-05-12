package com.example.wyb.anti_abuse_refined;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hour implements Serializable {

    public int hour;
    public int minute;
    /**贡献次数，默认0**/
    public int contribution = 0;
    /**默认颜色,根据提交次数改变**/
    public int colour = Color.rgb(230, 230, 230);//灰色
    public boolean vis = false;

    /**方格坐标，左上点，右下点，确定矩形范围**/
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    public boolean[] normal = new boolean[4];

    @Override
    public String toString() {
        //这里直接在弹出框中显示
        return ""+hour+" "+minute+","+contribution+"次";
    }
}
