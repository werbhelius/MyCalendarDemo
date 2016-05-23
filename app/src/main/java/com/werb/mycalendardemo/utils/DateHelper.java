package com.werb.mycalendardemo.utils;

import android.content.Context;

import com.werb.mycalendardemo.AlarmBean;
import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.models.WeekItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class containing helper functions for dates
 * 一个有关于日期的帮助类
 */
public class DateHelper {

    // region Public methods

    /**
     * Check if two Calendar instances have the same time (by month, year and day of month)
     * 检查两个Calendar是否相同(包括年，月，日)
     *
     * @param cal          The first Calendar instance.
     * @param selectedDate The second Calendar instance.
     * @return True if both instances have the same time.
     */
    public static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Check if a Date instance and a Calendar instance have the same time (by month, year and day
     * of month)
     * 检查一个Calendar和Date是否相同(包括年，月，日)
     *
     * @param cal          The Calendar instance.
     * @param selectedDate The Date instance.
     * @return True if both have the same time.
     */
    public static boolean sameDate(Calendar cal, Date selectedDate) {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);
        return cal.get(Calendar.MONTH) == selectedCal.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) == selectedCal.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) == selectedCal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Check if a Date instance is between two Calendar instances' dates (inclusively) in time.
     * 检查一个Date是否存在于两个Calendar之间
     *
     * @param selectedDate The date to verify. 需要检查的日期
     * @param startCal     The start time.
     * @param endCal       The end time.
     * @return True if the verified date is between the two specified dates.
     */
    public static boolean isBetweenInclusive(Date selectedDate, Calendar startCal, Calendar endCal) {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);
        // Check if we deal with the same day regarding startCal and endCal
        if (sameDate(selectedCal, startCal)) {
            return true;
        } else {
            return selectedCal.after(startCal) && selectedCal.before(endCal);
        }
    }

    /**
     * Check if Calendar instance's date is in the same week, as the WeekItem instance.
     * 检查一个Calendar实例是否属于这个WeekItem星期实例
     *
     * @param cal  The Calendar instance to verify.
     * @param week The WeekItem instance to compare to.
     * @return True if both instances are in the same week.
     */
    public static boolean sameWeek(Calendar cal, WeekItem week) {
        return (cal.get(Calendar.WEEK_OF_YEAR) == week.getWeekInYear() && cal.get(Calendar.YEAR) == week.getYear());
    }

    /**
     * Convert a millisecond duration to a string format
     * 把一个毫秒表示时间转化为字符串
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "Xd" or either "XhXm".
     */
    public static String getDuration(Context context, long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append(context.getResources().getString(R.string.agenda_event_day_duration));
            return (sb.toString());
        } else {
            if (hours > 0) {
                sb.append(hours);
                sb.append("h");
            }
            if (minutes > 0) {
                sb.append(minutes);
                sb.append("m");
            }
        }

        return (sb.toString());
    }

    /**
     * 返回开始结束时间字符串
     * @param bean
     * @return
     */
    public static String getStartAndEndTime(AlarmBean bean) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, bean.getStartTimeHour());
        startCalendar.set(Calendar.MINUTE, bean.getStartTimeMinute());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, bean.getEndTimeHour());
        endCalendar.set(Calendar.MINUTE, bean.getEndTimeMinute());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String startTime = df.format(startCalendar.getTime());
        String endTime = df.format(endCalendar.getTime());
        String startAndEndTime = startTime + "-" + endTime;
        return startAndEndTime;
    }

    /**
     * 返回日程日期字符串
     */
    public static String getScheduleDate(AlarmBean bean){
        Calendar calendar = Calendar.getInstance();
        calendar.set(bean.getYear(),bean.getMonth(),bean.getDay());
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  EE");
        return df.format(calendar.getTime());
    }

    /**
     * 返回日程开始时间的单个字符串
     */
    public static String getStartTime(AlarmBean bean){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, bean.getStartTimeHour());
        calendar.set(Calendar.MINUTE, bean.getStartTimeMinute());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return "开始时间:  " +df.format(calendar.getTime());
    }

    /**
     * 返回日程结束时间的单个字符串
     */
    public static String getEndTime(AlarmBean bean){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, bean.getEndTimeHour());
        calendar.set(Calendar.MINUTE, bean.getEndTimeMinute());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return "结束时间:  " + df.format(calendar.getTime());
    }


    // endregion
}
