package com.werb.mycalendardemo.customview.agenda;

import android.content.Context;
import android.util.AttributeSet;

import com.werb.mycalendardemo.models.CalendarEvent;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.DateHelper;

import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * StickyListHeadersListView to scroll chronologically through events.
 * 按照时间先后顺序滑动显示event
 * StickyListHeadersListView 是一个可以实现滑动顶部停靠的ListView
 */
public class AgendaListView extends StickyListHeadersListView {

    // region Constructors
    // 初始化构造方法

    public AgendaListView(Context context) {
        super(context);
    }

    public AgendaListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgendaListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // endregion

    // region Public methods

    /**
     * 根据传入的日期使 ListView 滚到对应的日期
     * @param today 传入的日期
     */
    public void scrollToCurrentDate(Calendar today) {
        List<CalendarEvent> events = CalendarManager.getInstance().getEvents();

        int toIndex = 0;
        for (int i = 0; i < events.size(); i++) {
            if (DateHelper.sameDate(today, events.get(i).getInstanceDay())) {
                toIndex = i;
                break;
            }
        }

        final int finalToIndex = toIndex;
        post(()->setSelection(finalToIndex));

    }

    // endregion
}
