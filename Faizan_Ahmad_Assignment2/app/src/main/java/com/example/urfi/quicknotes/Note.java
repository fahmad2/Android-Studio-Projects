package com.example.urfi.quicknotes;

/**
 * Created by urfi on 2/12/17.
 */

public class Note {
    private String noteDesc;
    private String date;

    public String getNoteDesc(){
        return this.noteDesc;
    }

    public void setNoteDesc(String nd){
        this.noteDesc = nd;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String d){
        this.date = d;
    }

    public String toString(){
        return "The note is: "+this.noteDesc;
    }
}
