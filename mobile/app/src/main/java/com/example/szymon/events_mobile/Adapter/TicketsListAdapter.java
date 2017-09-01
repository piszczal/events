package com.example.szymon.events_mobile.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Szymon on 22.05.2017.
 */

public class TicketsListAdapter extends RecyclerView.Adapter<TicketsListAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = TicketsListAdapter.class.getSimpleName();
    private ViewHolder.ClickListener clickListener;
    private final static int FADE_DURATION = 500;
    private Context context;

    private List<Ticket> ticketsList;

    public TicketsListAdapter(ViewHolder.ClickListener clickListener, Context context) {
        super();
        this.clickListener = clickListener;
        this.context = context;
    }

    public void setEventsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ticket ticket = ticketsList.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(ticket.getEvent().getId());
        holder.ticketColor.setBackgroundColor(color);

        holder.ticketCount.setText(position + 1 + "/" + ticketsList.size());
        holder.ticketTitle.setText(ticket.getEvent().getTitle());
        holder.ticketBuyDate.setText("Kupiony: " + ticket.getDate());
        holder.ticketExpDate.setText("Wygasa: " + ticket.getEvent().getStart_date());
        setScaleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return ticketsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @SuppressWarnings("unused")
        private static final String TAG = ViewHolder.class.getSimpleName();
        private ViewHolder.ClickListener clickListener;

        TextView ticketColor;
        TextView ticketCount;
        TextView ticketTitle;
        TextView ticketBuyDate;
        TextView ticketExpDate;


        public ViewHolder(View itemView, ViewHolder.ClickListener clickListener) {
            super(itemView);

            ticketColor = (TextView) itemView.findViewById(R.id.ticket_color_item);
            ticketCount = (TextView) itemView.findViewById(R.id.ticket_count);
            ticketTitle = (TextView) itemView.findViewById(R.id.ticket_title_item);
            ticketBuyDate = (TextView) itemView.findViewById(R.id.ticket_buy_date_item);
            ticketExpDate = (TextView) itemView.findViewById(R.id.ticket_date_item);

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
