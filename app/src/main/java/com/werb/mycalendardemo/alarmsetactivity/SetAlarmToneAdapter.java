package com.werb.mycalendardemo.alarmsetactivity;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.werb.mycalendardemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer-pc on 2016/4/18.
 */
public class SetAlarmToneAdapter extends RecyclerView.Adapter<SetAlarmToneAdapter.ListViewHolder> implements View.OnClickListener {

    public List<String> ringList;
    public RingtoneManager rm;
    public Context context;
    public Cursor cursor;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public SetAlarmToneAdapter(Context ctx){
        context = ctx;
        getRing();
    }

    @Override
    public SetAlarmToneAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_set_alarm_tone_list,null);
        v.setOnClickListener(this);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetAlarmToneAdapter.ListViewHolder holder, int position) {
        ListViewHolder listViewHolder = holder;
        listViewHolder.tone_path.setText(ringList.get(position));
        listViewHolder.itemView.setTag(R.id.tag_first,ringList.get(position));
        listViewHolder.itemView.setTag(R.id.tag_second,position);
    }

    @Override
    public int getItemCount() {
        return ringList.size();
    }

    public void getRing() {
		/* 新建一个arraylist来接收从系统中获取的短信铃声数据 */
        ringList = new ArrayList<String>();
		/* 添加“跟随系统”选项 */
        ringList.add("跟随系统");
		/* 获取RingtoneManager */
        rm = new RingtoneManager(context);
		/* 指定获取类型为短信铃声 */
        rm.setType(RingtoneManager.TYPE_RINGTONE);
		/* 创建游标 */
        cursor = rm.getCursor();
		/* 游标移动到第一位，如果有下一项，则添加到ringlist中 */
        if (cursor.moveToFirst()) {
            do { // 游标获取RingtoneManager的列inde x
                ringList.add(cursor
                        .getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag(R.id.tag_first), (Integer) v.getTag(R.id.tag_second));
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        public TextView tone_path;

        public ListViewHolder(View itemView) {
            super(itemView);
            tone_path = (TextView) itemView.findViewById(R.id.tone_path);
        }
    }

    //点击事件接口
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data,int position);
    }
}
