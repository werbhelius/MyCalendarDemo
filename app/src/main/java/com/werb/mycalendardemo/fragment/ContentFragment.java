package com.werb.mycalendardemo.fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.werb.mycalendardemo.AlarmBean;
import com.werb.mycalendardemo.MainActivity;
import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.database.AlarmDBSupport;
import com.werb.mycalendardemo.models.BaseCalendarEvent;
import com.werb.mycalendardemo.models.CalendarEvent;
import com.werb.mycalendardemo.pager.AboutMePager;
import com.werb.mycalendardemo.pager.BasePager;
import com.werb.mycalendardemo.pager.DayPager;
import com.werb.mycalendardemo.pager.HomePager;
import com.werb.mycalendardemo.pager.WeekPager;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.ColorUtils;
import com.werb.mycalendardemo.utils.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by acer-pc on 2016/3/11.
 */
public class ContentFragment extends BaseFragment {

    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private List<BasePager> mPageList;
    private NavigationView navigationView;//菜单栏
    private DrawerLayout drawerLayout;//DrawerLayout


    private  List<CalendarEvent> eventList;
    private AlarmDBSupport support;
    private HomePager homePager;
    private DayPager dayPager;
    private WeekPager weekPager;
    private AboutMePager aboutMePager;



    @Override
    public View initView() {

        View view=View.inflate(mActivity, R.layout.fragment_content,null);

        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void initDate() {

        homePager = new HomePager(mActivity);
        dayPager = new DayPager(mActivity);
        weekPager = new WeekPager(mActivity);
        aboutMePager = new AboutMePager(mActivity);

        //主界面添加数据
        mPageList= new ArrayList<>();

        mPageList.add(homePager);
        mPageList.add(dayPager);
        mPageList.add(weekPager);
        mPageList.add(aboutMePager);

        vpContent.setAdapter(new VpContentAdapter());

        //获取侧边栏
        MainActivity mainUi= (MainActivity) mActivity;
        navigationView = mainUi.getNavigationView();
        navigationView.setCheckedItem(R.id.schedule);
        initDataOfEveryPager(0);
        drawerLayout=mainUi.getDrawerLayout();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.schedule:
                        vpContent.setCurrentItem(0, false);//设置当前的页面，取消平滑滑动
                        initDataOfEveryPager(0);
                        break;
                    case R.id.day:
                        vpContent.setCurrentItem(1,false);
                        dayPager.initData(eventList);
                        break;
                    case R.id.week:
                        vpContent.setCurrentItem(2,false);
                        weekPager.initData(eventList);
                        break;
                    case R.id.aboutMe:
                        vpContent.setCurrentItem(3,false);
                        aboutMePager.initData(eventList);
                        break;
                }
                item.setChecked(true);//点击了设置为选中状态
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    /**
     * 初始化每个界面的数据
     */
    private void initDataOfEveryPager(int position){

        //第一次进入初始化数据
        support = new AlarmDBSupport(mActivity);
        eventList = new ArrayList<>();
        List<AlarmBean> alllist = support.getAll();
        mockList(eventList, alllist);

        homePager = (HomePager) mPageList.get(position);
        homePager.initData(eventList);

        BusProvider.getInstance().toObserverable().subscribe(event ->{
            if(event instanceof Events.GoBackToDay){
                homePager.agenda_view.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
            }
        });


    }

    /**
     * viewPager数据适配器
     */
    class VpContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager =mPageList.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPageList.get(position).mRootView);
        }
    }

    /**
     * 构建数据集合
     * @param eventList list显示的集合
     * @param beanList 从数据库中读到的全部数据集合
     */
    private void mockList(List<CalendarEvent> eventList, List<AlarmBean> beanList) {

        for (AlarmBean bean : beanList) {
            Calendar startTime1 = Calendar.getInstance();
            startTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());
            Calendar endTime1 = Calendar.getInstance();
            endTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());

            boolean isAllday;
            if (bean.getIsAllday() == 1) {
                isAllday = true;
            } else {
                isAllday = false;
            }

            int colorId = ColorUtils.getColorFromStr(bean.getAlarmColor());

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

            BaseCalendarEvent event1 = new BaseCalendarEvent(bean.getId(),bean.getTitle(), bean.getDescription(), bean.getLocal(),
                    ContextCompat.getColor(mActivity, colorId), startTime1, endTime1, isAllday, startAndEndTime);
            System.out.println("---" + event1.toString());
            eventList.add(event1);
        }

    }
}
