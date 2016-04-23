package com.werb.mycalendardemo.customview.calendar.weekslist;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.models.DayItem;
import com.werb.mycalendardemo.models.WeekItem;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.DateHelper;
import com.werb.mycalendardemo.utils.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeeksAdapter extends RecyclerView.Adapter<WeeksAdapter.WeekViewHolder> {

    public static final long FADE_DURATION = 250;//渐入渐出250毫秒

    private Context mContext;//上下文
    private Calendar mToday; //当前日期
    private List<WeekItem> mWeeksList = new ArrayList<>(); //数据集合
    private boolean mDragging; //是否拖拽
    private boolean mAlphaSet; //是否透明
    // 日期颜色，已过去的日期颜色，当前日期颜色
    private int mDayTextColor, mPastDayTextColor, mCurrentDayColor;

    // region Constructor

    public WeeksAdapter(Context context, Calendar today, int dayTextColor, int currentDayTextColor, int pastDayTextColor) {
        this.mToday = today;
        this.mContext = context;
        this.mDayTextColor = dayTextColor;
        this.mCurrentDayColor = currentDayTextColor;
        this.mPastDayTextColor = pastDayTextColor;
    }

    // endregion

    /**
     * 提供方法动态刷新Item
     *
     * @param weekItems
     */
    public void updateWeeksItems(List<WeekItem> weekItems) {
        this.mWeeksList.clear();
        this.mWeeksList.addAll(weekItems);
        //通过一个外部的方法控制适配器动态刷新每个Item的内容。
        notifyDataSetChanged();
    }

    // region Getters/setters

    public List<WeekItem> getWeeksList() {
        return mWeeksList;
    }

    /**
     * 返回拖拽状态
     *
     * @return
     */
    public boolean isDragging() {
        return mDragging;
    }

    /**
     * 判断是否拖拽，若为true则刷新数据
     *
     * @param dragging
     */
    public void setDragging(boolean dragging) {
        if (dragging != this.mDragging) {
            this.mDragging = dragging;
            //notifyItemRangeChanged设置区间段，可以实现局部刷新的效果
            notifyItemRangeChanged(0, mWeeksList.size());
        }
    }

    /**
     * 返回透明度设置状态
     *
     * @return
     */
    public boolean isAlphaSet() {
        return mAlphaSet;
    }

    /**
     * 设置透明度状态
     *
     * @param alphaSet
     */
    public void setAlphaSet(boolean alphaSet) {
        mAlphaSet = alphaSet;
    }

    // endregion

    // region RecyclerView.Adapter<WeeksAdapter.WeekViewHolder> methods
    // 继承 RecyclerView.Adapter 需要重写的方法

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //RecyclerView 绑定布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item_week, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder weekViewHolder, int position) {
        //绑定数据
        WeekItem weekItem = mWeeksList.get(position);
        weekViewHolder.bindWeek(weekItem, mToday);
    }

    @Override
    public int getItemCount() {
        return mWeeksList.size();
    }

    // endregion

    // region Class - WeekViewHolder

    public class WeekViewHolder extends RecyclerView.ViewHolder {

        /**
         * List of layout containers for each day
         */
        private List<LinearLayout> mCells;//7天日期的view布局
        private TextView mTxtMonth;//黑体显示的月份
        private FrameLayout mMonthBackground;

        //在ViewHolder的构造方法里初始化控件
        public WeekViewHolder(View itemView) {
            super(itemView);
            mTxtMonth = (TextView) itemView.findViewById(R.id.month_label);
            mMonthBackground = (FrameLayout) itemView.findViewById(R.id.month_background);
            LinearLayout daysContainer = (LinearLayout) itemView.findViewById(R.id.week_days_container);
            //把7天日期的view布局放进List集合中
            setUpChildren(daysContainer);
        }

        /**
         * RecyclerView 绑定数据
         * @param weekItem week数据
         * @param today  当前日期
         */
        public void bindWeek(WeekItem weekItem, Calendar today) {
            //设置动画渐入渐出的效果
            setUpMonthOverlay();

            //从一个星期中的到每一天dayItem
            List<DayItem> dayItems = weekItem.getDayItems();

            for (int c = 0; c < dayItems.size(); c++) {
                //得到dayItem的数据
                final DayItem dayItem = dayItems.get(c);
                //得到dayItem的布局,初始化布局控件
                LinearLayout cellItem = mCells.get(c);
                TextView txtDay = (TextView) cellItem.findViewById(R.id.view_day_day_label);
                TextView txtMonth = (TextView) cellItem.findViewById(R.id.view_day_month_label);
                View circleView = cellItem.findViewById(R.id.view_day_circle_selected);
                //设置每一天的点击事件
                cellItem.setOnClickListener(v -> BusProvider.getInstance().send(new Events.DayClickedEvent(dayItem)));

                txtMonth.setVisibility(View.GONE);
                //设置Day文字的颜色
                txtDay.setTextColor(mDayTextColor);
                //设置Month文字的颜色
                txtMonth.setTextColor(mDayTextColor);
                //设置文字上的圆圈为不显示
                circleView.setVisibility(View.GONE);

                //设置字体样式
                txtDay.setTypeface(null, Typeface.NORMAL);
                txtMonth.setTypeface(null, Typeface.NORMAL);

                // Display the day
                // 为Day设置数值,即Value和View绑定
                txtDay.setText(Integer.toString(dayItem.getValue()));

                // Highlight first day of the month
                // 每月的第一天高亮显示
                if (dayItem.isFirstDayOfTheMonth() && !dayItem.isSelected()) {//是每月的第一天，且没有被选择
                    txtMonth.setVisibility(View.VISIBLE);
                    txtMonth.setText(dayItem.getMonth());
                    //设置字体为粗体
                    txtDay.setTypeface(null, Typeface.BOLD);
                    txtMonth.setTypeface(null, Typeface.BOLD);
                }

                // Check if this day is in the past
                // 检查日期是否为过去,且不是同一天
                // after 判断此 Calendar 表示的时间是否在指定 Object 表示的时间之后，返回判断结果
                if (today.getTime().after(dayItem.getDate()) && !DateHelper.sameDate(today, dayItem.getDate())) {
                    //设置为过去日期的样式
                    txtDay.setTextColor(mPastDayTextColor);
                    txtMonth.setTextColor(mPastDayTextColor);
                }

                // Highlight the cell if this day is today
                // 当前日期且没有被选中，这是高亮样式
                if (dayItem.isToday() && !dayItem.isSelected()) {
                    txtDay.setTextColor(mCurrentDayColor);
                    circleView.setVisibility(View.VISIBLE);
                }

                // Show a circle if the day is selected
                // 如果日期被选中,显示选择圆圈
                if (dayItem.isSelected()) {
                    if(dayItem.isToday()){
                        txtDay.setTextColor(mCurrentDayColor);
                        circleView.setVisibility(View.VISIBLE);
                    }else {
                        txtDay.setTextColor(mDayTextColor);
                        circleView.setVisibility(View.VISIBLE);
                        circleView.setBackgroundResource(R.drawable.other_color_circle);
                    }
                }

                // Check if the month label has to be displayed
                // 判断月份标签是否被显示了
                if (dayItem.getValue() == 15) {//为什么是15？因为在一个月的最中间的一星期显示
                    mTxtMonth.setVisibility(View.VISIBLE);
                    SimpleDateFormat monthDateFormat = new SimpleDateFormat(mContext.getResources().getString(R.string.month_name_format), CalendarManager.getInstance().getLocale());
                    String month = monthDateFormat.format(weekItem.getDate()).toUpperCase();
                    //当前日期的年数不等于这个星期的年数，则需要在mTxtMonth上加上年份
                    if (today.get(Calendar.YEAR) != weekItem.getYear()) {
                        month = month + String.format(" %d", weekItem.getYear());
                    }
                    mTxtMonth.setText(month);
                    BusProvider.getInstance().send(new Events.CalendarMonthEvent(month));
                }
            }
        }

        /**
         * 把 7 天的 LinearLayout 布局放入集合中
         * @param daysContainer 7天日期 view 布局
         */
        private void setUpChildren(LinearLayout daysContainer) {
            mCells = new ArrayList<>();
            for (int i = 0; i < daysContainer.getChildCount(); i++) {
                mCells.add((LinearLayout) daysContainer.getChildAt(i));
            }
        }


        /**
         * 设置动画渐入渐出的效果
         */
        private void setUpMonthOverlay() {
            // 设置月份为不显示
            mTxtMonth.setVisibility(View.GONE);

            //判断是否为拖拽状态,若为拖拽则为透明状态
            if (isDragging()) {
                //动画集合(透明)
                AnimatorSet animatorSetFadeIn = new AnimatorSet();
                animatorSetFadeIn.setDuration(FADE_DURATION);
                //ObjectAnimator在动画的过程中自动更新属性值，这是通过调用该属性的set方法来实现
                //ofFloat,因为参数是float型的
                //参数:View目标对象,对象标识名,动画参数起始值,动画参数结束值
                ObjectAnimator animatorTxtAlphaIn = ObjectAnimator.ofFloat(mTxtMonth, "alpha", mTxtMonth.getAlpha(), 1f);
                ObjectAnimator animatorBackgroundAlphaIn = ObjectAnimator.ofFloat(mMonthBackground, "alpha", mMonthBackground.getAlpha(), 1f);
                //一起执行动画
                animatorSetFadeIn.playTogether(
                        animatorTxtAlphaIn,
                        animatorBackgroundAlphaIn
                );
                animatorSetFadeIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setAlphaSet(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorSetFadeIn.start();
            } else {//若不是拖拽则为显示状态
                //动画集合(显示)
                AnimatorSet animatorSetFadeOut = new AnimatorSet();
                animatorSetFadeOut.setDuration(FADE_DURATION);
                ObjectAnimator animatorTxtAlphaOut = ObjectAnimator.ofFloat(mTxtMonth, "alpha", mTxtMonth.getAlpha(), 0f);
                ObjectAnimator animatorBackgroundAlphaOut = ObjectAnimator.ofFloat(mMonthBackground, "alpha", mMonthBackground.getAlpha(), 0f);
                animatorSetFadeOut.playTogether(
                        animatorTxtAlphaOut,
                        animatorBackgroundAlphaOut
                );
                animatorSetFadeOut.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setAlphaSet(false);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorSetFadeOut.start();
            }

        }
    }

    // endregion
}
