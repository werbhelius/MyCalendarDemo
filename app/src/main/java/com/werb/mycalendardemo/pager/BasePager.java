package com.werb.mycalendardemo.pager;

import android.app.Activity;
import android.view.View;

/**
 *  Pager 页面的基类
 */
public abstract class BasePager {

    public Activity mActivity;
    public View mRootView;

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        this.mRootView = initView();
    }

    /**
     * 初始化 rootView
     * @return pager 布局
     */
    public abstract View initView();

    public void initData(){}
}
