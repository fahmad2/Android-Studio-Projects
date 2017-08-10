package com.example.urfi.faizan_ahmad_assignment4;

import java.io.Serializable;

/**
 * Created by urfi on 3/19/17.
 */

public class Stock implements Serializable{

    protected String symbol;
    protected String name;
    protected String price;
    protected String change;

    public Stock(String s, String n, String p, String c){
        this.symbol = s;
        this.name = n;
        this.price = p;
        this.change = c;
    }

    //accessors

    public String getSymbol() { return this.symbol; }

    public String getName() { return this.name; }

    public String getPrice() { return this.price; }

    public String getChange() { return this.change; }

    //modifiers

    public void setSymbol(String s) { this.symbol = s; }

    public void setName(String n) { this.name = n; }

    public void setPrice(String p) { this.price = p; }

    public void setChange(String c) { this.change = c; }
}
