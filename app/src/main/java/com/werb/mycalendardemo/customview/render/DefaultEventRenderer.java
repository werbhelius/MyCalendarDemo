package com.werb.mycalendardemo.customview.render;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.models.BaseCalendarEvent;


/**
 * Class helping to inflate our default layout in the AgendaAdapter
 * 默认的Event布局渲染类
 */
public class DefaultEventRenderer extends EventRenderer<BaseCalendarEvent> {

    // region class - EventRenderer

    @Override
    public void render(@NonNull View view, @NonNull BaseCalendarEvent event) {
        TextView alarmId = (TextView) view.findViewById(R.id.Alarm_id);
        alarmId.setText(event.getId()+"");
        TextView txtTitle = (TextView) view.findViewById(R.id.view_agenda_event_title);
        TextView txtLocation = (TextView) view.findViewById(R.id.view_agenda_event_location);
        TextView StartAndEndTime = (TextView) view.findViewById(R.id.view_agenda_event_startAndEndTime);
        TextView description = (TextView) view.findViewById(R.id.view_agenda_event_description);
        LinearLayout descriptionContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_description_container);
        LinearLayout locationContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_location_container);

        descriptionContainer.setVisibility(View.VISIBLE);
        txtTitle.setTextColor(view.getResources().getColor(android.R.color.white));
        txtTitle.setText(event.getTitle());
        txtLocation.setText(event.getLocation());
        StartAndEndTime.setTextColor(view.getResources().getColor(android.R.color.white));
        StartAndEndTime.setText(event.getmStartAndEndTime());
        description.setTextColor(view.getResources().getColor(android.R.color.white));
        description.setText(event.getDescription());
        if (event.getLocation().length() > 0) {
            locationContainer.setVisibility(View.VISIBLE);
            txtLocation.setText(event.getLocation());
        } else {
            locationContainer.setVisibility(View.GONE);
        }

        if (event.getTitle().equals(view.getResources().getString(R.string.agenda_event_no_events))) {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
        } else {
            txtTitle.setTextColor(view.getResources().getColor(android.R.color.white));
        }
        descriptionContainer.setBackgroundColor(event.getColor());
        txtLocation.setTextColor(view.getResources().getColor(android.R.color.white));
    }

    @Override
    public int getEventLayout() {
        return R.layout.agenda_view_event;
    }

    // endregion
}
