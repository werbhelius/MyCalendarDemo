package com.werb.mycalendardemo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.werb.mycalendardemo.alarmremind.SendAlarmBroadcast;
import com.werb.mycalendardemo.alarmsetactivity.SetAlarmTimeActivity;
import com.werb.mycalendardemo.alarmsetactivity.SetAlarmToneActivity;
import com.werb.mycalendardemo.alarmsetactivity.SetColorActivity;
import com.werb.mycalendardemo.alarmsetactivity.SetLocalActivity;
import com.werb.mycalendardemo.alarmsetactivity.SetRePlayActivity;
import com.werb.mycalendardemo.database.AlarmDBSupport;
import com.werb.mycalendardemo.utils.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加活动时间界面
 */
public class AddScheduleActivity extends AppCompatActivity {

    private DatePickerDialog mDataPicker;
    private TimePickerDialog mStartTimePicker, mEndTimePicker;
    private boolean isAllDay = false;
    private boolean isVibrate = false;
    private AlarmBean alarmBean = new AlarmBean();
    private AlarmDBSupport support;
    private int id;

    @Bind(R.id.alarm_title)
    EditText alarm_title;
    @Bind(R.id.alarm_description)
    EditText alarm_description;
    @Bind(R.id.alarm_replay)
    TextView alarm_replay;
    @Bind((R.id.alarm_remind))
    TextView alarm_remind;
    @Bind(R.id.alarm_local)
    TextView alarm_local;
    @Bind(R.id.alarm_color)
    TextView alarm_color;
    @Bind(R.id.alarm_tone_Path)
    TextView alarm_tone_Path;
    @Bind(R.id.alarm_date)
    TextView alarm_date;
    @Bind(R.id.insert_update_title)
    TextView insert_update_title;
    @Bind(R.id.action_bar)
    LinearLayout action_bar;

    @OnClick(R.id.alarm_date)
    void openDatePicker() {
        getDatePickerDialog();
        mDataPicker.show();
    }

    @Bind(R.id.alarm_start_time)
    TextView alarm_start_time;

    @OnClick(R.id.alarm_start_time)
    void openStartTimePicker() {
        getStartTimePickerDialog();
        mStartTimePicker.show();
    }

    @Bind(R.id.alarm_end_time)
    TextView alarm_end_time;

    @OnClick(R.id.alarm_end_time)
    void openEndTimePicker() {
        getEndTimePickerDialog();
        mEndTimePicker.show();
    }

    @OnClick(R.id.left_clear)
    void clear() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.layout_alarm_replay)
    void openSetReplayActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetRePlayActivity.class), 0);
    }

    @OnClick(R.id.layout_alarm_remind)
    void openSetAlarmTimeActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetAlarmTimeActivity.class), 1);
    }

    @OnClick(R.id.layout_alarm_local)
    void openSetLocalActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetLocalActivity.class), 2);
    }

    @OnClick(R.id.layout_alarm_color)
    void openSetColorActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetColorActivity.class), 3);
    }

    @OnClick(R.id.layout_alarm_tone_Path)
    void openSetAlarmToneActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetAlarmToneActivity.class), 4);
    }

    @Bind(R.id.sw_all_day)
    Switch sw_all_day;

    @OnClick(R.id.sw_all_day)
    void allday() {
        if (!isAllDay) {
            alarm_start_time.setVisibility(View.GONE);
            alarm_end_time.setVisibility(View.GONE);
            isAllDay = true;
        } else {
            alarm_start_time.setVisibility(View.VISIBLE);
            alarm_end_time.setVisibility(View.VISIBLE);
            isAllDay = false;
        }
    }

    @Bind(R.id.sw_vibrate)
    Switch sw_vibrate;

    @OnClick(R.id.sw_vibrate)
    void is_Vibrate() {
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (!isVibrate) {
            alarmBean.setIsVibrate(1);
            vibrator.vibrate(500);
            isVibrate = true;
        } else {
            alarmBean.setIsVibrate(0);
            vibrator.cancel();
            isVibrate = false;
        }
    }

    @OnClick(R.id.tv_save)
    void saveAlarmBeanToDB() {
        //设置标题
        if (alarm_title.getText().toString().equals("")) {
            alarmBean.setTitle("无");
        } else {
            alarmBean.setTitle(alarm_title.getText().toString());
        }

        //设置描述
        if (alarm_description.getText().toString().equals("")) {
            alarmBean.setDescription("暂无描述");
        } else {
            alarmBean.setDescription(alarm_description.getText().toString());
        }

        //设置地点
        if (alarm_local.getText().equals("添加地点")) {
            alarmBean.setLocal("暂无地点");
        }

        //设置是否全天
        if (isAllDay) {
            alarmBean.setIsAllday(1);
        } else {
            alarmBean.setIsAllday(0);
        }

        //设置年月日
        if (alarm_date.getText().toString().equals("选择活动日期")) {
            alarmBean.setYear(getToDay().get(Calendar.YEAR));
            alarmBean.setMonth(getToDay().get(Calendar.MONTH));
            alarmBean.setDay(getToDay().get(Calendar.DAY_OF_MONTH));
        }

        //设置开始时间
        if (alarm_start_time.getText().toString().equals("选择开始时间")) {
            if(isAllDay){
                alarmBean.setStartTimeHour(0);
                alarmBean.setStartTimeMinute(0);
            }else {
                alarmBean.setStartTimeHour(getToDay().get(Calendar.HOUR_OF_DAY));
                alarmBean.setStartTimeMinute(getToDay().get(Calendar.MINUTE));
            }
        }

        //设置结束时间
        if (alarm_end_time.getText().toString().equals("选择结束时间")) {
            if(isAllDay){
                alarmBean.setEndTimeHour(23);
                alarmBean.setEndTimeMinute(59);
            }else {
                alarmBean.setEndTimeHour(getToDay().get(Calendar.HOUR_OF_DAY) + 1);
                alarmBean.setEndTimeMinute(getToDay().get(Calendar.MINUTE));
            }
        }

        //设置提醒时间
        if (alarm_remind.getText().toString().equals("选择提醒时间")) {
            alarmBean.setAlarmTime("默认");
        } else {
            alarmBean.setAlarmTime(alarm_remind.getText().toString());
        }

        //设置重复天
        if (alarm_replay.getText().toString().equals("不重复")) {
            alarmBean.setReplay("不重复");
        } else {
            alarmBean.setReplay(alarm_replay.getText().toString());
        }

        //设置铃声
        if (alarm_tone_Path.getText().toString().equals("选择铃声")) {
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
                    AddScheduleActivity.this, RingtoneManager.TYPE_RINGTONE);
            alarmBean.setAlarmTonePath(uri.toString());
        }

        //设置颜色
        if (alarm_color.getText().toString().equals("默认颜色")) {
            alarmBean.setAlarmColor("默认颜色");
        } else {
            alarmBean.setAlarmColor(alarm_color.getText().toString());
        }

        //设置地区
        if (alarm_local.getText().toString().equals("暂无地点")) {
            alarmBean.setLocal("暂无地点");
        } else {
            alarmBean.setLocal(alarm_local.getText().toString());
        }

        if (id == 0) {
            System.out.println("保存的数据:" + alarmBean.toString());
            support.insertAlarmDate(alarmBean);

            SendAlarmBroadcast.startAlarmService(this);

            Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            System.out.println("更新的数据:" + alarmBean.toString());
            support.updateDataById(id, alarmBean);

            SendAlarmBroadcast.startAlarmService(this);

            Toast.makeText(this, "更新成功！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        ButterKnife.bind(this);

        support = new AlarmDBSupport(getApplicationContext());
        if (getIntent().getStringExtra("type").equals("DetailToAdd")) {
            Intent intent = getIntent();
            AlarmBean bean = (AlarmBean) intent.getSerializableExtra("AlarmBean");
            id = bean.getId();
            alarm_title.setText(bean.getTitle());
            alarm_remind.setText(bean.getAlarmTime());
            alarm_color.setText(bean.getAlarmColor());
            alarm_description.setText(bean.getDescription());
            alarm_local.setText(bean.getLocal());
            alarm_replay.setText(bean.getReplay());
//            alarm_date.setText(DateHelper.getScheduleDate(bean));
//            alarm_start_time.setText(DateHelper.getStartTime(bean));
//            alarm_end_time.setText(DateHelper.getEndTime(bean));
            insert_update_title.setText("修改活动");
            //动态改变颜色
            int colorId = ColorUtils.getColorFromStr(bean.getAlarmColor());
            action_bar.setBackgroundColor(getResources().getColor(colorId));
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(colorId));
            }
        } else {
            insert_update_title.setText("新建活动");
        }
    }

    /**
     * 获取日期选择器
     */
    private void getDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  EE");
                alarm_date.setText(df.format(calendar.getTime()));

                //设置选择的年、月、日
                alarmBean.setYear(year);
                alarmBean.setMonth(monthOfYear);
                alarmBean.setDay(dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获取开始时间选择器
     */
    private void getStartTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mStartTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                alarm_start_time.setText("开始时间:  " + df.format(calendar.getTime()));

                //设置开始时间的小时、分钟
                alarmBean.setStartTimeHour(hourOfDay);
                alarmBean.setStartTimeMinute(minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    /**
     * 获取结束时间选择器
     */
    private void getEndTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mEndTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                alarm_end_time.setText("结束时间:  " + df.format(calendar.getTime()));


                //设置结束时间的小时、分钟
                alarmBean.setEndTimeHour(hourOfDay);
                alarmBean.setEndTimeMinute(minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private Calendar getToDay() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return today;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == 0) {
                if (data != null) {
                    alarm_replay.setText(data.getStringExtra("replay"));
                    alarmBean.setReplay(data.getStringExtra("replay"));
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == 1) {
                if (data != null) {
                    alarm_remind.setText(data.getStringExtra("remind"));
                    alarmBean.setAlarmTime(data.getStringExtra("remind"));
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == 2) {
                if (data != null) {
                    alarm_local.setText(data.getStringExtra("local"));
                    alarmBean.setLocal(data.getStringExtra("local"));
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == 3) {
                if (data != null) {
                    alarm_color.setText(data.getStringExtra("color"));
                    alarmBean.setAlarmColor(data.getStringExtra("color"));
                    //动态改变颜色
                    int colorId = ColorUtils.getColorFromStr(data.getStringExtra("color"));
                    action_bar.setBackgroundColor(getResources().getColor(colorId));
                    Window window = getWindow();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(getResources().getColor(colorId));
                    }

                }
            }
        } else if (requestCode == 4) {
            if (resultCode == 4) {
                if (data != null) {
                    alarm_tone_Path.setText(data.getStringExtra("tone"));
                    alarmBean.setAlarmTonePath(data.getStringExtra("tonePath"));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
