package com.example.szymon.events_mobile.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Comparator;

/**
 * Created by Szymon on 01.05.2017.
 */
@JsonAutoDetect
public class Ticket implements Parcelable {

    private Integer id;
    private Integer user;
    private Event event;
    private String date;

    public Ticket() {

    }

    @Override
    public String toString() {
        return "Ticket{" +
                "user=" + user +
                ", event=" + event +
                ", date='" + date + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public static Comparator<Ticket> ticketTitleComparator = new Comparator<Ticket>() {
        @Override
        public int compare(Ticket o1, Ticket o2) {
            return o1.getEvent().getTitle().compareTo(o2.getEvent().getTitle());
        }
    };

    public static Comparator<Ticket> ticketDateComparator = new Comparator<Ticket>() {
        @Override
        public int compare(Ticket o1, Ticket o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user);
        dest.writeParcelable(this.event, flags);
        dest.writeString(this.date);
    }

    protected Ticket(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user = (Integer) in.readValue(Integer.class.getClassLoader());
        this.event = in.readParcelable(Event.class.getClassLoader());
        this.date = in.readString();
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel source) {
            return new Ticket(source);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}
