package com.werb.mycalendardemo.models;

import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.DateHelper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by acer-pc on 2016/4/9.
 */
public class DayItem implements Serializable {

    private Date mDate; //日期
    private int mValue; //相当于Day_OF_Month,一月中的第几天
    private int mDayOfTheWeek; //一个星期中的第几天
    private boolean mToday; //是否是今天
    private boolean mFirstDayOfTheMonth; //是否是当前月的第一天
    private boolean mSelected;//是否选中
    private String mMonth; // 月？不确定

    // region Constructor

    public DayItem(Date date, int value, boolean today, String month) {
        this.mDate = date;
        this.mValue = value;
        this.mToday = today;
        this.mMonth = month;
    }

    // endregion

    // region Getters/Setters

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }

    public boolean isToday() {
        return mToday;
    }

    public void setToday(boolean today) {
        this.mToday = today;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }

    public boolean isFirstDayOfTheMonth() {
        return mFirstDayOfTheMonth;
    }

    public void setFirstDayOfTheMonth(boolean firstDayOfTheMonth) {
        this.mFirstDayOfTheMonth = firstDayOfTheMonth;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String month) {
        this.mMonth = month;
    }

    public int getDayOftheWeek() {
        return mDayOfTheWeek;
    }

    public void setDayOftheWeek(int mDayOftheWeek) {
        this.mDayOfTheWeek = mDayOftheWeek;
    }

    // region Public methods

    /**
     * 将Calendar转化为DayItem对象
     * @param calendar
     * @return
     */
    public static DayItem buildDayItemFromCal(Calendar calendar) {
        Date date = calendar.getTime();
        boolean isToday = DateHelper.sameDate(calendar, CalendarManager.getInstance().getToday());
        int value = calendar.get(Calendar.DAY_OF_MONTH);
        DayItem dayItem = new DayItem(date, value, isToday, CalendarManager.getInstance().getMonthHalfNameFormat().format(date));
        if (value == 1) {
            dayItem.setFirstDayOfTheMonth(true);
        }
        dayItem.setToday(isToday);
        return dayItem;
    }

    // endregion

    @Override
    public String toString() {
        return "DayItem{"
                + "Date='"
                + mDate.toString()
                + ", value="
                + mValue
                + '}';
    }
}
