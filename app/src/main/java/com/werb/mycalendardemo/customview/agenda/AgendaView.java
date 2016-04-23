package com.werb.mycalendardemo.customview.agenda;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.Events;

/**
 * 日程 View 包括  AgendaListView 和 mShadowView(一个长方形的阴影，位于CalendarView下部)
 */
public class AgendaView extends FrameLayout {

    /**
     * 显示日程的 ListView
     */
    public AgendaListView mAgendaListView;
    /**
     * 一个长方形的阴影，位于CalendarView下部
     */
    private View mShadowView;

    // region Constructors

    public AgendaView(Context context) {
        super(context);
    }

    public AgendaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.agenda_view, this, true);
    }

    // endregion

    // region Class - View

    //当View中所有的子控件均被映射成xml后触发,及完成inflater之后
    //当View和它的所有子对象从XML中导入之后，调用此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //初始化布局
        mAgendaListView = (AgendaListView) findViewById(R.id.agenda_listview);
        mShadowView = findViewById(R.id.view_shadow);



        //BusProvider处理事件
        BusProvider.getInstance().toObserverable()
                .subscribe(event -> {
                    if (event instanceof Events.DayClickedEvent) {
                        //Day日期点击事件
                        Events.DayClickedEvent clickedEvent = (Events.DayClickedEvent) event;
                        //通过在Calendar上点击日期,下边的ListView滚动到正确匹配的日期
                        getAgendaListView().scrollToCurrentDate(clickedEvent.getCalendar());
                    } else if (event instanceof Events.CalendarScrolledEvent) {
                        //CalendarView 日历滑动事件
                        translateList(1);
                    } else if (event instanceof Events.EventsFetched) {
                        //日期与事件匹配完成
                        //日期事件匹配成功后,更新数据适配器
                        ((AgendaAdapter) getAgendaListView().getAdapter()).updateEvents(CalendarManager.getInstance().getEvents());
                        System.out.println("---onAgendaView---调用了updateEvents");
                        getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());


                    } else if (event instanceof Events.ForecastFetched) {
                        //应该是完成天气预报匹配后,刷新数据
                        ((AgendaAdapter) getAgendaListView().getAdapter()).updateEvents(CalendarManager.getInstance().getEvents());
                    }
                });
    }

    //触摸事件分发机制
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                // if the user touches the listView, we put it back to the top
                // 如果用户点击触摸AgendaListView 则将高度变为0,即取消CalendarView折叠
                translateList(0);
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    // endregion

    // region Public methods

    /**
     * 得到 AgendaView 下的日程 View AgendaListView
     *
     * @return
     */
    public AgendaListView getAgendaListView() {
        return mAgendaListView;
    }

    /**
     * 通过传入的距离,来使 AgendaView 配合 CalendarView 移动
     *
     * @param targetY 传入的目标 Y 值
     */
    public void translateList(int targetY) {
        if (targetY != getTranslationY()) {
            System.out.println("getTranslationY=" + getTranslationY());
            System.out.println("targetY=" + targetY);
            //属性动画,通过修改view的属性达到动画的效果
            ObjectAnimator mover = ObjectAnimator.ofFloat(this, "translationY", targetY);
            mover.setDuration(150);
            //动画监听事件
            mover.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    //滑动动画开始时,隐藏阴影
                    mShadowView.setVisibility(GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //即在动画结束后，点击 AgendaView ,发送消息事件,收缩CalendarView
                    if (targetY == 0) {
                        BusProvider.getInstance().send(new Events.AgendaListViewTouchedEvent());
                    }
                    mShadowView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mover.start();
        }
    }


    // endregion
}
