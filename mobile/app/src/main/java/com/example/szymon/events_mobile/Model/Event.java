package com.example.szymon.events_mobile.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;

/**
 * Created by Szymon on 01.05.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class Event implements Parcelable {
    private Integer id;
    private String title;
    private String description;
    private Integer how_many_tickets;
    private Integer how_many_tickets_sold;
    private String start_date;

    public Event() {

    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", how_many_tickets=" + how_many_tickets +
                ", how_many_tickets_sold=" + how_many_tickets_sold +
                ", start_date='" + start_date + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHow_many_tickets() {
        return how_many_tickets;
    }

    public void setHow_many_tickets(Integer how_many_tickets) {
        this.how_many_tickets = how_many_tickets;
    }

    public Integer getHow_many_tickets_sold() {
        return how_many_tickets_sold;
    }

    public void setHow_many_tickets_sold(Integer how_many_tickets_sold) {
        this.how_many_tickets_sold = how_many_tickets_sold;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeValue(this.how_many_tickets);
        dest.writeValue(this.how_many_tickets_sold);
        dest.writeString(this.start_date);
    }

    protected Event(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
        this.how_many_tickets = (Integer) in.readValue(Integer.class.getClassLoader());
        this.how_many_tickets_sold = (Integer) in.readValue(Integer.class.getClassLoader());
        this.start_date = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public static Comparator<Event> eventTitleComparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    public static Comparator<Event> eventDescriptionComparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    };

    public static Comparator<Event> eventDateComparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getStart_date().compareTo(o2.getStart_date());
        }
    };
}
