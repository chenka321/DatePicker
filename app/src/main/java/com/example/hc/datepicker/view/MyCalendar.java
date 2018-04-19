package com.example.hc.datepicker.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import com.example.hc.datepicker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by hc on 2016/11/4. 自定义日历View，可多选
 */

public class MyCalendar extends View {

    // 列的数量
    private static final int NUM_COLUMNS = 7;
    // 行的数量
    private static final int NUM_ROWS = 6;
    /**
     * 可选日期数据
     */
    private List<String> mOptionalDates = new ArrayList<>();

    /**
     * 以选日期数据
     */
    private List<String> mSelectedDates = new ArrayList<>();

    // 天数默认颜色
    private int mDayNormalColor = Color.parseColor("#3A3E39");
    // 天数不可选颜色
//    private int mDayNotOptColor = Color.parseColor("#9B9B9C");
    //电费度数颜色
    private int liteColor = Color.parseColor("#9B9B9C");
    // 是否可以被点击状态
    private boolean mClickable = true;

    private DisplayMetrics mMetrics;
    private Paint mPaint;

    private int mSelYear;
    private int mSelMonth;
    private int mSelDate;
    private int mColumnSize;
    private int mRowSize;
    private int[][] mDays;

    private ArrayList<String> mDatas = new ArrayList<>();//两个日期间的选中的日期;
    //    private List<DateDate> datedes = new ArrayList<>();//日期下面的详情;
    private int firstYear = 0;
    private int firstMonth = 0;
    private int firstDay = 0;
    private int twoYear = 0;
    private int twoMonth = 0;
    private int twoDay = 0;
    // 已选中背景Bitmap
    private Bitmap mBgOptBitmap;
    // 未选中背景Bitmap
    private Bitmap mBgNotOptBitmap;

    public MyCalendar(Context context) {
        super(context);
        init();
    }

    public MyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 获取手机屏幕参数
        mMetrics = getResources().getDisplayMetrics();
        // 创建画笔
        mPaint = new Paint();
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int mCurYear = calendar.get(Calendar.YEAR);
        int mCurMonth = calendar.get(Calendar.MONTH);
        int mCurDate = calendar.get(Calendar.DATE);
        setSelYTD(mCurYear, mCurMonth, mCurDate);

        // 获取背景Bitmap  是大图片;
//        mBgOptBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ending);
//        mBgNotOptBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ending);

        //获取压缩图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ending);
        mBgOptBitmap = createScaledBitmap(bitmap, bitmap.getWidth() * 4 / 5, bitmap.getHeight() * 4 / 5, true);
        mBgNotOptBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 4 / 5, bitmap.getHeight() * 4 / 5, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();

        // 绘制背景
        int mBgColor = Color.WHITE;
        mPaint.setColor(mBgColor);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);

        mDays = new int[6][7];
        // 设置绘制字体大小
        int mDayTextSize = 14;
        mPaint.setTextSize(mDayTextSize * mMetrics.scaledDensity);
        // 设置绘制字体颜色

        String dayStr;
        // 获取当月一共有多少天
        int mMonthDays = getMonthDays(mSelYear, mSelMonth);
        // 获取当月第一天位于周几
        int mWeekNumber = getFirstDayWeek(mSelYear, mSelMonth);

        for (int day = 0; day < mMonthDays; day++) {
            dayStr = String.valueOf(day + 1);
            int column = (day + mWeekNumber - 1) % 7;
            int row = (day + mWeekNumber - 1) / 7;
            mDays[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayStr)) / 2);
            int startY = (int) ((mRowSize) * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            // 判断当前天数是否可选
            if (mOptionalDates.contains(getSelData(mSelYear, mSelMonth, mDays[row][column]))) {
                // 没有点击过，绘制默认背景
                canvas.drawBitmap(mBgNotOptBitmap, startX - (mBgNotOptBitmap.getWidth() / 2 - mPaint.measureText(dayStr) / 2), startY - (mBgNotOptBitmap.getHeight() / 2 - ((mPaint.ascent() + mPaint.descent()) / 2)), mPaint);
                int mDayPressedColor = Color.WHITE;
                mPaint.setColor(mDayPressedColor);
                //绘制度数;
                if (datadata != null && datadata.size() > 0) {
                    String s = datadata.get(getSelData(mSelYear, mSelMonth, mDays[row][column]));
                    if (s != null && !s.equals("")) {
                        mPaint.setColor(liteColor);
                        mPaint.setTextSize(12 * mMetrics.scaledDensity);
                        canvas.drawText(s, startX - s.length() * 6, startY + 70, mPaint);
                    }
                }
                // 绘制选中天数的颜色;
                mPaint.setColor(mDayPressedColor);
                //绘制选中天数的大小
                mPaint.setTextSize(mDayTextSize * mMetrics.scaledDensity);
                //绘制选中天数的数字;
                canvas.drawText(dayStr, startX, startY, mPaint);
            } else {//绘制未选中的字体和颜色;liteColor
                mPaint.setColor(mDayNormalColor);
                mPaint.setTextSize(mDayTextSize * mMetrics.scaledDensity);
                canvas.drawText(dayStr, startX, startY, mPaint);
            }
        }
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!mClickable) return true;

                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {
                    performClick();
                    onClick((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        return true;
    }

    /**
     * 点击事件
     */
    private void onClick(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelYTD(mSelYear, mSelMonth, mDays[row][column]);

        // 判断是否点击过
        boolean isSelected = mSelectedDates.contains(getSelData(mSelYear, mSelMonth, mSelDate));
        // 判断是否可以添加
        boolean isCanAdd = mOptionalDates.contains(getSelData(mSelYear, mSelMonth, mSelDate));
        if (isSelected) {
            mSelectedDates.remove(getSelData(mSelYear, mSelMonth, mSelDate));
        } else if (isCanAdd) {
            mSelectedDates.add(getSelData(mSelYear, mSelMonth, mSelDate));
        }

        invalidate();
        if (mListener != null) {
            // 执行回调
            mListener.onClickDateListener(mSelYear, (mSelMonth + 1), mSelDate);
        }
        if (checkMoreClickListener != null) {
            //执行日期多选;
            checkMore();
        }
    }

    HashMap<String, String> datadata = new HashMap<>();

    public void setDataData(HashMap<String, String> dates) {
        this.datadata = dates;
    }

    private void checkMore() {//多选操作;
        if (twoYear != 0 && twoMonth != 0 && twoDay != 0) {
            twoDay = 0;
            twoMonth = 0;
            twoYear = 0;
            firstMonth = 0;
            firstDay = 0;
            firstYear = 0;
            mDatas.clear();
            datadata.clear();
            this.setOptionalDate(mDatas);
            return;
        }

        if (firstYear == 0 && firstMonth == 0 && firstDay == 0) {//第一次选择的日期;
            firstYear = mSelYear;
            firstMonth = (mSelMonth + 1);
            firstDay = mSelDate;
            mDatas.add(mSelYear + String.format(Locale.getDefault(), "%02d", (mSelMonth + 1)) + String.format(Locale.getDefault(), "%02d", mSelDate));
            //开始时间的回调;
            checkMoreClickListener.onCheckStartDateListener(firstYear, firstMonth, firstDay);
            this.setOptionalDate(mDatas);
        } else {
            twoYear = mSelYear;
            twoMonth = (mSelMonth + 1);
            twoDay = mSelDate;
            if (compareToDate(firstYear, firstMonth, firstDay, twoYear, twoMonth, twoDay) == 1) {
                initYMD(firstYear, firstMonth, firstDay, twoYear, twoMonth, twoDay);
            } else {
                initYMD(twoYear, twoMonth, twoDay, firstYear, firstMonth, firstDay);
            }
            //结束时间的回调;
            checkMoreClickListener.onCheckEndDateListener(twoYear, twoMonth, twoDay, 0, repetition(mDatas));
        }
    }

    /**
     * list去除重复,
     *
     * @param list 源数据
     */
    public ArrayList<String> removeDuplicate(ArrayList<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 初始化列宽和高
     */
    private void initSize() {
        // 初始化每列的大小
        mColumnSize = getWidth() / NUM_COLUMNS;
        // 初始化每行的大小
        mRowSize = getHeight() / NUM_ROWS - 10;//偏移量,局部调整每行高度;
    }

    /**
     * 设置可选择日期
     *
     * @param dates 日期数据
     */
    public void setOptionalDate(List<String> dates) {
        this.mOptionalDates = dates;
        invalidate();
    }

    /**
     * 设置已选日期数据
     */
    public void setSelectedDates(List<String> dates) {
        this.mSelectedDates = dates;
    }

    /**
     * 获取已选日期数据
     */
    public List<String> getSelectedDates() {
        return mSelectedDates;
    }

    /**
     * 设置日历是否可以点击
     */
    @Override
    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    /**
     * 设置年月日
     *
     * @param year  年
     * @param month 月
     * @param date  日
     */
    private void setSelYTD(int year, int month, int date) {
        this.mSelYear = year;
        this.mSelMonth = month;
        if (date == 0) {
            this.mSelDate = 1;
        } else {
            this.mSelDate = date;
        }
    }

    /**
     * 设置上一个月日历
     */
    public void setLastMonth() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDate;
        // 如果是1月份，则变成12月份
        if (month == 0) {
            year = mSelYear - 1;
            month = 11;
        } else if (getMonthDays(year, month) == day) {
            //　如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelYTD(year, month, day);
        invalidate();
    }

    /**
     * 设置下一个日历
     */
    public void setNextMonth() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDate;
        // 如果是12月份，则变成1月份
        if (month == 11) {
            year = mSelYear + 1;
            month = 0;
        } else if (getMonthDays(year, month) == day) {
            //　如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelYTD(year, month, day);
        invalidate();
    }

    /**
     * 获取当前展示的年和月份
     *
     * @return 格式：2016-06
     */
    public String getDate() {
        String data;
        if ((mSelMonth + 1) < 10) {
            data = mSelYear + "-0" + (mSelMonth + 1);
        } else {
            data = mSelYear + "-" + (mSelMonth + 1);
        }
        return data;
    }

    /**
     * 获取当前展示的月份
     *
     * @return 格式：6
     */
    public String getSelMonth() {
        return String.valueOf(mSelMonth + 1);
    }

    /**
     * 获取当前展示的年份
     *
     * @return 格式：2016
     */
    public String getSelYear() {
        return String.valueOf(mSelYear);
    }

    /**
     * 获取当前展示的日期
     *
     * @return 格式：20160606
     */
    public String getSelData(int year, int month, int date) {
        String monty, day;
        month = (month + 1);

        // 判断月份是否有非0情况
        if ((month) < 10) {
            monty = "0" + month;
        } else {
            monty = String.valueOf(month);
        }

        // 判断天数是否有非0情况
        if ((date) < 10) {
            day = "0" + (date);
        } else {
            day = String.valueOf(date);
        }
        return year + monty + day;
    }

    private OnClickListener mListener;
    private OnCheckMoreClickListener checkMoreClickListener;

    public interface OnClickListener {
        void onClickDateListener(int year, int month, int day);
    }

    public interface OnCheckMoreClickListener {
        void onCheckStartDateListener(int startYear, int startMonth, int startDay);

        void onCheckEndDateListener(int endYear, int endMonth, int endDay, int Days, ArrayList<String> DaysList);
    }

    public ArrayList<String> repetition(List<String> DaysList) {
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.addAll(DaysList);

        ArrayList<String> arrayList = new ArrayList<>(treeSet);
        treeSet.clear();
        return arrayList;

    }

    /**
     * 设置点击回调
     */
    public void setOnClickDate(OnClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置多选日期的回调
     */
    public void setOnCheckMoreDateClick(OnCheckMoreClickListener checkMoreClickListener) {
        this.checkMoreClickListener = checkMoreClickListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recyclerBitmap(mBgOptBitmap);
        recyclerBitmap(mBgNotOptBitmap);
    }

    /**
     * 释放Bitmap资源
     */
    private void recyclerBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }


    /**
     * 通过年份和月份 得到当月的日子
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /*
    * 比较第一次的年月日与第二次的年月日的大小;日期比较;
    * */
    public int compareToDate(int firstYear, int firstMonth, int firstDay, int twoYear, int twoMonth, int twoDay) {//比较日期大小;
        String DATE1 = firstYear + "-" + firstMonth + "-" + firstDay;
        String DATE2 = twoYear + "-" + twoMonth + "-" + twoDay;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {//date1大于date2  two小于first;
                return -1;
            } else if (dt1.getTime() < dt2.getTime()) {//date1小于date2   two大于First;
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /*
    * 日历多选  --选中两个日期间的日期;
    * */
    private void initYMD(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {//遍历两个日期中的日期
        Calendar start = Calendar.getInstance();
        start.set(startYear, startMonth - 1, startDay);
        Long startTIme = start.getTimeInMillis();

        Calendar end = Calendar.getInstance();
        end.set(endYear, endMonth - 1, endDay);
        Long endTime = end.getTimeInMillis();

        Long oneDay = 1000 * 60 * 60 * 24L;

        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            mDatas.add(df.format(d));
            time += oneDay;
        }
    }
//class DateDate implements Serializable{
//    String date;
//    String dateDes;
//
//    public DateDate(){}
//    public String getDateDes() {
//        return dateDes;
//    }
//
//    public void setDateDes(String dateDes) {
//        this.dateDes = dateDes;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//}
}
