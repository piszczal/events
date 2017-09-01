package com.example.szymon.events_mobile.Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Szymon on 21.05.2017.
 */
@JsonAutoDetect
public class Token {

    private String token;

    public Token() {

    }

    public Token(String user_token) {
        this.token = user_token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
