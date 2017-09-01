package com.example.szymon.events_mobile.Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Szymon on 01.05.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class User {

    private Long social_id;
    private String screen_name;
    private String name;
    private String email;
    private String location;
    private String oauth_token;

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "social_id=" + social_id +
                ", screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", oauth_token='" + oauth_token + '\'' +
                '}';
    }

    public Long getSocial_id() {
        return social_id;
    }

    public void setSocial_id(Long social_id) {
        this.social_id = social_id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }
}
