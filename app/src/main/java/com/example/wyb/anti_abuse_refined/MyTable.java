package com.example.wyb.anti_abuse_refined;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.provider.Contacts;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class MyTable extends View {

    private Context context;
    /**灰色方格的默认颜色**/
    private final int DEFAULT_BOX_COLOUR = Color.rgb(230, 230, 230);
    private final int NORMAL_BOX_COLOUR = Color.rgb(202, 213, 255);
    //private final int DEFAULT_BOX_COLOUR = ContextCompat.getColor(context, R.color.colorPrimary);
    /**提交次数颜色值**/
    private final int[] COLOUR_LEVEL =
            new int[]{DEFAULT_BOX_COLOUR,NORMAL_BOX_COLOUR, Color.rgb(123, 150, 254), Color.rgb(255, 211, 114),Color.rgb(255, 60, 34)};//灰蓝蓝黄红

    private String[] hours =
            new String[]{"8:00~13:00","14:00~20:00"};
    /**默认的padding,绘制的时候不贴边画**/
    private int padding = 35;
    /**小方格的默认边长**/
    private int boxSide = 20;
    /**小方格间的默认间隔**/
    private int boxInterval = 8;
    /**所有周的列数**/
    private int column = 12;

    public List<Hour> mHours;//一年中所有的天
    private Paint boxPaint;//方格画笔
    private Paint textPaint;//文字画笔
    private Paint infoPaint;//弹出框画笔

    private Paint.FontMetrics metrics;//测量文字

    private float downX;//按下的点的X坐标
    private float downY;//按下的点的Y坐标
    private Hour clickDay;//按下所对应的天


    public MyTable(Context context) {
        this(context, null);
        context = context;
    }

    public MyTable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        context = context;
    }

    public MyTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context = context;
        initView();
    }

    public void initView() {
        mHours = HourFactory.getGrids();
        //方格画笔
        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setStrokeWidth(2);
        boxPaint.setColor(DEFAULT_BOX_COLOUR);
        boxPaint.setAntiAlias(true);
        //文字画笔
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36);
        textPaint.setAntiAlias(true);
        //弹出的方格信息画笔
        infoPaint = new Paint();
        infoPaint.setStyle(Paint.Style.FILL);
        infoPaint.setColor(0xCC888888);
        infoPaint.setTextSize(12);
        infoPaint.setAntiAlias(true);
        //将默认值转换px
        padding = dip2px(getContext(), padding);
        boxSide = dip2px(getContext(), boxSide);

        metrics = textPaint.getFontMetrics();
    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        column = 0;
        canvas.save();
        drawBox(canvas);
        //drawTag(canvas);
        drawPopupInfo(canvas);
        canvas.restore();
    }

    /**
     * 画出1-12月方格小块和上面的月份
     * @param canvas 画布
     */
    private void drawBox(Canvas canvas) {

        // /方格的左上右下坐标
        float startX, startY, endX, endY;
        //起始月份为1月

        canvas.drawText(hours[0], padding, padding - boxSide / 2, textPaint);
        for(int grid = 0; grid < mHours.size(); grid++){
            Hour hour = mHours.get(grid);
            int i = hour.minute;
            int j = hour.hour;
            if(j >= 8 && j <= 13){
                j -= 8;
                startX = padding + i * (boxSide + boxInterval);
                startY = padding + j * (boxSide + boxInterval);
                endX = startX + boxSide;
                endY = startY + boxSide;
            }
            else{
                j -= 14;
                startX = padding + (i + 6) * (boxSide + boxInterval) + 5 * boxInterval;
                startY = padding + j * (boxSide + boxInterval);
                endX = startX + boxSide;
                endY = startY + boxSide;
            }
            hour.startX = startX;
            hour.startY = startY;
            hour.endX = endX;
            hour.endY = endY;
            //给画笔设置当前天的颜色
            boxPaint.setColor(hour.colour);
            canvas.drawRect(startX,startY, endX, endY,boxPaint);
        }

        canvas.drawText(hours[1], padding + 6 * (boxSide + boxInterval) + 2 * boxInterval, padding - boxSide / 2, textPaint);
        boxPaint.setColor(DEFAULT_BOX_COLOUR);//恢复默认颜色
    }


    /**
     * 画出右下角的颜色深浅标志，因为是右对齐的所以需要从右往左画
     * @param canvas 画布
     */
    private void drawTag(Canvas canvas) {
        //首先计算出两个文本的长度
        float txtLength = textPaint.measureText("筛选");
        float txtX = padding;
        float txtY = padding + 7 * (boxSide + boxInterval) + Math.abs(metrics.ascent);
        canvas.drawText("筛选 (后面用radiocheck)", txtX, txtY, textPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取点击时候的坐标，用来判断点在哪天，并弹出·
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            downX = event.getX();
            downY = event.getY();
            findClickBox();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否点击在方格内
     */
    private void findClickBox() {
        for (Hour hour : mHours) {
            //检测点击的坐标如果在方格内，则弹出信息提示
            if (downX >= hour.startX && downX <= hour.endX && downY >= hour.startY && downY <= hour.endY) {
                clickDay = hour;//纪录点击的哪天
                break;
            }
        }
        //点击完要刷新，这样每次点击不同的方格，弹窗就可以在相应的位置显示
        //refreshView();
    }

    /**
     * 点击弹出文字提示
     */
    /*private void refreshView() {
        invalidate();
    }*/

    /**
     * 画方格上的文字弹框
     * @param canvas 画布
     */
    private void drawPopupInfo(Canvas canvas) {
        if (clickDay != null) {
            //先根据方格来画出一个小三角形，坐标就是方格的中间
            Path infoPath = new Path();
            //先从方格中心
            infoPath.moveTo(clickDay.startX + boxSide / 2, clickDay.startY + boxSide / 2);
            //然后是方格的左上点
            infoPath.lineTo(clickDay.startX, clickDay.startY);
            //然后是方格的右上点
            infoPath.lineTo(clickDay.endX, clickDay.startY);
            //画出三角
            canvas.drawPath(infoPath,infoPaint);
            //画三角上的圆角矩形
            textPaint.setColor(Color.WHITE);
            //得到当天的文本信息
            String popupInfo = clickDay.toString();
            System.out.println(popupInfo);
            //计算文本的高度和长度用以确定矩形的大小
            float infoHeight = metrics.descent - metrics.ascent;
            float infoLength = textPaint.measureText(popupInfo);
            Log.e("height",infoHeight+"");
            Log.e("length",infoLength+"");
            //矩形左上点应该是x=当前天的x+边长/2-（文本长度/2+文本和框的间隙）
            float leftX = (clickDay.startX + boxSide / 2 ) - (infoLength / 2 + boxSide);
            //矩形左上点应该是y=当前天的y+边长/2-（文本高度+上下文本和框的间隙）
            float topY = clickDay.startY-(infoHeight+2*boxSide);
            //矩形的右下点应该是x=leftX+文本长度+文字两边和矩形的间距
            float rightX = leftX+infoLength+2*boxSide;
            //矩形的右下点应该是y=当前天的y
            float bottomY = clickDay.startY;
            System.out.println(""+leftX+"/"+topY+"/"+rightX+"/"+bottomY);
            RectF rectF = new RectF(leftX, topY, rightX, bottomY);
            canvas.drawRoundRect(rectF,4,4,infoPaint);
            //绘制文字,x=leftX+文字和矩形间距,y=topY+文字和矩形上面间距+文字顶到基线高度
            canvas.drawText(popupInfo,leftX+boxSide,topY+boxSide+Math.abs(metrics.ascent),textPaint);
            clickDay = null;//重新置空，保证点击方格外信息消失
            textPaint.setColor(Color.GRAY);//恢复画笔颜色
        }
    }

    /**
     * 设置某天的次数
    1iscry, 2heart, 3accelerate
     */
    public void setData(int hour,int minute, int type){
        //先找到是第几天，为了方便不做参数检测了
        for (Hour d : mHours) {
            if (d.hour == hour && d.minute == minute){
                if(type == -1){
                    d.contribution++;
                    d.colour = getColour(d.contribution);
                }
                else if(d.normal[type] == false){
                    d.contribution++;
                    d.colour = getColour(d.contribution);
                    d.normal[type] = true;
                    Log.d("SoundFragment", hour + ":"+minute + "contribuion"+d.contribution);

                }

                break;
            }
        }
        //refreshView();
    }

    /**
     * 根据提交次数来获取颜色值
     * @param contribution 提交的次数
     * @return 颜色值
     */
    private int getColour(int contribution){
        int colour = 0;
        //contribution = Math.min(contribution + 1, 4);
        colour = COLOUR_LEVEL[contribution];
        return colour;
    }

}
