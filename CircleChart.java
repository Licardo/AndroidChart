package com.anim.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.anim.R;

/**
 * Created by zhanghao on 2016/12/2.
 */

public class CircleChart extends View {
    private int[] mColors;
    private float[] mNums;
    private CharSequence[] mText;
    private float startAngel;
    private int mTextColor;
    private int mTextSize;

    public CircleChart(Context context) {
        this(context, null);
    }

    public CircleChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleChart);
        startAngel = typedArray.getFloat(R.styleable.CircleChart_circle_startAngel, -25);
        mTextColor = typedArray.getColor(R.styleable.CircleChart_circle_textColor, 0x000000);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleChart_circle_textSize, 30);

        int fcolor = typedArray.getColor(R.styleable.CircleChart_circle_fColor, 0x3F51B5);
        int scolor = typedArray.getColor(R.styleable.CircleChart_circle_sColor, 0xFF4081);
        int tcolor = typedArray.getColor(R.styleable.CircleChart_circle_tColor, 0x43CD80);
        mColors = new int[]{fcolor, scolor, tcolor};
        float fscore = typedArray.getFloat(R.styleable.CircleChart_circle_fScore, 20);
        float sscore = typedArray.getFloat(R.styleable.CircleChart_circle_sScore, 40);
        float tscore = typedArray.getFloat(R.styleable.CircleChart_circle_tScore, 60);
        mNums = new float[]{fscore, sscore, tscore};
        CharSequence ftext = typedArray.getText(R.styleable.CircleChart_circle_fText);
        CharSequence stext = typedArray.getText(R.styleable.CircleChart_circle_sText);
        CharSequence ttext = typedArray.getText(R.styleable.CircleChart_circle_tText);
        mText = new CharSequence[]{ftext, stext, ttext};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        if (mNums == null || mColors == null || mNums.length < 1 || mColors.length < 1) {
            return;
        }
        Paint textPaint = new Paint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float baseTop = metrics.top; //基线到上边的距离
        float baseBottom = metrics.bottom;//基线到底部的距离
        Rect rect = new Rect(0, 0, width, height);
        for (int i = 0; i < mNums.length; i++) {
            float angel = 360 * (mNums[i] / 100);
            float oldangel = 360 * (sum(i) / 100);

            Paint paint = new Paint();
            if (mColors.length > i) {
                paint.setColor(mColors[i]);
            } else {
                paint.setColor(mColors[mColors.length - 1]);
            }
            canvas.drawArc(new RectF(rect), oldangel, angel, true, paint);
        }
        for (int i = 0; i < mNums.length; i++) {
            float angel = 360 * (mNums[i] / 100);
            float oldangel = 360 * (sum(i) / 100);
            //圆弧中心写字
            if (mText.length > i) {
                //计算圆弧中心在x,y方向上的距离
                double calAngel = oldangel + angel/2;
                double yheight = Math.sin(Math.PI*calAngel/180) * (width/4);
                double xheight = Math.cos(Math.PI*calAngel/180) * (width/4);
                double dotX = 0,dotY = 0;//圆弧中心点xy的坐标
                //计算x、y方向相对圆心的正负值
                dotX = width/2 + xheight;
                dotY = height/2 + yheight;
                canvas.drawText((String) mText[i],(float)dotX, ((float) (dotY-baseBottom/2-baseTop/2)), textPaint);
            }
        }
    }

    public void setColors(int... colors) {
        mColors = colors;
    }

    public void setNums(float... nums) {
        mNums = nums;
    }

    public void setText(String... values){
        mText = values;
    }

    private float sum(int index) {
        float sum = startAngel;
        if (index == 0) {
            return sum;
        } else {
            for (int i = 0; i < (index); i++) {
                sum += mNums[i];
            }
            return sum;
        }
    }
}
