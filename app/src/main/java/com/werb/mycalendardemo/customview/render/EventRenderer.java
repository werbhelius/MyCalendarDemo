package com.werb.mycalendardemo.customview.render;

import android.support.annotation.LayoutRes;
import android.view.View;
import com.werb.mycalendardemo.models.CalendarEvent;

import java.lang.reflect.ParameterizedType;

/**
 * Base class for helping layout rendering
 * 帮助 Layout 渲染的基类
 * Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
 * 这个类也看不懂
 */
public abstract class EventRenderer<T extends CalendarEvent> {

    // 初始化一些布局的信息
    public abstract void render(final View view, final T event);

    //返回一个布局文件
    @LayoutRes
    public abstract int getEventLayout();

    /**
     * 得到映射布局的泛型类型
     * @return 布局的泛型类型
     */
    public Class<T> getRenderType() {
        //ParameterizedType 表示参数化类型
        //getGenericSuperclass()获得带有泛型的父类
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        System.out.println("getClass de type ： "+ type.getActualTypeArguments()[0].toString());
        //返回表示此类型实际类型参数的 Type 对象的数组
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}
