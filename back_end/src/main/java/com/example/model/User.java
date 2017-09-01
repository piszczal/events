package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Szymon on 06.04.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class User {

    public Integer id;
    public String name;
    public String screen_name;
    public String location;
    public String email;
    public String oauth_token;
    public String oauth_token_secret;

    public User() {

    }

    public void setAuthorizationToken(String oauth_token, String oauth_token_secret) {
        this.oauth_token = oauth_token;
        this.oauth_token_secret = oauth_token_secret;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", oauth_token='" + oauth_token + '\'' +
                ", oauth_token_secret='" + oauth_token_secret + '\'' +
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

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public String getOauth_token_secret() {
        return oauth_token_secret;
    }

    public void setOauth_token_secret(String oauth_token_secret) {
        this.oauth_token_secret = oauth_token_secret;
    }
}
