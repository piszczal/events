package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Szymon on 20.03.2017.
 */
@JsonAutoDetect
public class Event {

    public Integer id;
    public String title;
    public String description;
    public Integer how_many_tickets;
    public Integer how_many_tickets_sold;
    public String start_date;
    public List<Artist> artists_list;

    public Event(){

    }

    public Event(Integer id, String title, String description, Integer how_many_tickets, Integer how_many_tickets_sold, String start_date, List<Artist> artists_list) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.how_many_tickets = how_many_tickets;
        this.how_many_tickets_sold = how_many_tickets_sold;
        this.start_date = start_date;
        this.artists_list = artists_list;
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
                ", artists_list=" + artists_list +
                '}';
    }

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

    public static Comparator<Event> eventTicketsComparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getHow_many_tickets().compareTo(o2.getHow_many_tickets());
        }
    };

    public static Comparator<Event> eventDateComparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getStart_date().compareTo(o2.getStart_date());
        }
    };

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

    public List<Artist> getArtists_list() {
        return artists_list;
    }

    public void setArtists_list(List<Artist> artists_list) {
        this.artists_list = artists_list;
    }
}
