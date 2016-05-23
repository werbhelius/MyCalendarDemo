package com.werb.mycalendardemo.customview.pageradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.AlarmBean;
import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.utils.ColorUtils;
import com.werb.mycalendardemo.utils.DateHelper;

import java.util.List;

/**
 * Created by acer-pc on 2016/5/12.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String tag;
    private List<Object> list;
    private Context context;
    private MyItemClickListener listener;

    public RecycleAdapter(String tag, List<Object> list,Context ctx) {
        this.tag = tag;
        this.list = list;
        context = ctx;

        System.out.println("~~~" + this.list.toString());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(tag.equals("dayPager")) {
            View view = View.inflate(parent.getContext(), R.layout.daypager_list, null);
            System.out.println("--adapter" + list.size());
            return new DayPagerViewHolder(view);
        }else if(tag.equals("weekPager")){
            View view = View.inflate(parent.getContext(), R.layout.weekpager_list, null);
            System.out.println("--adapter" + list.size());
            return new WeekPagerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(tag.equals("dayPager")) {
            AlarmBean bean = (AlarmBean) list.get(position);
            DayPagerViewHolder dayPagerViewHolder = (DayPagerViewHolder) holder;
            dayPagerViewHolder.Alarm_id.setText(bean.getId() + "");
            dayPagerViewHolder.day_event_title.setText(bean.getTitle());
            dayPagerViewHolder.day_event_startAndEndTime.setText(DateHelper.getStartAndEndTime(bean));
            dayPagerViewHolder.day_event_location.setText(bean.getLocal());
            dayPagerViewHolder.day_event_description.setText(bean.getDescription());
            dayPagerViewHolder.lay_bg.setBackgroundColor(context.getResources().getColor(ColorUtils.getColorFromStr(bean.getAlarmColor())));

            dayPagerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        listener.onItemClick(v,position);
                    }
                }
            });
        }else if(tag.equals("weekPager")){
            AlarmBean bean = (AlarmBean) list.get(position);
            WeekPagerViewHolder weekPagerViewHolder = (WeekPagerViewHolder) holder;
            weekPagerViewHolder.Alarm_id.setText(bean.getId() + "");
            weekPagerViewHolder.day_event_title.setText(bean.getTitle());
            weekPagerViewHolder.day_event_startAndEndTime.setText(DateHelper.getStartAndEndTime(bean));
            weekPagerViewHolder.day_event_location.setText(bean.getLocal());
            weekPagerViewHolder.day_event_date.setText(DateHelper.getScheduleDate(bean));
            weekPagerViewHolder.lay_bg.setBackgroundColor(context.getResources().getColor(ColorUtils.getColorFromStr(bean.getAlarmColor())));

            weekPagerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        listener.onItemClick(v,position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DayPagerViewHolder extends RecyclerView.ViewHolder{

        private TextView Alarm_id, day_event_title, day_event_startAndEndTime, day_event_location, day_event_description;
        private LinearLayout lay_bg;

        public DayPagerViewHolder(View itemView) {
            super(itemView);

            Alarm_id = (TextView) itemView.findViewById(R.id.Alarm_id);
            day_event_title = (TextView) itemView.findViewById(R.id.day_event_title);
            day_event_startAndEndTime = (TextView) itemView.findViewById(R.id.day_event_startAndEndTime);
            day_event_location = (TextView) itemView.findViewById(R.id.day_event_location);
            day_event_description = (TextView) itemView.findViewById(R.id.day_event_description);
            lay_bg = (LinearLayout) itemView.findViewById(R.id.lay_bg);
        }

    }

    class WeekPagerViewHolder extends RecyclerView.ViewHolder{

        private TextView Alarm_id, day_event_title, day_event_startAndEndTime, day_event_location, day_event_date;
        private LinearLayout lay_bg;

        public WeekPagerViewHolder(View itemView) {
            super(itemView);

            Alarm_id = (TextView) itemView.findViewById(R.id.Alarm_id);
            day_event_title = (TextView) itemView.findViewById(R.id.day_event_title);
            day_event_startAndEndTime = (TextView) itemView.findViewById(R.id.day_event_startAndEndTime);
            day_event_location = (TextView) itemView.findViewById(R.id.day_event_location);
            day_event_date = (TextView) itemView.findViewById(R.id.day_event_date);
            lay_bg = (LinearLayout) itemView.findViewById(R.id.lay_bg);
        }
    }

    private int getDeviceWidth() {
        // 得到屏幕的宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public interface MyItemClickListener {
        public void onItemClick(View view,int position);
    }

    public void setOnMyItemListener(MyItemClickListener listener){
        this.listener = listener;
    }
}
