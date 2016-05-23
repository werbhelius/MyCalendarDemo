package com.werb.mycalendardemo.models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 日历事件接口
 */

public interface CalendarEvent extends Serializable {

    int getId();//event_id

    void setId(int mId);

    Calendar getStartTime();//event 开始时间

    void setStartTime(Calendar mStartTime);

    Calendar getEndTime();//event 结束时间

    void setEndTime(Calendar mEndTime);

    String getTitle();//event 标题

    void setTitle(String mTitle);

    Calendar getInstanceDay();//calendar 实例 (应该是因为Calendar为单例模式吧？)

    void setInstanceDay(Calendar mInstanceDay);

    DayItem getDayReference();

    void setDayReference(DayItem mDayReference);

    WeekItem getWeekReference();

    void setWeekReference(WeekItem mWeekReference);

    CalendarEvent copy();
}
