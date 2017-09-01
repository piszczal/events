package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Szymon on 24.04.2017.
 */
@JsonAutoDetect
public class Conspiration {

    private String email;
    private String screen_name;

    public Conspiration() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }
}
