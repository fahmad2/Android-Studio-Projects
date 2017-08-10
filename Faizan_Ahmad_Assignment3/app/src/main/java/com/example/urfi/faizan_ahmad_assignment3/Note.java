package com.example.urfi.faizan_ahmad_assignment3;

import java.io.Serializable;

/**
 * Created by urfi on 2/28/17.
 */

public class Note implements Serializable {

    protected String title;
    protected String date;
    protected String desc;

    public Note(String title, String s, String desc) {
        this.title = title;
        this.date = s;
        this.desc = desc;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDate(){
        return this.date;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setTitle(String tt){
        this.title = tt;
    }

    public void setDesc(String ds){
        this.desc = ds;
    }

    public void setDate(String dt){
        this.date = dt;
    }
}
