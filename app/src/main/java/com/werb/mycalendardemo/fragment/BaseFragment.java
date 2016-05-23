package com.werb.mycalendardemo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment的基类
 * Created by acer-pc on 2016/3/11.
 */
public abstract class BaseFragment extends Fragment{

    public Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return initView();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initDate();
    }

    /**
     * 初始化界面
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initDate(){

    }
}
