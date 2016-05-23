package com.werb.mycalendardemo.alarmsetactivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.werb.mycalendardemo.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by acer-pc on 2016/4/18.
 */
public class SetAlarmToneActivity extends AppCompatActivity {

    @OnClick(R.id.left_alarm_tone_back) void finishClose(){
        finish();
    }
    @OnClick(R.id.tv_save) void saveTone(){
        Intent intent=new Intent();
        intent.putExtra("tone", toneName);
        intent.putExtra("tonePath",tonePath);
        setResult(4, intent);
        finish();
    }
    @Bind(R.id.tone_recycler)
    RecyclerView tone_recycler;
    private String toneName;
    private String tonePath;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_tone);

        ButterKnife.bind(this);

        tone_recycler.setLayoutManager(new LinearLayoutManager(this));
        SetAlarmToneAdapter adapter = new SetAlarmToneAdapter(this);
        tone_recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new SetAlarmToneAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data,int position) {
                Toast.makeText(SetAlarmToneActivity.this, data + "-" + position, Toast.LENGTH_SHORT).show();
                toneName = data;
                RingtoneManager rm = new RingtoneManager(SetAlarmToneActivity.this);
                rm.setType(RingtoneManager.TYPE_RINGTONE);
                rm.getCursor();
                Uri ringtoneUri = rm.getRingtoneUri(position);
                tonePath = ringtoneUri.toString();
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                } else {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(SetAlarmToneActivity.this, ringtoneUri);
                    mediaPlayer.setVolume(1f, 1f);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}
