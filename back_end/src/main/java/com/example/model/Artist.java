package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Szymon on 21.03.2017.
 */
@JsonAutoDetect
public class Artist {

    public Integer id;
    public String name;
    public String description;
    public String about_artist;
    public Integer event;

    public Artist() {

    }

    public Artist(Integer id, String name, String description, String about_artist, Integer event) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.about_artist = about_artist;
        this.event = event;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", about_artist='" + about_artist + '\'' +
                ", event=" + event +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout_artist() {
        return about_artist;
    }

    public void setAbout_artist(String about_artist) {
        this.about_artist = about_artist;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
