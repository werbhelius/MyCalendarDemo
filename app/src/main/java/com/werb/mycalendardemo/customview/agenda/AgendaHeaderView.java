package com.werb.mycalendardemo.customview.agenda;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.DateHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Header view for the StickyHeaderListView of the agenda view
 * AgendaListView 中用于停靠的顶部 view AgendaHeaderView
 */
public class AgendaHeaderView extends LinearLayout {

    /**
     * 引入布局文件
     * @param parent AgendaHeaderView 的父布局 ，应该就是AgendaListView
     * @return
     */
    public static AgendaHeaderView inflate(ViewGroup parent) {
        return (AgendaHeaderView) LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_view_header, parent, false);
    }

    // region Constructors
    // 继承的构造方法
    public AgendaHeaderView(Context context) {
        super(context);
    }

    public AgendaHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgendaHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // endregion

    // region Public methods

    /**
     * 设置 headerView 上文字的样式,内容
     * @param day 需要设置的日期
     * @param currentDayTextColor 当前设置日期的颜色
     */
    public void setDay(Calendar day, int currentDayTextColor) {
        //初始化控件
        TextView txtDayOfMonth = (TextView) findViewById(R.id.view_agenda_day_of_month);
        TextView txtDayOfWeek = (TextView) findViewById(R.id.view_agenda_day_of_week);
        View circleView = findViewById(R.id.view_day_circle_selected);

        //得到今天
        Calendar today = CalendarManager.getInstance().getToday();

        //设置星期格式
        SimpleDateFormat dayWeekFormatter = new SimpleDateFormat("E");

        //list 左边日期的颜色
        txtDayOfMonth.setTextColor(getResources().getColor(R.color.calendar_text_default));
        //list 左边星期的颜色
        txtDayOfWeek.setTextColor(getResources().getColor(R.color.calendar_text_default));

        //如果当前日期为today,则显示circleView,同时设置circleView的颜色粗细,否则就隐藏circleView
        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(currentDayTextColor);
            txtDayOfWeek.setTextColor(currentDayTextColor);
            circleView.setVisibility(VISIBLE);
            GradientDrawable drawable = (GradientDrawable) circleView.getBackground();
            drawable.setStroke((int) (2 * Resources.getSystem().getDisplayMetrics().density), currentDayTextColor);
        } else {
            circleView.setVisibility(INVISIBLE);
        }

        //添加文字
        txtDayOfMonth.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        txtDayOfWeek.setText(dayWeekFormatter.format(day.getTime()));
    }
    // endregion
}
