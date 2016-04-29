package com.werb.mycalendardemo.utils;

import android.content.Context;
import android.util.Log;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.models.BaseCalendarEvent;
import com.werb.mycalendardemo.models.CalendarEvent;
import com.werb.mycalendardemo.models.DayItem;
import com.werb.mycalendardemo.models.MonthItem;
import com.werb.mycalendardemo.models.WeekItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class manages information about the calendar. (Events, weather info...)
 * Holds reference to the days list of the calendar.
 * As the app is using several views, we want to keep everything in one place.
 * 用于一些日历信息的管理
 */
public class CalendarManager {

    private static final String LOG_TAG = CalendarManager.class.getSimpleName();

    private static CalendarManager mInstance;//单例模型

    private Context mContext;
    private Locale mLocale;
    private Calendar mToday = Calendar.getInstance();

    //SimpleDateFormat 是一个以国别敏感的方式格式化和分析数据的具体类。 它允许格式化 (date -> text)、语法分析 (text -> date)和标准化。
    private SimpleDateFormat mWeekdayFormatter;
    private SimpleDateFormat mMonthHalfNameFormat;

    /**
     * List of days used by the calendar
     * DayItem 集合
     */
    private List<DayItem> mDays = new ArrayList<>();
    /**
     * List of weeks used by the calendar
     * WeekItem 集合
     */
    private List<WeekItem> mWeeks = new ArrayList<>();
    /**
     * List of months used by the calendar
     * MonthItem 集合
     */
    private List<MonthItem> mMonths = new ArrayList<>();
    /**
     * List of events instances
     * CalendarEvent 集合
     */
    private List<CalendarEvent> mEvents = new ArrayList<>();
    /**
     * Helper to build our list of weeks
     * 帮助我们构建 week 列表
     */
    private Calendar mWeekCounter;
    /**
     * The start date given to the calendar view
     * 日历开始的时间
     */
    private Calendar mMinCal;
    /**
     * The end date given to the calendar view
     * 日历结束的时间
     */
    private Calendar mMaxCal;

    // region Constructors

    //构造方法
    public CalendarManager(Context context) {
        this.mContext = context;
    }

    //单子例模型
    public static CalendarManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CalendarManager(context);
        }
        return mInstance;
    }

    public static CalendarManager getInstance() {
        return mInstance;
    }

    // endregion

    // region Getters/Setters

    /**
     * Sets the current mLocale
     * 设置当前的区域
     * @param locale to be set
     */
    public void setLocale(Locale locale) {
        this.mLocale = locale;

        //apply the same locale to all variables depending on that
        //采用相同的区域语言环境
        setToday(Calendar.getInstance(mLocale));
        mWeekdayFormatter = new SimpleDateFormat(getContext().getString(R.string.day_name_format), mLocale);
        mMonthHalfNameFormat = new SimpleDateFormat(getContext().getString(R.string.month_half_name_format), locale);
    }

    public Locale getLocale() {
        return mLocale;
    }

    public Context getContext() {
        return mContext;
    }

    public Calendar getToday() {
        return mToday;
    }

    public void setToday(Calendar today) {
        this.mToday = today;
    }

    public List<WeekItem> getWeeks() {
        return mWeeks;
    }

    public List<MonthItem> getMonths() {
        return mMonths;
    }

    public List<DayItem> getDays() {
        return mDays;
    }

    public List<CalendarEvent> getEvents() {
        return mEvents;
    }

    public SimpleDateFormat getWeekdayFormatter() {
        return mWeekdayFormatter;
    }

    public SimpleDateFormat getMonthHalfNameFormat() {
        return mMonthHalfNameFormat;
    }

    // endregion

    // region Public methods

    /**
     * 构建Calendar
     * @param minDate 日历最小时间
     * @param maxDate 日历最大时间
     * @param locale  区域
     */
    public void buildCal(Calendar minDate, Calendar maxDate, Locale locale) {
        //异常处理
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.");
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null.");
        }
        //构建基本的Calendar
        setLocale(locale);

        getDays().clear();
        getWeeks().clear();
        getMonths().clear();
        getEvents().clear();

        mMinCal = Calendar.getInstance(mLocale);
        mMaxCal = Calendar.getInstance(mLocale);
        mWeekCounter = Calendar.getInstance(mLocale);

        mMinCal.setTime(minDate.getTime());
        mMaxCal.setTime(maxDate.getTime());

        // maxDate is exclusive(不包括的), here we bump back to the previous day, as maxDate if December 1st, 2020,
        // we don't include that month in our list
        // 不包括最大的那天，所以-1
        mMaxCal.add(Calendar.MINUTE, -1);

        // Now iterate(重复、迭代) we iterate between mMinCal and mMaxCal so we build our list of weeks
        mWeekCounter.setTime(mMinCal.getTime());
        int maxMonth = mMaxCal.get(Calendar.MONTH);//日历所存在的最大月
        int maxYear = mMaxCal.get(Calendar.YEAR);//日历所存在的最大年
        // Build another month item and add it to our list, if this value change when we loop through the weeks
        int tmpMonth = 0;
        //setToday(Calendar.getInstance(mLocale));

        // Loop through the weeks
        // 按周循环，构建 monthItem 和 WeekItem
        while ((mWeekCounter.get(Calendar.MONTH) <= maxMonth // Up to, including the month.
                || mWeekCounter.get(Calendar.YEAR) < maxYear) // Up to the year.
                && mWeekCounter.get(Calendar.YEAR) < maxYear + 1) { // But not > next yr.
            Date date = mWeekCounter.getTime();

            if (tmpMonth != mWeekCounter.get(Calendar.MONTH)) {
                //Calendar.MONTH
                MonthItem monthItem = new MonthItem(mWeekCounter.get(Calendar.YEAR), mWeekCounter.get(Calendar.MONTH));
                System.out.println("monthItem_month="+monthItem.getMonth());
                //把 monthItem 放入 List<monthItem> 集合中
                getMonths().add(monthItem);
            }

            // Build our week list
            // 构建 week 列表
            WeekItem weekItem = new WeekItem(mWeekCounter.get(Calendar.WEEK_OF_YEAR), mWeekCounter.get(Calendar.YEAR), date, mMonthHalfNameFormat.format(date), mWeekCounter.get(Calendar.MONTH));
            System.out.println("weekItem_month="+weekItem.getMonth());//9月
            List<DayItem> dayItems = getDayCells(mWeekCounter); // gather(收集) days for the built week
            weekItem.setDayItems(dayItems);
            //把 weekItem 放入 List<weekItem> 集合中
            getWeeks().add(weekItem);
            //把 weekItem 加入 MonthItem
            addWeekToLastMonth(weekItem);

            Log.d(LOG_TAG, String.format("Adding week: %s", weekItem));
            tmpMonth = mWeekCounter.get(Calendar.MONTH);
            //加1周，循环
            mWeekCounter.add(Calendar.WEEK_OF_YEAR, 1);
        }
    }

    /**
     * 按照日期处理 event 添加到集合中，即让日期与event匹配
     * @param eventList 事件集合
     */
    public void loadEvents(List<CalendarEvent> eventList) {
        /*CalendarLoadTask calendarLoadTask = new CalendarLoadTask();
        calendarLoadTask.execute();*/
        System.out.println("---onloadEvents----"+eventList.size());
        for (WeekItem weekItem : getWeeks()) {
            for (DayItem dayItem : weekItem.getDayItems()) {
                boolean isEventForDay = false;
                for (CalendarEvent event : eventList) {
                    //event 是按日期排的，先判断这个event在不在这个日期下
                    //属于这个日期的，把该event添加到list集合中
                    if (DateHelper.isBetweenInclusive(dayItem.getDate(), event.getStartTime(), event.getEndTime())) {
                        System.out.println("---on 有匹配的event");
                        CalendarEvent copy = event.copy();
                        Calendar dayInstance = Calendar.getInstance();
                        dayInstance.setTime(dayItem.getDate());
                        copy.setInstanceDay(dayInstance);
                        copy.setDayReference(dayItem);
                        copy.setWeekReference(weekItem);
                        // add instances in chronological order
                        // 按时间排序event,加入list集合中
                        getEvents().add(copy);
                        //event 是否属于这一天
                        isEventForDay = true;
                    }
                }
                //event 不属于当前日期，添加空事件，即“No Event”
                if (!isEventForDay) {
                    Calendar dayInstance = Calendar.getInstance();
                    dayInstance.setTime(dayItem.getDate());
                    BaseCalendarEvent event = new BaseCalendarEvent(dayInstance, getContext().getResources().getString(R.string.agenda_event_no_events));
                    event.setDayReference(dayItem);
                    event.setWeekReference(weekItem);
                    getEvents().add(event);
                }
            }
        }


        //发送消息，EventsFetched 暂时理解为 事件与日期匹配
        BusProvider.getInstance().send(new Events.EventsFetched());
        Log.d(LOG_TAG, "CalendarEventTask finished");

    }

    // endregion

    // region Private methods

    /**
     * 利用传入的值(向后+7天),构建DayItem
     * @param startCal
     * @return
     */
    private List<DayItem> getDayCells(Calendar startCal) {
        Calendar cal = Calendar.getInstance(mLocale);
        cal.setTime(startCal.getTime());
        List<DayItem> dayItems = new ArrayList<>();

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//传入的这天在这一周中week的位置
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;//这一周的第一天-传入的这天在这一周中week的位置

        System.out.println("cal.getFirstDayOfWeek()="+cal.getFirstDayOfWeek()+",firstDayOfWeek="+firstDayOfWeek);

        if (offset > 0) {
            offset -= 7;
        }

        //补全偏移量
        cal.add(Calendar.DATE, offset);

        Log.d(LOG_TAG, String.format("Buiding row week starting at %s", cal.getTime()));

        //向DayItem中添加数据
        for (int c = 0; c < 7; c++) {
            DayItem dayItem = DayItem.buildDayItemFromCal(cal);
            dayItem.setDayOftheWeek(c);
            dayItems.add(dayItem);
            cal.add(Calendar.DATE, 1);
        }

        //把 dayItem 放入 List<dayItem> 集合中
        getDays().addAll(dayItems);
        return dayItems;
    }


    /**
     * 向MonthItem添加WeekItem
     * @param weekItem
     */
    private void addWeekToLastMonth(WeekItem weekItem) {
        getLastMonth().getWeeks().add(weekItem);
        getLastMonth().setMonth(mWeekCounter.get(Calendar.MONTH) + 1);
    }

    /**
     * 得到最后一个月
     * @return
     */
    private MonthItem getLastMonth() {
        System.out.println("Months().size()=" + getMonths().size());
        return getMonths().get(getMonths().size()-1);
    }

    public List<Calendar> getFirstDayOfMonth(){
        List<Calendar> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(int i=0;i<12;i++){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.MONTH,i);
            list.add(calendar);
        }
        return list;
    }


}
