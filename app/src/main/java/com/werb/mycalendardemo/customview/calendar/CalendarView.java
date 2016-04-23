package com.werb.mycalendardemo.customview.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.customview.calendar.weekslist.WeekListView;
import com.werb.mycalendardemo.customview.calendar.weekslist.WeeksAdapter;
import com.werb.mycalendardemo.models.CalendarEvent;
import com.werb.mycalendardemo.models.DayItem;
import com.werb.mycalendardemo.models.WeekItem;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.DateHelper;
import com.werb.mycalendardemo.utils.Events;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * The calendar view is a freely(自由的) scrolling(上下滑动) view that allows the user to browse(浏览) between days of the
 * year.
 * Calendar View 是一个允许用户在 day 和 year 之间自由浏览的上下滑动的view
 */
public class CalendarView extends LinearLayout {

    private static final String LOG_TAG = CalendarView.class.getSimpleName();

    /**
     * Top of the calendar view layout, the week days list
     * calendar view 的顶部布局，显示星期名字
     */
    private LinearLayout mDayNamesHeader;
    /**
     * Part of the calendar view layout always visible, the weeks list
     * 总是可见的 calendar view 布局 ，显示星期列表
     */
    private WeekListView mListViewWeeks;
    /**
     * The adapter for the weeks list
     * weeks list 的数据适配器
     */
    private WeeksAdapter mWeeksAdapter;
    /**
     * The current highlighted day in blue
     * 当前被选中且高亮蓝色显示的日期
     *
     */
    private DayItem mSelectedDay;
    /**
     * The current row displayed at top of the list
     * 在列表顶部显示的当前行的position
     */
    private int mCurrentListPosition;

    // region Constructors

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //引入布局文件
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this, true);

        //垂直线性布局
        setOrientation(VERTICAL);
    }

    // endregion

    /**
     * 得到当前被选择的日期
     * @return
     */
    public DayItem getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(DayItem mSelectedDay) {
        this.mSelectedDay = mSelectedDay;
    }

    public WeekListView getListViewWeeks() {
        return mListViewWeeks;
    }

    // region Class - View


    //当View中所有的子控件均被映射成xml后触发,及完成inflater之后
    //当View和它的所有子对象从XML中导入之后，调用此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDayNamesHeader = (LinearLayout) findViewById(R.id.cal_day_names);
        mListViewWeeks = (WeekListView) findViewById(R.id.list_week);
        mListViewWeeks.setLayoutManager(new LinearLayoutManager(getContext()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mListViewWeeks.setHasFixedSize(true);
        //设置recyclerView的子动画
        mListViewWeeks.setItemAnimator(null);
        //设置是否能折叠recyclerView
        mListViewWeeks.setSnapEnabled(true);

        // display only two visible rows on the calendar view
        // 只显示两行Calendar view ,getViewTreeObserver()视图观察者，用于观察整个试图的变化
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (getWidth() != 0 && getHeight() != 0) {
                            collapseCalendarView();
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                }
        );
    }

    //当View附加到一个窗体上时调用
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        BusProvider.getInstance().toObserverable()
                .subscribe(event -> {
                    if (event instanceof Events.CalendarScrolledEvent) {// CalendarView 的滑动事件
                        expandCalendarView();
                    } else if (event instanceof Events.AgendaListViewTouchedEvent) {//日程list的触摸事件，只要一触摸就变为两行的高度
                        collapseCalendarView();
                    } else if (event instanceof Events.DayClickedEvent) {//日历 Day 的点击事件，点击改变高亮表示的Day
                        Events.DayClickedEvent clickedEvent = (Events.DayClickedEvent) event;
                        updateSelectedDay(clickedEvent.getCalendar(), clickedEvent.getDay());
                    }
                });
    }

    // endregion

    // region Public methods

    /**
     * 初始化 Calendar View WeekItem数据与view绑定
     * @param calendarManager 管理日历日程的类
     * @param dayTextColor   day的文字颜色
     * @param currentDayTextColor 当前day的颜色
     * @param pastDayTextColor 过去day的颜色
     */
    public void init(CalendarManager calendarManager, int dayTextColor, int currentDayTextColor, int pastDayTextColor) {
        Calendar today = calendarManager.getToday();
        Locale locale = calendarManager.getLocale();
        SimpleDateFormat weekDayFormatter = calendarManager.getWeekdayFormatter();
        List<WeekItem> weeks = calendarManager.getWeeks();

        //初始化最顶部的周日，周一...周六
        setUpHeader(today, weekDayFormatter, locale);
        //设置WeekListView的数据适配器
        setUpAdapter(today, weeks, dayTextColor, currentDayTextColor, pastDayTextColor);
        //布局设置到当前目标日期
        scrollToDate(today, weeks);
    }

    /**
     * Fired when the Agenda list view changes section.
     * 当滑动 agendaListView 日程时,更新CalendarView中，滚到当前日期
     *
     * @param calendarEvent The event for the selected position in the agenda listview.
     */
    public void scrollToDate(final CalendarEvent calendarEvent) {
        mListViewWeeks.post(()->scrollToPosition(updateSelectedDay(calendarEvent.getInstanceDay(), calendarEvent.getDayReference())));
    }

    /**
     * 滚动到当前日期
     * @param today 目标日期
     * @param weeks WeekItem集合
     */
    public void scrollToDate(Calendar today, List<WeekItem> weeks) {
        Integer currentWeekIndex = null;
        Calendar scrollToCal = today;

        for (int c = 0; c < weeks.size(); c++) {
            if (DateHelper.sameWeek(scrollToCal, weeks.get(c))) {
                currentWeekIndex = c;
                break;
            }
        }

        if (currentWeekIndex != null) {
            final Integer finalCurrentWeekIndex = currentWeekIndex;
            mListViewWeeks.post(() -> scrollToPosition(finalCurrentWeekIndex));
        }
    }


    // endregion

    // region Private methods

    /**
     * 布局跳转到目标位置(一般用于初次加载时，显示到当前日期)
     * @param targetPosition 需要跳转的目标位置
     */
    private void scrollToPosition(int targetPosition) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) mListViewWeeks.getLayoutManager());
        layoutManager.scrollToPosition(targetPosition);
    }

    /**
     * 通过改变参数 position 来实现 WeeksAdapter 的局部数据刷新，即改变被选择的日期
     * @param position
     */
    private void updateItemAtPosition(int position) {
        WeeksAdapter weeksAdapter = (WeeksAdapter) mListViewWeeks.getAdapter();
        weeksAdapter.notifyItemChanged(position);
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters.
     */
    private void setUpAdapter(Calendar today, List<WeekItem> weeks, int dayTextColor, int currentDayTextColor, int pastDayTextColor) {
        if (mWeeksAdapter == null) {
            Log.d(LOG_TAG, "Setting adapter with today's calendar: " + today.toString());
            System.out.println("Setting adapter with today's calendar: "+today.toString());
            //
            mWeeksAdapter = new WeeksAdapter(getContext(), today, dayTextColor, currentDayTextColor, pastDayTextColor);
            mListViewWeeks.setAdapter(mWeeksAdapter);
        }
        mWeeksAdapter.updateWeeksItems(weeks);
    }

    /**
     * 设置日历头 即最上边的文字
     * @param today
     * @param weekDayFormatter
     * @param locale
     */
    private void setUpHeader(Calendar today, SimpleDateFormat weekDayFormatter, Locale locale) {
        //每周7天
        int daysPerWeek = 7;
        String[] dayLabels = new String[daysPerWeek];
        Calendar cal = Calendar.getInstance(CalendarManager.getInstance(getContext()).getLocale());
        cal.setTime(today.getTime());
        //每周的第一天
        int firstDayOfWeek = cal.getFirstDayOfWeek();
        for (int count = 0; count < 7; count++) {
            //因为默认getFirstDayOfWeek()得到的返回值为1，默认为周日，可以通过setFirstDayOfWeek()自定义设置
            cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + count);
            //根据语言地区变化中文或其他语言
            if (locale.getLanguage().equals("en")) {
                //getTime()返回的是Date类型的
                dayLabels[count] = weekDayFormatter.format(cal.getTime()).toUpperCase(locale);
            } else {
                dayLabels[count] = weekDayFormatter.format(cal.getTime());
            }
        }

        //Layout 中的 5 个布局
        for (int i = 0; i < mDayNamesHeader.getChildCount(); i++) {
            TextView txtDay = (TextView) mDayNamesHeader.getChildAt(i);
            txtDay.setText(dayLabels[i]);
            System.out.println("按顺序显示:"+dayLabels[i]);
        }
    }

    /**
     * 扩展布局的高度，改为5行的高度
     */
    private void expandCalendarView() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        int height = (int) (getResources().getDimension(R.dimen.calendar_header_height) + 5 * getResources().getDimension(R.dimen.day_cell_height));
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * 折叠CalendarView，设置为其只显示两行的高度
     */
    private void collapseCalendarView() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        int height = (int) (getResources().getDimension(R.dimen.calendar_header_height) + 2 * getResources().getDimension(R.dimen.day_cell_height));
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * day 点击事件，更新点中的day
     * Update a selected cell day item.
     *
     * @param calendar The Calendar instance of the day selected.
     * @param dayItem  The DayItem information held by the cell item.
     * @return The selected row of the weeks list, to be updated.
     */
    private int updateSelectedDay(Calendar calendar, DayItem dayItem) {
        Integer currentWeekIndex = null;
        Calendar scrollToCal = calendar;

        // update highlighted/selected day
        if (!dayItem.equals(getSelectedDay())) {
            dayItem.setSelected(true);
            if (getSelectedDay() != null) {
                getSelectedDay().setSelected(false);
            }
            setSelectedDay(dayItem);
        }

        //循环 得出被选中的日期在哪个星期中
        for (int c = 0; c < CalendarManager.getInstance().getWeeks().size(); c++) {
            if (DateHelper.sameWeek(scrollToCal, CalendarManager.getInstance().getWeeks().get(c))) {
                currentWeekIndex = c;

                break;
            }
        }

        System.out.println("currentWeekIndex="+currentWeekIndex+";mCurrentListPosition="+mCurrentListPosition);

        if (currentWeekIndex != null) {
            // highlighted day has changed, update the rows concerned
            // 被选择的日期已经改变，更新相关的行，根据被选择的日期，改变在最顶部停留的星期
            if (currentWeekIndex != mCurrentListPosition) {
                updateItemAtPosition(mCurrentListPosition);
            }
            mCurrentListPosition = currentWeekIndex;
            updateItemAtPosition(currentWeekIndex);
        }

        return mCurrentListPosition;
    }

}
