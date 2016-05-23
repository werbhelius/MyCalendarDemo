package com.werb.mycalendardemo.pager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.ScheduleDetailActivity;
import com.werb.mycalendardemo.customview.pageradapter.RecycleAdapter;
import com.werb.mycalendardemo.database.AlarmDBSupport;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.Events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by acer-pc on 2016/5/13.
 */
public class WeekPager extends BasePager {

    @Bind(R.id.recycle_view)
    RecyclerView recycle_view;
    @Bind(R.id.info)
    TextView info;

    List<Object> listList;

    public WeekPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.weekpager_view, null);

        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void initData() {

        chooseDayFromClick();

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());


        recycle_view.setLayoutManager(new LinearLayoutManager(mActivity));
        setDayAndWeek(today);


        recycle_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                BusProvider.getInstance().send(new Events.AgendaListViewTouchedEvent());
            }
        });

    }

    /**
     * 点击 CalendarView 选择不同的日期
     */
    private void chooseDayFromClick(){
        //BusProvider处理事件
        BusProvider.getInstance().toObserverable()
                .subscribe(event -> {
                    if (event instanceof Events.DayClickedEvent) {

                        Events.DayClickedEvent clickedEvent = (Events.DayClickedEvent) event;

                        Calendar selectDay = clickedEvent.getCalendar();
                        setDayAndWeek(selectDay);


                    }else if(event instanceof Events.GoBackToDay){
                        Calendar today = Calendar.getInstance();
                        today.setTimeInMillis(System.currentTimeMillis());
                        setDayAndWeek(today);
                    }

                });
    }

    /**
     * 构建显示的日期和星期
     */
    private void setDayAndWeek(Calendar selectDay){
        listList = new ArrayList<>();
        //Layout 中的 5 个布局
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH,selectDay.get(Calendar.MONTH));
            calendar.set(Calendar.YEAR,selectDay.get(Calendar.YEAR));
            calendar.set(Calendar.WEEK_OF_MONTH,selectDay.get(Calendar.WEEK_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK,i+1);

            List<Object> list = new AlarmDBSupport(mActivity).getDataByDay(calendar);
            for(Object o:list){
                listList.add(o);
            }
        }

        RecycleAdapter recycleAdapter = new RecycleAdapter("weekPager",listList,mActivity);
        recycle_view.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();

        if(listList.size()==0){
            info.setText("本周还没有日程信息，点击加号添加");
        }else {
            info.setText("本星期的日程信息如下显示：");
        }

        //Item 点击事件
        itemClick(recycleAdapter);
    }

    /**
     //     * listView 的Item点击事件
     //     */
    private void itemClick(RecycleAdapter adapter){
        //Item 点击事件
        adapter.setOnMyItemListener(new RecycleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
    }
}
