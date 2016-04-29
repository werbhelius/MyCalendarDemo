package com.werb.mycalendardemo.utils;

import com.werb.mycalendardemo.R;

/**
 * Created by acer-pc on 2016/4/28.
 */
public class ColorUtils {

    public static int getColorFromStr(String s){
        int colorId = 0;
        switch (s) {
            case "默认颜色":
                colorId = R.color.moren;
                break;
            case "罗勒绿":
                colorId = R.color.luolelv;
                break;
            case "耀眼黄":
                colorId = R.color.yaoyanhuang;
                break;
            case "番茄红":
                colorId = R.color.fanqiehong;
                break;
            case "低调灰":
                colorId = R.color.didiaohui;
                break;
            case "橘子红":
                colorId = R.color.juzihong;
                break;
            case "深空蓝":
                colorId = R.color.shenkonglan;
                break;
        }
        return colorId;
    }
}
