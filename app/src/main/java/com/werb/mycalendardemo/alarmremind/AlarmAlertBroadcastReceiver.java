package com.werb.mycalendardemo.alarmremind;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;

import com.werb.mycalendardemo.AlarmBean;

import java.io.IOException;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isVibrator = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReceiver.class);
        context.sendBroadcast(alarmServiceIntent, null);

        Bundle bundle = intent.getExtras();
        AlarmBean alarm = (AlarmBean) bundle.getSerializable("alarm");

        showAlarmDialog(context,alarm);
    }

    private void showAlarmDialog(Context context, AlarmBean bean) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        playMusicAndVibrate(context,bean);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("距离您定的日期 "+bean.getTitle()+" 已经到了哦！")
                .setMessage(bean.getDescription())
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            if(isVibrator){
                                vibrator.cancel();
                                isVibrator = false;
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * 播放音乐
     * @param context
     */
    private void playMusicAndVibrate(Context context,AlarmBean bean){
        Uri ringtoneUri = Uri.parse(bean.getAlarmTonePath());
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
        }try {
            mediaPlayer.setVolume(100f, 100f);
            mediaPlayer.setDataSource(context, ringtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if(bean.getIsVibrate()==1){
                vibrator.vibrate(new long[]{1000,50,1000,50},0);
                isVibrator = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
