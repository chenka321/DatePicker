package com.example.hc.datepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hc.datepicker.view.MyCalendar;

import java.util.ArrayList;

import static com.example.hc.datepicker.R.id.basic;
import static com.example.hc.datepicker.R.id.select_month;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyCalendar calendarView;
    private TextView select_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.last_month).setOnClickListener(this);
        findViewById(R.id.next_month).setOnClickListener(this);
        select_month = (TextView) findViewById(R.id.select_month);
        setYearMonth();
        initClick();
    }

    private void initClick() {
        calendarView = (MyCalendar) findViewById(R.id.calendarView);
        calendarView.setOnCheckMoreDateClick(new MyCalendar.OnCheckMoreClickListener() {
            @Override
            public void onCheckStartDateListener(int startYear, int startMonth, int startDay) {

            }

            @Override
            public void onCheckEndDateListener(int endYear, int endMonth, int endDay, int Days, ArrayList<String> DaysList) {

            }
        });
    }

    /**
     * 设置年月份
     */
    private void setYearMonth() {
        select_month.setText(calendarView.getSelYear() + "年" + calendarView.getSelMonth() + "月");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_month:
                calendarView.setNextMonth();
                setYearMonth();
                break;
            case R.id.last_month:
                calendarView.setLastMonth();
                setYearMonth();
                break;
        }
    }
}
