package com.anim.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.anim.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhanghao on 2016/12/2.
 */

public class HistogramChart extends View{
    private List<HistogramBean> mBeanList;
    private Context mContext;
    private int rectWidth;
    private float scale; //柱与空白之间的比例
    private int mRectColor;//柱的颜色
    private int mLineColor;
    private int mTextColor;//字体的颜色
    private int mTextSize;
    public HistogramChart(Context context) {
        this(context, null);
    }

    public HistogramChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs){
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramChart);
        rectWidth = typedArray.getDimensionPixelSize(R.styleable.HistogramChart_histogram_rectWidth, dip2px(context,15));
        scale = typedArray.getFloat(R.styleable.HistogramChart_histogram_scale, 1.5f);
        mRectColor = typedArray.getColor(R.styleable.HistogramChart_histogram_rectColor, 0xFFFF4081);
        mLineColor = typedArray.getColor(R.styleable.HistogramChart_histogram_lineColor, 0xFF000000);
        mTextColor = typedArray.getColor(R.styleable.HistogramChart_histogram_textColor, 0xFF000000);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.HistogramChart_histogram_textSize, dip2px(context, 10));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint rectPaint = new Paint();
        rectPaint.setColor(mRectColor);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint linePaint = new Paint();
        linePaint.setColor(mLineColor);
//        Path path = new Path();
        //计算最大值得比例

        if (mBeanList == null || mBeanList.size() < 1) {
            return;
        }

        //线到控件底部的距离（为字留空）
//        int bottom = dip2px(mContext, 40);
        float bottom = textPaint.getTextSize() * 4f;

        //最长的柱到控件顶部的距离（为字留空）
//        int top = dip2px(mContext, 25);
        float top = textPaint.getTextSize() * 2.5f;

        //最大值
        float maxNum = getMaxNum(mBeanList);

        //柱的数量
        int size = mBeanList.size();

        //柱和柱之间的空白是柱宽的1.5倍
        int lineWidth = 0;
        if (size == 1) {
            lineWidth = rectWidth;
        }else if (size > 1){
            lineWidth = (int) (size * rectWidth + (size-1) * scale * rectWidth);
        }

        //画柱底下的线
        canvas.drawLine(width/2-lineWidth/2, height-bottom, width/2+lineWidth/2, height-bottom, linePaint);
//        canvas.drawLine(0, 0, width, 0, rectPaint);

        //画柱
        for (int i = 0; i < size; i++) {
            canvas.save();
            HistogramBean bean = mBeanList.get(i);
            int x = (int) ((width/2-lineWidth/2) + i * (1 + scale) * rectWidth);
            float rectHeight = (bean.getNum()/maxNum) * (height - top - bottom);
            //画柱状图
            Rect rect = new Rect(x, (int) (height-bottom-rectHeight), x+rectWidth, (int) (height-bottom));
            canvas.drawRect(rect, rectPaint);
            //画底部文字
//            canvas.drawText(bean.getTitle(), x-textPaint.getTextSize()/2, (int) (height-bottom-rectHeight) - 6, textPaint);
            StaticLayout bottomL = new StaticLayout(bean.getIntro(), textPaint, (int) (textPaint.getTextSize()*3), Layout.Alignment.ALIGN_NORMAL, 1f, 0, false);
            canvas.translate(x+rectWidth/2, height-bottom + textPaint.getTextSize()/2);
            bottomL.draw(canvas);
            canvas.restore();
//            canvas.drawText(bean.getIntro(), x+rectWidth/2, (height- bottom - textPaint.getTextSize()/2), textPaint);
        }
        for (int i = 0; i < size; i++) {
            //画顶部文字
            canvas.save();
            HistogramBean bean = mBeanList.get(i);
            int x = (int) ((width/2-lineWidth/2) + i * (1 + scale) * rectWidth);
            float rectHeight = (bean.getNum()/maxNum) * (height - top - bottom);
            StaticLayout topL = new StaticLayout(bean.getTitle(), textPaint, (int) (textPaint.getTextSize()*3), Layout.Alignment.ALIGN_NORMAL, 1f, 0, false);
            int lineRow = topL.getLineCount();
            canvas.translate(x+rectWidth/2, (float) (height-bottom-rectHeight-(textPaint.getTextSize()*(lineRow + 0.5))));
            topL.draw(canvas);
            canvas.restore();
        }
    }

    private Float getMaxNum(List<HistogramBean> beanList){
        List<Float> list = new ArrayList();
        for (HistogramBean bean:beanList){
            list.add(bean.getNum());
        }
        return Collections.max(list);
    }

    public void setBeanList(List<HistogramBean> beanList) {
        mBeanList = beanList;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
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

    public void setRectWidth(int rectWidth) {
        this.rectWidth = dip2px(mContext, rectWidth);
    }
}
