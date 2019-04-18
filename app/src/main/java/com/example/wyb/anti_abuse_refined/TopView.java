package com.example.wyb.anti_abuse_refined;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TopView extends View {
    private Paint mPaint;

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //int color = (int)Long.parseLong("CAD5FF", 16);
        mPaint.setColor(Color.rgb(202, 213, 255));
        canvas.drawRect(0, getStatusHeight(getContext()), getWidth(), getHeight(), mPaint);
//        mPaint.setColor(Color.BLUE);
//        mPaint.setTextSize(80);
//        String text = "本月异常次数";
//        canvas.drawText(text, 50, getStatusHeight(getContext()) + getHeight() / 2, mPaint);
//        text = "2/14";
//        mPaint.setTextSize(96);
//        canvas.drawText(text, 50, getStatusHeight(getContext()) + getHeight() - mPaint.getTextSize(), mPaint);

    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
            Log.i("TAG", "statusHeight:" + statusHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
