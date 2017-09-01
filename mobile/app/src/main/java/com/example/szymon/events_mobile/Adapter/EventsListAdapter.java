package com.example.szymon.events_mobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.R;

import java.util.List;

/**
 * Created by Szymon on 06.05.2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = EventsListAdapter.class.getSimpleName();
    private ViewHolder.ClickListener clickListener;
    private final static int FADE_DURATION = 500;
    private Context context;

    private List<Event> eventsList;

    public EventsListAdapter(ViewHolder.ClickListener clickListener, Context context) {
        super();
        this.clickListener = clickListener;
        this.context = context;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = eventsList.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(event.getId());
        holder.event_color.setBackgroundColor(color);
        holder.event_title.setText(event.getTitle());
        holder.event_description.setText(event.getDescription());
        holder.event_date.setText(event.getStart_date());

        if (event.getHow_many_tickets() > 0)
            holder.event_tickets_info.setText(context.getResources().getString(R.string.ticket_available) + " " + String.valueOf(event.getHow_many_tickets()));
        else
            holder.event_tickets_info.setText(context.getResources().getString(R.string.tickets_sold));

        setScaleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @SuppressWarnings("unused")
        private static final String TAG = ViewHolder.class.getSimpleName();
        private ClickListener clickListener;

        TextView event_color;
        TextView event_title;
        TextView event_description;
        TextView event_date;
        TextView event_tickets_info;

        public ViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);

            event_color = (TextView) itemView.findViewById(R.id.event_color_item);
            event_title = (TextView) itemView.findViewById(R.id.event_title_item);
            event_description = (TextView) itemView.findViewById(R.id.event_description_item);
            event_date = (TextView) itemView.findViewById(R.id.event_date_item);
            event_tickets_info = (TextView) itemView.findViewById(R.id.event_tickets_info);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClicked(getPosition());
        }

        public interface ClickListener {
            public void onItemClicked(int position);
        }

    }
}
