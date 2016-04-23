package com.werb.mycalendardemo.alarmsetactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.werb.mycalendardemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by acer-pc on 2016/4/17.
 */
public class SetLocalActivity extends AppCompatActivity {

    @Bind(R.id.ed_local)
    EditText ed_local;
    @OnClick(R.id.tv_save) void saveAndClose(){
        Intent intent=new Intent();
        if(ed_local.getText().toString().equals("")){
            intent.putExtra("local", "无");
            setResult(2, intent);
            finish();
        }else {
            intent.putExtra("local", ed_local.getText().toString());
            setResult(2, intent);
            finish();
        }

    }

    @OnClick(R.id.left_local_back) void finishClose(){
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_local);
        ButterKnife.bind(this);


    }
}
