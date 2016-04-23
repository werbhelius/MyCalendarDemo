package com.werb.mycalendardemo.customview.agenda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.werb.mycalendardemo.R;


/**
 * List item view for the StickyHeaderListView of the agenda view
 * AgendaListView 中 显示日程事件信息的 view
 */
public class AgendaEventView extends LinearLayout {

    /**
     * 引入布局文件
     * @param parent AgendaEventView 的父布局
     * @return
     */
    public static AgendaEventView inflate(ViewGroup parent) {
        return (AgendaEventView) LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_view_event, parent, false);
    }

    // region Constructors

    public AgendaEventView(Context context) {
        this(context, null);
    }

    public AgendaEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgendaEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置padding
        setPadding(getResources().getDimensionPixelSize(R.dimen.agenda_event_view_padding_left),
                getResources().getDimensionPixelSize(R.dimen.agenda_event_view_padding_top),
                getResources().getDimensionPixelSize(R.dimen.agenda_event_view_padding_right),
                getResources().getDimensionPixelSize(R.dimen.agenda_event_view_padding_bottom));
    }

    // endregion
}
