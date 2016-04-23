package com.werb.mycalendardemo.alarmsetactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.werb.mycalendardemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by acer-pc on 2016/4/17.
 */
public class SetColorActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox color_zi,color_lv,color_huang,color_hong,color_hui,color_juhong,color_shenlan;
    @OnClick(R.id.left_color_back) void finishClose(){
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_color);
        ButterKnife.bind(this);
        color_zi = (CheckBox) findViewById(R.id.color_zi);
        color_lv = (CheckBox) findViewById(R.id.color_lv);
        color_huang = (CheckBox) findViewById(R.id.color_huang);
        color_hong = (CheckBox) findViewById(R.id.color_hong);
        color_hui = (CheckBox) findViewById(R.id.color_hui);
        color_juhong = (CheckBox) findViewById(R.id.color_juhong);
        color_shenlan = (CheckBox) findViewById(R.id.color_shenlan);

        color_zi.setChecked(true);

        color_zi.setOnClickListener(this);
        color_lv.setOnClickListener(this);
        color_huang.setOnClickListener(this);
        color_hong.setOnClickListener(this);
        color_hui.setOnClickListener(this);
        color_juhong.setOnClickListener(this);
        color_shenlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.color_zi:
                color_zi.setChecked(true);
                color_lv.setChecked(false);
                color_huang.setChecked(false);
                color_hong.setChecked(false);
                color_hui.setChecked(false);
                color_juhong.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "默认颜色");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_lv:
                color_lv.setChecked(true);
                color_zi.setChecked(false);
                color_huang.setChecked(false);
                color_hong.setChecked(false);
                color_hui.setChecked(false);
                color_juhong.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "罗勒绿");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_huang:
                color_huang.setChecked(true);
                color_lv.setChecked(false);
                color_zi.setChecked(false);
                color_hong.setChecked(false);
                color_hui.setChecked(false);
                color_juhong.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "耀眼黄");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_hong:
                color_hong.setChecked(true);
                color_lv.setChecked(false);
                color_zi.setChecked(false);
                color_huang.setChecked(false);
                color_hui.setChecked(false);
                color_juhong.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "番茄红");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_hui:
                color_hui.setChecked(true);
                color_lv.setChecked(false);
                color_zi.setChecked(false);
                color_huang.setChecked(false);
                color_hong.setChecked(false);
                color_juhong.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "低调灰");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_juhong:
                color_juhong.setChecked(true);
                color_lv.setChecked(false);
                color_zi.setChecked(false);
                color_huang.setChecked(false);
                color_hui.setChecked(false);
                color_hui.setChecked(false);
                color_shenlan.setChecked(false);
                intent.putExtra("color", "橘子红");
                setResult(3, intent);
                finish();
                break;
            case R.id.color_shenlan:
                color_shenlan.setChecked(true);
                color_lv.setChecked(false);
                color_zi.setChecked(false);
                color_huang.setChecked(false);
                color_hui.setChecked(false);
                color_juhong.setChecked(false);
                color_juhong.setChecked(false);
                intent.putExtra("color", "深空蓝");
                setResult(3, intent);
                finish();
                break;
        }
    }
}
