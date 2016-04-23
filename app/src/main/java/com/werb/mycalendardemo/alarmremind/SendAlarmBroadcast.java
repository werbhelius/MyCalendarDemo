package com.werb.mycalendardemo.alarmremind;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class SendAlarmBroadcast {

    public static void startAlarmService(Activity activity){
        Intent startAlarmServiceIntent = new Intent(activity,AlarmServiceBroadcastReceiver.class);
        activity.sendBroadcast(startAlarmServiceIntent,null);
    }
}
