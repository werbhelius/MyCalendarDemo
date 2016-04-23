package com.werb.mycalendardemo.pager;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.werb.mycalendardemo.MainActivity;
import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.ScheduleDetailActivity;
import com.werb.mycalendardemo.customview.agenda.AgendaAdapter;
import com.werb.mycalendardemo.customview.agenda.AgendaView;
import com.werb.mycalendardemo.customview.calendar.CalendarView;
import com.werb.mycalendardemo.customview.render.DefaultEventRenderer;
import com.werb.mycalendardemo.customview.render.EventRenderer;
import com.werb.mycalendardemo.database.AlarmDBSupport;
import com.werb.mycalendardemo.models.CalendarEvent;
import com.werb.mycalendardemo.utils.CalendarManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 用于显示界面的 Pager ，通过设置不同的 adapter 实现布局复用
 */
public class HomePager extends BasePager implements StickyListHeadersListView.OnStickyHeaderChangedListener {

    private static final String LOG_TAG = HomePager.class.getSimpleName();

    @Bind(R.id.agenda_view)
    public AgendaView agenda_view;
    private AlarmDBSupport support;
    public AgendaAdapter agendaAdapter;

    private int mAgendaCurrentDayTextColor = mActivity.getResources().getColor(R.color.calendar_text_current_day);

    public HomePager(Activity mActivity) {
        super(mActivity);

    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_home, null);

        ButterKnife.bind(this, view);

        return view;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData(List<CalendarEvent> eventList) {
        // 初始化AgendaView的数据适配器
        agendaAdapter = new AgendaAdapter(mAgendaCurrentDayTextColor);
        agenda_view.getAgendaListView().setAdapter(agendaAdapter);

        //注册AgendaListView的OnStickyHeaderChanged监听事件
        agenda_view.getAgendaListView().setOnStickyHeaderChangedListener(this);
        agenda_view.getAgendaListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView alarm_id = (TextView) view.findViewById(R.id.Alarm_id);
                Toast.makeText(mActivity,position+"-"+alarm_id.getText().toString(),Toast.LENGTH_SHORT).show();
                if(!alarm_id.getText().toString().equals("0")){
                    Intent intent = new Intent(mActivity, ScheduleDetailActivity.class);
                    intent.putExtra("id",alarm_id.getText().toString());
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        });

        //将日期与event进行匹配
        CalendarManager.getInstance().loadEvents(eventList);

        // 添加默认的 Renderer 渲染布局
        addEventRenderer(new DefaultEventRenderer());

    }

    //为event添加布局渲染
    public void addEventRenderer(@NonNull final EventRenderer<?> renderer) {
        AgendaAdapter adapter = (AgendaAdapter) agenda_view.getAgendaListView().getAdapter();
        adapter.addEventRenderer(renderer);
    }

    /**
     * 当ListView的头部改变时,同时改变CalendarView的日期
     *
     * @param l            ListView
     * @param header       头部 header View
     * @param itemPosition 位置
     * @param headerId     头部 header ID
     */
    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        Log.d(LOG_TAG, String.format("onStickyHeaderChanged, position = %d, headerId = %d", itemPosition, headerId));

        if (CalendarManager.getInstance().getEvents().size() > 0) {
            CalendarEvent event = CalendarManager.getInstance().getEvents().get(itemPosition);
            if (event != null) {
                getCalendarView().scrollToDate(event);
            }
        }
    }

    /**
     * 得到 CalendarView
     */
    private CalendarView getCalendarView() {
        MainActivity mainUi = (MainActivity) mActivity;
        return mainUi.calendar_view;
    }

//    //数据集合
//    private void mockList(List<CalendarEvent> eventList, List<AlarmBean> beanList) {
//
//        for (AlarmBean bean : beanList) {
//            Calendar startTime1 = Calendar.getInstance();
//            startTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());
//            Calendar endTime1 = Calendar.getInstance();
//            endTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());
//
//            boolean isAllday;
//            if (bean.getIsAllday() == 1) {
//                isAllday = true;
//            } else {
//                isAllday = false;
//            }
//
//            int colorId = 0;
//            switch (bean.getAlarmColor()) {
//                case "默认颜色":
//                    colorId = R.color.moren;
//                    break;
//                case "罗勒绿":
//                    colorId = R.color.luolelv;
//                    break;
//                case "耀眼黄":
//                    colorId = R.color.yaoyanhuang;
//                    break;
//                case "番茄红":
//                    colorId = R.color.fanqiehong;
//                    break;
//                case "低调灰":
//                    colorId = R.color.didiaohui;
//                    break;
//                case "橘子红":
//                    colorId = R.color.juzihong;
//                    break;
//                case "深空蓝":
//                    colorId = R.color.shenkonglan;
//                    break;
//            }
//
//            Calendar startCalendar = Calendar.getInstance();
//            startCalendar.set(Calendar.HOUR_OF_DAY, bean.getStartTimeHour());
//            startCalendar.set(Calendar.MINUTE, bean.getStartTimeMinute());
//            Calendar endCalendar = Calendar.getInstance();
//            endCalendar.set(Calendar.HOUR_OF_DAY, bean.getEndTimeHour());
//            endCalendar.set(Calendar.MINUTE, bean.getEndTimeMinute());
//
//            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
//            String startTime = df.format(startCalendar.getTime());
//            String endtTime = df.format(endCalendar.getTime());
//            String startAndEndTime = startTime+"-"+endtTime;
//
//            BaseCalendarEvent event1 = new BaseCalendarEvent(bean.getTitle(), bean.getDescription(), bean.getLocal(),
//                    ContextCompat.getColor(mActivity, colorId), startTime1, endTime1, isAllday,startAndEndTime);
//            System.out.println("---"+event1.toString());
//            eventList.add(event1);
//        }
//
//    }
}
