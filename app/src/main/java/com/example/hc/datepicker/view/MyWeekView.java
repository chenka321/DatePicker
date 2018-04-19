package com.example.hc.datepicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by hc on 2016/11/4
 */

public class MyWeekView extends View {
    private String[] mWeeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    // 字体大小
    private int mWeekTextSize = 14;
    // 字体颜色
    private int mWeekTextColor = Color.parseColor("#3A3E39");
    // 背景颜色
    private int mWeekBgColor = Color.WHITE;

    private DisplayMetrics mMetrics;
    private final Paint mPaint;

    public MyWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取手机屏幕参数
        mMetrics = getResources().getDisplayMetrics();
        // 创建画笔
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == View.MeasureSpec.AT_MOST) {
            heightSize = mMetrics.densityDpi * 30;
        }
        if (widthMode == View.MeasureSpec.AT_MOST) {
            widthSize = mMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 画背景
        mPaint.setColor(mWeekBgColor);
        canvas.drawRect(0, 0, width, height, mPaint);

        // 画星期
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mWeekTextSize * mMetrics.scaledDensity);
        mPaint.setColor(mWeekTextColor);
        int columnWidth = width / 7;
        for (int i = 0; i < mWeeks.length; i++) {
            String text = mWeeks[i];
            int fontWidth = (int) mPaint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            canvas.drawText(text, startX, startY, mPaint);
        }
    }

    /**
     * 设置星期数据（默认值："日","一","二","三","四","五","六"）
     */
    public void setWeeks(String[] weeks) {
        this.mWeeks = weeks;
    }

    /**
     * 设置星期字体大小
     */
    public void setWeekTextSize(int size) {
        this.mWeekTextSize = size;
    }

    /**
     * 设置星期背景颜色
     */
    public void setWeekBgColor(int color) {
        this.mWeekBgColor = color;
    }

    /**
     * 设置星期文字颜色
     */
    public void setWeekTextColor(int color) {
        this.mWeekTextColor = color;
    }
}
