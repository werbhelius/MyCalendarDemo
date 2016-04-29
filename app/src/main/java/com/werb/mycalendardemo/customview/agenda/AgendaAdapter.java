package com.werb.mycalendardemo.customview.agenda;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.werb.mycalendardemo.customview.render.DefaultEventRenderer;
import com.werb.mycalendardemo.customview.render.EventRenderer;
import com.werb.mycalendardemo.models.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Adapter for the agenda, implements StickyListHeadersAdapter.
 * Days as sections and CalendarEvents as list items.
 */
public class AgendaAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    /**
     * CalendarEvent 事件集合
     */
    private List<CalendarEvent> mEvents = new ArrayList<>();
    private List<EventRenderer<?>> mRenderers = new ArrayList<>();
    /**
     * 当前日期的颜色
     */
    private int mCurrentDayColor;

    // region Constructor

    // 构造方法
    public AgendaAdapter(int currentDayTextColor) {
        this.mCurrentDayColor = currentDayTextColor;
        System.out.println("AgendaAdapter--1");
    }

    // endregion

    // region Public methods

    /**
     * 根据传入的 Event 更新 ListView中的数据
     * @param events 事件集合
     */
    public void updateEvents(List<CalendarEvent> events) {
        this.mEvents.clear();
        this.mEvents.addAll(events);
        notifyDataSetChanged();
    }

    // endregion

    // region Interface - StickyListHeadersAdapter

    //实现接口 StickyListHeadersAdapter 需要重写的方法

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        // convertView 重复使用的View 需要判断是否为空
        AgendaHeaderView agendaHeaderView = (AgendaHeaderView) convertView;
        if (agendaHeaderView == null) {
            agendaHeaderView = AgendaHeaderView.inflate(parent);
        }
        //设置headerView中日期和星期的文字和样式
        agendaHeaderView.setDay(getItem(position).getInstanceDay(), mCurrentDayColor);
        //返回agendaHeaderView
        return agendaHeaderView;
    }

    @Override
    public long getHeaderId(int position) {
        //返回此 Calendar 的时间值，以毫秒为单位。
        return mEvents.get(position).getInstanceDay().getTimeInMillis();
    }

    // endregion

    // region Class - BaseAdapter

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public CalendarEvent getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventRenderer eventRenderer = new DefaultEventRenderer();
        final CalendarEvent event = getItem(position);

        // Search for the correct event renderer
        for (EventRenderer renderer : mRenderers) {
            /*
                官方API解释
                判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，
                或是否是其超类或超接口。如果是则返回 true；否则返回 false。
                如果该 Class 表示一个基本类型，且指定的 Class 参数正是该 Class 对象，则该方法返回 true；否则返回 false。
             */
            //判断event的渲染布局是否与renderer.getRenderType()一致
            if(event.getClass().isAssignableFrom(renderer.getRenderType())) {
                eventRenderer = renderer;
                break;
            }
        }
        //引入布局文件，即不同的event对应自己的renderer布局
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(eventRenderer.getEventLayout(), parent, false);

        //初始化布局
        System.out.println("---event:"+event.toString());
        eventRenderer.render(convertView, event);
        return convertView;
    }

    /**
     * 添加 布局渲染集合
     * @param renderer 布局渲染类
     */
    public void addEventRenderer(@NonNull final EventRenderer<?> renderer) {
        mRenderers.add(renderer);
    }

    // endregion
}
