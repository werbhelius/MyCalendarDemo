package com.werb.mycalendardemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.werb.mycalendardemo.AlarmBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by acer-pc on 2016/4/18.
 */
public class AlarmDBSupport {

    private Context context;
    private MyOpenHelper mop;
    private SQLiteDatabase db;
    private AlarmDBSupport instance = null;

    public AlarmDBSupport(Context context) {
        this.context = context;
        mop = new MyOpenHelper(context, "alarmlist.db", null, 1);
        db = mop.getWritableDatabase();
    }

    public  void deactivate() {
        if (null != db && db.isOpen()) {
            db.close();
        }
        db = null;
    }

    /**
     * 插入数据
     */
    public void insertAlarmDate(AlarmBean bean){
        db.execSQL("insert into alarmlist(title,isAllday,isVibrate,year,month,day,startTimeHour," +
                "startTimeMinute,endTimeHour,endTimeMinute,alarmTime,alarmColor,alarmTonePath,local," +
                "description,replay) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{bean.getTitle(),bean.getIsAllday(),
        bean.getIsVibrate(),bean.getYear(),bean.getMonth(),bean.getDay(),bean.getStartTimeHour(),bean.getStartTimeMinute(),bean.getEndTimeHour(),
                bean.getEndTimeMinute(),bean.getAlarmTime(),bean.getAlarmColor(),bean.getAlarmTonePath(),bean.getLocal(),bean.getDescription(),bean.getReplay()});
    }

    /**
     * 查询全部
     */
    public List<AlarmBean> getAll(){
        List<AlarmBean> beanList = new ArrayList<AlarmBean>();
        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            do{
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setTitle(cursor.getString(1));
                bean.setIsAllday(cursor.getInt(2));
                bean.setIsVibrate(cursor.getInt(3));
                bean.setYear(cursor.getInt(4));
                bean.setMonth(cursor.getInt(5));
                bean.setDay(cursor.getInt(6));
                bean.setStartTimeHour(cursor.getInt(7));
                bean.setStartTimeMinute(cursor.getInt(8));
                bean.setEndTimeHour(cursor.getInt(9));
                bean.setEndTimeMinute(cursor.getInt(10));
                bean.setAlarmTime(cursor.getString(11));
                bean.setAlarmColor(cursor.getString(12));
                bean.setAlarmTonePath(cursor.getString(13));
                bean.setLocal(cursor.getString(14));
                bean.setDescription(cursor.getString(15));
                bean.setReplay(cursor.getString(16));

                beanList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照日期查找
     * @return
     */
    public List<Object> getDataByDay(Calendar calendar){
        List<Object> beanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from alarmlist where year=? and month=? and day=?",
                new String[]{calendar.get(Calendar.YEAR)+"",calendar.get(Calendar.MONTH)+"",calendar.get(Calendar.DAY_OF_MONTH)+""});
        if(cursor.moveToFirst()){
            do{
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setTitle(cursor.getString(1));
                bean.setIsAllday(cursor.getInt(2));
                bean.setIsVibrate(cursor.getInt(3));
                bean.setYear(cursor.getInt(4));
                bean.setMonth(cursor.getInt(5));
                bean.setDay(cursor.getInt(6));
                bean.setStartTimeHour(cursor.getInt(7));
                bean.setStartTimeMinute(cursor.getInt(8));
                bean.setEndTimeHour(cursor.getInt(9));
                bean.setEndTimeMinute(cursor.getInt(10));
                bean.setAlarmTime(cursor.getString(11));
                bean.setAlarmColor(cursor.getString(12));
                bean.setAlarmTonePath(cursor.getString(13));
                bean.setLocal(cursor.getString(14));
                bean.setDescription(cursor.getString(15));
                bean.setReplay(cursor.getString(16));

                beanList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照id查找
     * @return
     */
    public AlarmBean getDataById(int id){
        Cursor cursor = db.rawQuery("select * from alarmlist where _id=?", new String[]{id + ""});
        cursor.moveToNext();
        AlarmBean bean = new AlarmBean();
        bean.setId(cursor.getInt(0));
        bean.setTitle(cursor.getString(1));
        bean.setIsAllday(cursor.getInt(2));
        bean.setIsVibrate(cursor.getInt(3));
        bean.setYear(cursor.getInt(4));
        bean.setMonth(cursor.getInt(5));
        bean.setDay(cursor.getInt(6));
        bean.setStartTimeHour(cursor.getInt(7));
        bean.setStartTimeMinute(cursor.getInt(8));
        bean.setEndTimeHour(cursor.getInt(9));
        bean.setEndTimeMinute(cursor.getInt(10));
        bean.setAlarmTime(cursor.getString(11));
        bean.setAlarmColor(cursor.getString(12));
        bean.setAlarmTonePath(cursor.getString(13));
        bean.setLocal(cursor.getString(14));
        bean.setDescription(cursor.getString(15));
        bean.setReplay(cursor.getString(16));

        return bean;
    }

    /**
     * 删除指定数据
     */
    public void deleteDataById(int id) {
        db.delete("alarmlist", "_id=?", new String[]{id+""});
    }

    public void updateDataById(int id,AlarmBean bean){
        ContentValues values=new ContentValues();
        values.put("title",bean.getTitle());
        values.put("isAllday",bean.getIsAllday());
        values.put("isVibrate",bean.getIsVibrate());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("startTimeHour",bean.getStartTimeHour());
        values.put("startTimeMinute",bean.getStartTimeMinute());
        values.put("endTimeHour",bean.getEndTimeHour());
        values.put("endTimeMinute",bean.getEndTimeMinute());
        values.put("alarmTime",bean.getAlarmTime());
        values.put("alarmColor",bean.getAlarmColor());
        values.put("alarmTonePath",bean.getAlarmTonePath());
        values.put("local",bean.getLocal());
        values.put("description",bean.getDescription());
        values.put("replay",bean.getReplay());
        db.update("alarmlist",values,"_id=?",new String[]{id+""});
    }

    public Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                "_id",
                "title",
                "isAllday",
                "isVibrate",
                "year",
                "month",
                "day",
                "startTimeHour",
                "startTimeMinute",
                "endTimeHour",
                "endTimeMinute",
                "alarmTime",
                "alarmColor",
                "alarmTonePath",
                "local",
                "description",
                "replay"
        };
        return db.query("alarmlist", columns, null, null, null, null,
                null);
    }
}
