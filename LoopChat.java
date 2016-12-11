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

public class LoopChat extends View{
    private float score;
    private int borderRadius;
    private Context mContext;
    private int mColor;//圆环的有分数的颜色
    private int mInColor ;//圆环内部的颜色
    private int mOutColor;//圆环外部颜色
    private int mTextColor;//字体颜色
    private int mTextSize;//字体大小
    private CharSequence mText;//圆环中间的文字
    public LoopChat(Context context) {
        this(context, null);
    }

    public LoopChat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopChat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoopChat);
        score = typedArray.getFloat(R.styleable.LoopChat_loop_score, 0);
        borderRadius = typedArray.getDimensionPixelSize(R.styleable.LoopChat_loop_borderRadius, (int) dip2px(context, 30));
        mColor = typedArray.getColor(R.styleable.LoopChat_loop_scoreColor, 0x3c000000);
        mOutColor = typedArray.getColor(R.styleable.LoopChat_loop_outColor, 0x3c000000);
        mInColor = typedArray.getColor(R.styleable.LoopChat_loop_inColor, 0x3c000000);
        mTextColor = typedArray.getColor(R.styleable.LoopChat_loop_textColor, 0x000000);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoopChat_loop_textSize, (int) dip2px(context, 10));
        mText = typedArray.getText(R.styleable.LoopChat_loop_textValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint bigPaint = new Paint();
        bigPaint.setColor(mOutColor);
        Paint smallPaint = new Paint();
        Paint scorePaint = new Paint();
        scorePaint.setColor(mColor);
        smallPaint.setColor(mInColor);
        canvas.drawCircle(width/2, height/2, width/2, bigPaint);
        Rect rect = new Rect(0, 0, width, height);
        canvas.drawArc(new RectF(rect), -90, score/100*360, true, scorePaint);
        canvas.drawCircle(width/2, height/2, width/2 - borderRadius, smallPaint);
        //写圆环中间的文字
        Paint textPaint = new Paint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float baseTop = metrics.top; //基线到上边的距离
        float baseBottom = metrics.bottom;//基线到底部的距离
        int baseY = (int) (rect.centerY() - baseBottom/2 - baseTop/2);//基线中间y的坐标
        canvas.drawText((String) mText, rect.centerX(), baseY, textPaint);
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = (int) dip2px(mContext, borderRadius);
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setText(String text) {
        mText = text;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
