package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;

/**
 * Created by Szymon on 28.03.2017.
 */
@JsonAutoDetect
public class Log {

    public Integer ID;
    public String date;
    public String description;
    public String module;

    public Log(){

    }

    public Log(Integer ID, String date, String description, String module) {
        this.ID = ID;
        this.date = date;
        this.description = description;
        this.module = module;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer code) {
        this.ID = code;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
