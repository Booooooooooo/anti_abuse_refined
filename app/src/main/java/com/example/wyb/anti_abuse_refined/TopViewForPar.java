package com.example.wyb.anti_abuse_refined;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class TopViewForPar extends View {
    private Paint mPaint;
    private static int toolbarHeight;
    private static int statusHeight;
    private boolean isRed;

    public TopViewForPar(Context context, AttributeSet attrs, boolean isRed) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.isRed = isRed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //int color = (int)Long.parseLong("CAD5FF", 16);
        if(!isRed)mPaint.setColor(Color.rgb(202, 213, 255));
        else mPaint.setColor(Color.rgb(255,213, 184));
        LinearGradient lg=new LinearGradient(0,0,getWidth(),getHeight(),Color.rgb(135,179, 255),Color.rgb(202, 213, 255), Shader.TileMode.CLAMP);
        getStatusHeight(getContext());
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper) return getActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }

    public static void getStatusHeight(Context context) {
        //int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
            toolbarHeight = getActivity(context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            toolbarHeight =  toolbarHeight - statusHeight;
            Log.i("TAG", "statusHeight:" + statusHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
        //return statusHeight;
    }
}
