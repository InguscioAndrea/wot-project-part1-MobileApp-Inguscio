package com.example.tabbedapplication.ui.Entities;

import java.io.Serializable;

public class Dataset_ML implements Serializable {

    private String current_time, value;

    public Dataset_ML(String current_time, String value) {
        this.current_time = current_time;
        this.value = value;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}




