package com.werb.mycalendardemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.werb.mycalendardemo.alarmremind.AlarmAlertBroadcastReceiver;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by acer-pc on 2016/4/17.
 */
public class AlarmBean implements Serializable {

    private int id;
    private String title;
    private int isAllday;//是否是全天
    private int isVibrate;//是否震动
    private int year;
    private int month;
    private int day;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private String alarmTime;
    private String alarmColor;
    private String alarmTonePath;
    private String local;
    private String description;
    private String replay;

    public AlarmBean() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(int isAllday) {
        this.isAllday = isAllday;
    }

    public int getIsVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(int isVibrate) {
        this.isVibrate = isVibrate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmColor() {
        return alarmColor;
    }

    public void setAlarmColor(String alarmColor) {
        this.alarmColor = alarmColor;
    }

    public String getAlarmTonePath() {
        return alarmTonePath;
    }

    public void setAlarmTonePath(String alarmTonePath) {
        this.alarmTonePath = alarmTonePath;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public Calendar getRealAlarmTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(), getMonth(), getDay());
        calendar.set(Calendar.HOUR_OF_DAY, getStartTimeHour());
        calendar.set(Calendar.MINUTE, getStartTimeMinute());
        calendar.set(Calendar.SECOND, 0);
        switch (getAlarmTime()) {
            case "无":
                break;
            case "提前十分钟提醒":
                calendar.add(Calendar.MINUTE, -10);
                break;
            case "提前一个小时通知":
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                break;
            case "提前一天提醒":
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
        }
        return calendar;
    }

    private long getRaplayTime(){
        switch (getReplay()){
            case "不重复":
                return 0;
            case "每天":
                return 86400000;
            case "每周":
                return 604800000;
            case "每年":
                return 31536000000L;
        }
        return 0;
    }

    public void schedule(Context context) {
        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        myIntent.putExtra("alarm", this);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getRealAlarmTime().getTimeInMillis(), pendingIntent);
        //设置带有重复的提醒
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getRealAlarmTime().getTimeInMillis(),getRaplayTime(),pendingIntent);
    }

    public Calendar getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isAllday=" + isAllday +
                ", isVibrate=" + isVibrate +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", startTimeHour=" + startTimeHour +
                ", startTimeMinute=" + startTimeMinute +
                ", endTimeHour=" + endTimeHour +
                ", endTimeMinute=" + endTimeMinute +
                ", alarmTime='" + alarmTime + '\'' +
                ", alarmColor='" + alarmColor + '\'' +
                ", alarmTonePath='" + alarmTonePath + '\'' +
                ", local='" + local + '\'' +
                ", description='" + description + '\'' +
                ", replay='" + replay + '\'' +
                '}';
    }
}
